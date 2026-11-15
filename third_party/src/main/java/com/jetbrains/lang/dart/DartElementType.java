package com.jetbrains.lang.dart;

import consulo.language.ast.IElementType;

public class DartElementType extends IElementType {
  public DartElementType(String debug_description) {
    super(debug_description, DartLanguage.INSTANCE);
  }
}
