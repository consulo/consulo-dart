// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart;

import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressManager;
import consulo.content.base.ExcludedContentFolderTypeProvider;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.search.FilenameIndex;
import consulo.language.util.ModuleUtilCore;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.*;

/**
 * Top-level functions of upstream {@code DartStartupActivity.kt}. Consulo has no workspace model,
 * exclusions are applied with {@link ModifiableRootModel}.
 */
public final class DartStartupActivityKt {
  private DartStartupActivityKt() {
  }

  @VisibleForTesting
  public static @NotNull Map<Module, Map<String, Set<String>>> collectExclusionsByModule(@NotNull Project project) {
    final Map<Module, Map<String, Set<String>>> exclusions = new HashMap<>();
    final Map<Module, Set<String>> exclusionsCache = new HashMap<>();
    final ProjectFileIndex fileIndex = ProjectFileIndex.getInstance(project);
    final Collection<VirtualFile> pubspecYamlFiles =
      FilenameIndex.getVirtualFilesByName(project, PubspecYamlUtil.PUBSPEC_YAML, GlobalSearchScope.projectScope(project));
    for (VirtualFile file : pubspecYamlFiles) {
      // Allow the platform to cancel this background scanning task if the user starts typing
      ProgressManager.checkCanceled();
      final Module module = ModuleUtilCore.findModuleForFile(file, project);
      if (module == null) continue;
      final VirtualFile root = file.getParent();
      if (root == null) continue;
      final VirtualFile contentRoot = fileIndex.getContentRootForFile(root);
      if (contentRoot == null) continue;
      final String rootUrl = root.getUrl();

      // Cache already-excluded roots per module to prevent redundant lookups in monorepos
      final Set<String> existingExclusions =
        exclusionsCache.computeIfAbsent(module, m -> new HashSet<>(Arrays.asList(ModuleRootManager.getInstance(m).getExcludeRootUrls())));

      final Set<String> urlsToExclude = getExclusionUrls(rootUrl);
      urlsToExclude.removeAll(existingExclusions);
      if (!urlsToExclude.isEmpty()) {
        exclusions.computeIfAbsent(module, m -> new HashMap<>())
          .computeIfAbsent(contentRoot.getUrl(), u -> new HashSet<>())
          .addAll(urlsToExclude);
      }
    }
    return exclusions;
  }

  /**
   * Consulo replacement of upstream {@code applyExclusionsToWorkspaceModel}, must be called in a write action.
   */
  public static void applyExclusions(@NotNull Map<Module, Map<String, Set<String>>> exclusionsByModule) {
    for (Map.Entry<Module, Map<String, Set<String>>> moduleEntry : exclusionsByModule.entrySet()) {
      final Module module = moduleEntry.getKey();
      if (module.isDisposed()) continue;

      final ModifiableRootModel model = ModuleRootManager.getInstance(module).getModifiableModel();
      boolean changed = false;
      for (ContentEntry contentEntry : model.getContentEntries()) {
        final Set<String> urls = moduleEntry.getValue().get(contentEntry.getUrl());
        if (urls == null) continue;

        final Set<String> existingUrls =
          new HashSet<>(Arrays.asList(contentEntry.getFolderUrls(ExcludedContentFolderTypeProvider.getInstance()::equals)));
        final String prefix = contentEntry.getUrl() + "/";
        for (String url : urls) {
          if (url.startsWith(prefix) && existingUrls.add(url)) {
            contentEntry.addFolder(url, ExcludedContentFolderTypeProvider.getInstance());
            changed = true;
          }
        }
      }

      if (changed) {
        model.commit();
      }
      else {
        model.dispose();
      }
    }
  }

  public static void excludeBuildAndToolCacheFolders(@NotNull Module module, @NotNull VirtualFile pubspecYamlFile) {
    if (module.isDisposed()) return;
    final VirtualFile root = pubspecYamlFile.getParent();
    if (root == null) return;
    final Project project = module.getProject();
    final VirtualFile contentRoot = ProjectFileIndex.getInstance(project).getContentRootForFile(root);
    if (contentRoot == null) return;
    final String rootUrl = root.getUrl();

    final Set<String> urlsToExclude = getExclusionUrls(rootUrl);
    urlsToExclude.removeAll(Arrays.asList(ModuleRootManager.getInstance(module).getExcludeRootUrls()));
    if (urlsToExclude.isEmpty()) return;

    final Map<Module, Map<String, Set<String>>> exclusionsByModule = Map.of(module, Map.of(contentRoot.getUrl(), urlsToExclude));
    ApplicationManager.getApplication().runWriteAction(() -> {
      if (!module.isDisposed()) {
        applyExclusions(exclusionsByModule);
      }
    });
  }

  private static @NotNull Set<String> getExclusionUrls(@NotNull String rootUrl) {
    return new HashSet<>(Set.of(rootUrl + "/.dart_tool", rootUrl + "/.pub", rootUrl + "/build"));
  }
}
