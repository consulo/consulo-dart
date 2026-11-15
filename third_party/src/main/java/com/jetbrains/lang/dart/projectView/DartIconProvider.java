// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.projectView;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.analyzer.DartFileInfoKt;
import com.jetbrains.lang.dart.psi.DartFile;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.component.util.Iconable;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.ProjectRootManager;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import consulo.ui.image.OverlayPosition;
import consulo.util.lang.lazy.LazyValue;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

import static com.jetbrains.lang.dart.util.PubspecYamlUtil.PUBSPEC_YAML;

@ExtensionImpl
public final class DartIconProvider implements IconDescriptorUpdater {
  // Consulo: ScratchFileTypeIcon -> scratch overlay
  private static final Supplier<Image> DART_NOT_LOCAL_FILE_ICON =
    LazyValue.notNull(() -> ImageEffects.overlay(DartFileType.INSTANCE.getIcon(), PlatformIconGroup.actionsScratch(), OverlayPosition.BOTTOM_RIGHT));
  private static final Image FOLDER_SYMLINK_ICON = ImageEffects.layered(PlatformIconGroup.nodesFolder(), PlatformIconGroup.nodesSymlink());
  static final Image EXCLUDED_FOLDER_SYMLINK_ICON =
    ImageEffects.layered(PlatformIconGroup.modulesExcluderoot(), PlatformIconGroup.nodesSymlink());

  @RequiredReadAction
  @Override
  public void updateIcon(@NotNull IconDescriptor iconDescriptor, @NotNull PsiElement element, int flags) {
    Image icon = getIcon(element, flags);
    if (icon != null) {
      iconDescriptor.setMainIcon(icon);
    }
  }

  public @Nullable Image getIcon(final @NotNull PsiElement element, @Iconable.IconFlags final int flags) {
    if (element instanceof DartFile) {
      VirtualFile file = ((PsiFile)element).getVirtualFile();
      if (file != null && file.getUserData(DartFileInfoKt.DART_NOT_LOCAL_FILE_URI_KEY) != null) {
        return DART_NOT_LOCAL_FILE_ICON.get();
      }
    }

    if (element instanceof PsiDirectory) {
      final VirtualFile folder = ((PsiDirectory)element).getVirtualFile();

      if (isFolderNearPubspecYaml(folder, "build")) {
        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(element.getProject()).getFileIndex();
        return fileIndex.isExcluded(folder) ? PlatformIconGroup.modulesGeneratedexcluderoot()
                                            : PlatformIconGroup.modulesGeneratedroot();
      }
      if (isFolderNearPubspecYaml(folder, "web")) return PlatformIconGroup.nodesWebfolder();
      if (isFolderNearPubspecYaml(folder, "test")) return PlatformIconGroup.modulesTestroot();
      if (isFolderNearPubspecYaml(folder, "tool")) return PlatformIconGroup.nodesKeymaptools();
      if (isFolderNearPubspecYaml(folder.getParent(), "packages")) {
        final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(element.getProject()).getFileIndex();
        return fileIndex.isExcluded(folder) ? EXCLUDED_FOLDER_SYMLINK_ICON
                                            : FOLDER_SYMLINK_ICON;
      }
    }

    return null;
  }

  public static boolean isFolderNearPubspecYaml(@Nullable VirtualFile folder, @NotNull String folderName) {
    if (folder != null && folder.isDirectory() && folder.isInLocalFileSystem() && folderName.equals(folder.getName())) {
      final VirtualFile parentFolder = folder.getParent();
      final VirtualFile pubspecYamlFile = parentFolder != null ? parentFolder.findChild(PUBSPEC_YAML) : null;
      return pubspecYamlFile != null;
    }
    return false;
  }
}
