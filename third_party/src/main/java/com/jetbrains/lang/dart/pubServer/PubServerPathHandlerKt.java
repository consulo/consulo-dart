// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.pubServer;

import com.google.common.net.UrlEscapers;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.util.DartUrlResolver;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import consulo.application.progress.ProgressManager;
import consulo.logging.Logger;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.ProjectRootManager;
import consulo.project.Project;
import consulo.util.lang.Pair;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Top-level functions of upstream {@code PubServerPathHandler.kt}.
 */
public final class PubServerPathHandlerKt {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(PubServerPathHandler.class);

  private PubServerPathHandlerKt() {
  }

  private static @Nullable VirtualFile findFileInContentRoots(@NotNull Project project, @NotNull String path) {
    final String relativePath = StringUtil.trimStart(path, "/");
    for (VirtualFile root : ProjectRootManager.getInstance(project).getContentRoots()) {
      ProgressManager.checkCanceled();
      final VirtualFile file = root.findFileByRelativePath(relativePath);
      if (file != null) {
        return file;
      }
    }
    return null;
  }

  private static @NotNull String removeProjectNamePrefix(@NotNull String path, @Nullable String projectName) {
    if (StringUtil.isEmpty(projectName)) return path;
    final String trimmed = StringUtil.trimStart(path, "/");
    if (trimmed.equals(projectName)) return "";
    if (trimmed.startsWith(projectName + "/")) return trimmed.substring(projectName.length());
    return path;
  }

  public static @Nullable Pair<VirtualFile, String> getServedDirAndPathForPubServer(@NotNull Project project, @NotNull String path) {
    return getServedDirAndPathForPubServer(project, path, null);
  }

  public static @Nullable Pair<VirtualFile, String> getServedDirAndPathForPubServer(@NotNull Project project,
                                                                                    @NotNull String path,
                                                                                    @Nullable String projectName) {
    final String effectivePath = removeProjectNamePrefix(path, projectName);

    // File with requested path may not exist, pub server will generate and serve it.
    // Here we find deepest (if nested) Dart project (aka Dart package) folder and its existing subfolder that can be served by pub server.

    // There may be 2 content roots with web/foo.html and web/bar.html files in them correspondingly. We need to catch the correct 'web' folder.
    // First see if full path can be resolved to a file
    final VirtualFile file = findFileInContentRoots(project, effectivePath);
    if (file != null && ProjectFileIndex.getInstance(project).isInContent(file)) {
      return getServedDirAndPathForPubServer(project, file);
    }

    // If above failed then take the longest path part that corresponds to an existing folder
    VirtualFile servedDir = null;
    String pubServePath = null;

    int slashIndex = -1;
    while (true) {
      ProgressManager.checkCanceled();
      slashIndex = effectivePath.indexOf('/', slashIndex + 1);
      if (slashIndex < 0) {
        break;
      }

      final String pathPart = effectivePath.substring(0, slashIndex);
      final VirtualFile dir = findFileInContentRoots(project, pathPart);
      if (dir == null || !dir.isDirectory()) {
        continue;
      }

      final VirtualFile parentDir = dir.getParent();
      if (parentDir != null && parentDir.findChild(PubspecYamlUtil.PUBSPEC_YAML) != null) {
        final CharSequence name = dir.getNameSequence();
        if (StringUtil.equals(name, "build") || StringUtil.equals(name, "lib") || StringUtil.equals(name, DartUrlResolver.PACKAGES_FOLDER_NAME)) {
          // contents of "build" folder should be served by the IDE internal web server directly, i.e. without pub serve
          return null;
        }

        servedDir = dir;
        pubServePath = effectivePath.substring(slashIndex);
        // continue looking for nested Dart project
      }
    }

    return servedDir == null ? null : Pair.create(servedDir, escapeUrl(pubServePath));
  }

  public static @Nullable Pair<VirtualFile, String> getServedDirAndPathForPubServer(@NotNull Project project, @NotNull VirtualFile file) {
    final VirtualFile pubspecYamlFile = PubspecYamlUtil.findPubspecYamlFile(project, file);
    final VirtualFile dartRoot = pubspecYamlFile == null ? null : pubspecYamlFile.getParent();
    if (dartRoot == null) return null;

    final String relativePath = VirtualFileUtil.getRelativePath(file, dartRoot);
    // we only handle files 2 levels deeper than the Dart project root
    final int slashIndex = relativePath == null ? -1 : relativePath.indexOf('/');
    final String folderName = slashIndex == -1 ? null : relativePath.substring(0, slashIndex);
    if (folderName == null || folderName.equals("build") || folderName.equals("lib") || DartUrlResolver.PACKAGES_FOLDER_NAME.equals(folderName)) {
      return null;
    }

    final String pubServePath = relativePath.substring(slashIndex);
    return Pair.create(dartRoot.findChild(folderName), escapeUrl(pubServePath));
  }

  private static @NotNull String escapeUrl(@NotNull String path) {
    try {
      // need to restore slash separators after UrlEscapers.urlPathSegmentEscaper work
      return StringUtil.replace(UrlEscapers.urlPathSegmentEscaper().escape(path), "%2F", "/");
    }
    catch (Exception e) {
      LOG.warn(path, e);
      return path;
    }
  }
}
