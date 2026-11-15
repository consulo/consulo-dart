// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart;

import consulo.language.file.LanguageFileType;
import consulo.localize.LocalizeValue;
import consulo.ui.image.Image;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;

public final class DartFileType extends LanguageFileType {
  public static final LanguageFileType INSTANCE = new DartFileType();
  public static final String DEFAULT_EXTENSION = "dart";

  private DartFileType() {
    super(DartLanguage.INSTANCE);
  }

  @Override
  public @NotNull String getId() {
    return "Dart";
  }

  @Override
  public @NotNull LocalizeValue getDescription() {
    return DartBundle.messageValue("filetype.dart.description");
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return DEFAULT_EXTENSION;
  }

  @Override
  public Image getIcon() {
    return DartIcons.Dart_file;
  }
}
