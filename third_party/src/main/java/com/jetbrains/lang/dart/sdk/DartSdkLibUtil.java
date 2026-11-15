// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.lang.dart.sdk;

import com.google.common.annotations.VisibleForTesting;
import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.dart.module.extension.DartModuleExtension;
import consulo.dart.module.extension.DartMutableModuleExtension;
import consulo.dart.sdk.DartSdkType;
import consulo.disposer.Disposable;
import consulo.language.editor.WriteCommandAction;
import consulo.language.util.ModuleUtilCore;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.content.layer.orderEntry.ModuleExtensionWithSdkOrderEntry;
import consulo.module.content.layer.orderEntry.OrderEntry;
import consulo.content.library.LibraryTable;
import consulo.project.Project;
import consulo.util.collection.ArrayUtil;
import consulo.util.collection.SmartList;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.*;

/**
 * Consulo: upstream keeps the Dart SDK as the "Dart SDK" project library and enables Dart in a module by a dependency on it.
 * Here the SDK is a {@link DartSdkType} SDK and Dart is enabled by {@link DartModuleExtension}; public methods are kept,
 * so upstream callers do not change.
 */
public final class DartSdkLibUtil {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartSdkLibUtil.class);

  private static final String[] SDK_LIB_SUBFOLDERS_BLACKLIST = {
    "analysis_server",
    "analyzer",
    "dev_compiler",
    "front_end",
    "internal",
    "kernel",
    "profiler",
    "web_sql",
  };

  public static boolean isIdeWithMultipleModuleSupport() {
    return true;
  }

  public static void ensureDartSdkConfigured(final @NotNull Project project, final @NotNull String sdkHomePath) {
    configureDartSdkAndReturnUndoingDisposable(project, sdkHomePath);
  }

  /**
   * @return Disposable which undoes configuration
   */
  @VisibleForTesting
  public static @NotNull Disposable configureDartSdkAndReturnUndoingDisposable(final @NotNull Project project, final @NotNull String sdkHomePath) {
    final Sdk existing = findDartSdkByHomePath(sdkHomePath);
    if (existing != null) return () -> {};

    final Sdk sdk = createDartSdk(sdkHomePath);
    return () -> WriteCommandAction.runWriteCommandAction(null, () -> SdkTable.getInstance().removeSdk(sdk));
  }

  public static void ensureDartSdkConfigured(final @NotNull Project project,
                                             final @NotNull LibraryTable.ModifiableModel libraryTableModel,
                                             final @NotNull String sdkHomePath) {
    ensureDartSdkConfigured(project, sdkHomePath);
  }

  public static @Nullable Sdk findDartSdkByHomePath(final @NotNull String sdkHomePath) {
    final String path = VirtualFileUtil.pathToUrl(sdkHomePath);
    for (Sdk sdk : SdkTable.getInstance().getSdksOfType(DartSdkType.getInstance())) {
      final String homePath = sdk.getHomePath();
      if (homePath != null && VirtualFileUtil.pathToUrl(homePath).equals(path)) {
        return sdk;
      }
    }
    return null;
  }

  private static @NotNull Sdk createDartSdk(final @NotNull String sdkHomePath) {
    LocalFileSystem.getInstance().refreshAndFindFileByPath(sdkHomePath + "/lib");

    final DartSdkType sdkType = DartSdkType.getInstance();
    final String version = DartSdkUtil.getSdkVersion(sdkHomePath);
    final String name = version == null ? DartSdk.DART_SDK_LIB_NAME : DartSdk.DART_SDK_LIB_NAME + " " + version;
    final Sdk sdk = SdkTable.getInstance().createSdk(Path.of(sdkHomePath), name, sdkType);
    sdkType.setupSdkPaths(sdk);
    SdkTable.getInstance().addSdk(sdk);
    return sdk;
  }

  private static @Nullable Sdk findAnyDartSdk() {
    return SdkTable.getInstance().findMostRecentSdkOfType(DartSdkType.getInstance());
  }

  private static @NotNull SortedSet<String> getRootUrls(final @NotNull Project project, final @NotNull String sdkHomePath) {
    final SortedSet<String> result = getRootUrlsFromLibrariesFile(project, sdkHomePath);

    if (result.isEmpty() || !result.contains(VirtualFileUtil.pathToUrl(sdkHomePath + "/lib/core"))) {
      LOG.info("Failed to get useful info from " + sdkHomePath + "/lib/_internal/libraries.dart");
      return getRootUrlsFailover(sdkHomePath);
    }

    return result;
  }

  @VisibleForTesting
  public static @NotNull SortedSet<String> getRootUrlsFromLibrariesFile(@NotNull Project project, @NotNull String sdkHomePath) {
    final Map<String, String> map = DartLibraryIndex.getSdkLibUriToRelativePathMap(project, sdkHomePath);
    final SortedSet<String> result = new TreeSet<>();

    for (Map.Entry<String, String> entry : map.entrySet()) {
      if (entry.getKey().startsWith("dart:_")) continue; // private libs

      final String relPath = entry.getValue();
      final int slashIndex = relPath.indexOf("/");
      if (slashIndex <= 0) {
        LOG.info("Skipping unexpected Dart library path: " + relPath);
        continue;
      }

      result.add(VirtualFileUtil.pathToUrl(sdkHomePath + "/lib/" + relPath.substring(0, slashIndex)));
    }
    return result;
  }

  @VisibleForTesting
  public static @NotNull SortedSet<String> getRootUrlsFailover(final @NotNull String sdkHomePath) {
    final SortedSet<String> result = new TreeSet<>();

    final File lib = new File(sdkHomePath + "/lib");
    if (!lib.isDirectory()) return result;

    final File[] children = lib.listFiles();
    if (children == null) return result;

    for (File subDir : children) {
      final String subDirName = subDir.getName();
      if (!subDir.isDirectory()) continue;
      if (subDirName.startsWith("_")) continue;
      if (ArrayUtil.contains(subDirName, SDK_LIB_SUBFOLDERS_BLACKLIST)) continue;

      result.add(VirtualFileUtil.pathToUrl(sdkHomePath + "/lib/" + subDirName));
    }

    return result;
  }

  public static boolean isDartSdkEnabled(final @NotNull Module module) {
    return ModuleUtilCore.getExtension(module, DartModuleExtension.class) != null;
  }

  public static void enableDartSdk(final @NotNull Module module) {
    enableDartSdkAndReturnUndoingDisposable(module);
  }

  /**
   * @return Disposable which undoes configuration
   */
  @VisibleForTesting
  public static @NotNull Disposable enableDartSdkAndReturnUndoingDisposable(final @NotNull Module module) {
    if (isDartSdkEnabled(module)) return () -> {};

    final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
    try {
      setDartEnabled(modifiableModel, true);
      modifiableModel.commit();
      return () -> {
        if (module.isDisposed()) return;

        WriteCommandAction.runWriteCommandAction(null, () -> {
          ModifiableRootModel m = ModuleRootManager.getInstance(module).getModifiableModel();
          setDartEnabled(m, false);
          m.commit();
        });
      };
    }
    catch (Exception e) {
      LOG.error(e);
      if (!modifiableModel.isDisposed()) modifiableModel.dispose();
    }
    return () -> {};
  }

  private static void setDartEnabled(final @NotNull ModifiableRootModel model, final boolean enabled) {
    final DartMutableModuleExtension extension = model.getExtensionWithoutCheck(DartMutableModuleExtension.class);
    if (extension == null) return;

    extension.setEnabled(enabled);
    if (enabled && extension.getSdk() == null) {
      final Sdk sdk = findAnyDartSdk();
      if (sdk != null) {
        extension.getInheritableSdk().set(null, sdk);
      }
    }
  }

  public static void disableDartSdk(final @NotNull Collection<? extends Module> modules) {
    if (modules.isEmpty()) return;

    final List<ModifiableRootModel> models = new SmartList<>();

    for (final Module module : modules) {
      final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
      setDartEnabled(modifiableModel, false);
      models.add(modifiableModel);
    }

    commitModifiableModels(models);
  }

  public static Collection<Module> getModulesWithDartSdkEnabled(final @NotNull Project project) {
    final Collection<Module> result = new SmartList<>();

    for (final Module module : ModuleManager.getInstance(project).getModules()) {
      if (isDartSdkEnabled(module)) {
        result.add(module);
      }
    }

    return result;
  }

  public static void enableDartSdkForSpecifiedModulesAndDisableForOthers(final @NotNull Project project,
                                                                         final Module @NotNull [] modulesWithDart) {
    final List<ModifiableRootModel> modelsToCommit = new SmartList<>();

    for (final Module module : ModuleManager.getInstance(project).getModules()) {
      final boolean mustHaveDart = ArrayUtil.contains(module, modulesWithDart);
      if (mustHaveDart == isDartSdkEnabled(module)) continue;

      final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
      setDartEnabled(modifiableModel, mustHaveDart);
      modelsToCommit.add(modifiableModel);
    }

    commitModifiableModels(modelsToCommit);
  }

  public static boolean isDartSdkOrderEntry(final @NotNull OrderEntry orderEntry) {
    return orderEntry instanceof ModuleExtensionWithSdkOrderEntry sdkOrderEntry &&
           sdkOrderEntry.getModuleExtension() instanceof DartModuleExtension;
  }

  private static void commitModifiableModels(final @NotNull Collection<ModifiableRootModel> modelsToCommit) {
    for (ModifiableRootModel model : modelsToCommit) {
      try {
        model.commit();
      }
      finally {
        if (!model.isDisposed()) {
          model.dispose();
        }
      }
    }
  }
}
