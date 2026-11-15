// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.sdk;

import consulo.annotation.component.ExtensionImpl;
import consulo.project.Project;
import consulo.content.library.LibraryType;
import consulo.content.library.NewLibraryConfiguration;
import consulo.content.library.PersistentLibraryKind;
import consulo.content.library.ui.LibraryEditorComponent;
import consulo.content.library.ui.LibraryPropertiesEditor;
import consulo.virtualFileSystem.VirtualFile;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@ExtensionImpl
public final class DartPackagesLibraryType extends LibraryType<DartPackagesLibraryProperties> {

  public static final String DART_PACKAGES_LIBRARY_NAME = "Dart Packages";

  public static final PersistentLibraryKind<DartPackagesLibraryProperties> LIBRARY_KIND =
    new PersistentLibraryKind<>("DartPackagesLibraryType") {
      @Override
      public @NotNull DartPackagesLibraryProperties createDefaultProperties() {
        return new DartPackagesLibraryProperties();
      }
    };

  private DartPackagesLibraryType() {
    super(LIBRARY_KIND);
  }

  @Override
  public @Nullable String getCreateActionName() {
    return null;
  }

  @Override
  public @Nullable NewLibraryConfiguration createNewLibrary(final @NotNull JComponent parentComponent,
                                                            final @Nullable VirtualFile contextDirectory,
                                                            final @NotNull Project project) {
    return null;
  }

  @Override
  public @Nullable LibraryPropertiesEditor createPropertiesEditor(final @NotNull LibraryEditorComponent<DartPackagesLibraryProperties> editorComponent) {
    return null;
  }

  @Override
  public @Nullable Icon getIcon(DartPackagesLibraryProperties properties) {
    return DartIcons.Dart_16;
  }
}
