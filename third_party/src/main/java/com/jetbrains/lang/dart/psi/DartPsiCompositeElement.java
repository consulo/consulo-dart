package com.jetbrains.lang.dart.psi;

import consulo.language.psi.NavigatablePsiElement;
import consulo.language.ast.IElementType;

public interface DartPsiCompositeElement extends NavigatablePsiElement {
  IElementType getTokenType();
}
