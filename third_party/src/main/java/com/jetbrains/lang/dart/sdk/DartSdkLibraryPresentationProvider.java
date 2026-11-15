// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.ui.image.Image;
import consulo.content.library.DummyLibraryProperties;
import consulo.content.library.LibraryKind;
import consulo.content.library.LibraryPresentationProvider;
import consulo.virtualFileSystem.VirtualFile;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

@ExtensionImpl
public final class DartSdkLibraryPresentationProvider extends LibraryPresentationProvider<DummyLibraryProperties> {

  private static final LibraryKind KIND = LibraryKind.create("dart");

  private DartSdkLibraryPresentationProvider() {
    super(KIND);
  }

  @Override
  public @Nullable Image getIcon() {
    return DartIcons.Dart_16;
  }

  @Override
  public @Nullable DummyLibraryProperties detect(final @NotNull List<VirtualFile> classesRoots) {
    return findDartCoreRoot(classesRoots) == null ? null : DummyLibraryProperties.INSTANCE;
  }

  public static @Nullable VirtualFile findDartCoreRoot(final @NotNull List<? extends VirtualFile> classesRoots) {
    for (VirtualFile root : classesRoots) {
      if (root.isInLocalFileSystem() &&
          root.isDirectory() &&
          root.getPath().endsWith("/lib/core") &&
          root.findChild("core.dart") != null) {
        return root;
      }
    }
    return null;
  }
}
