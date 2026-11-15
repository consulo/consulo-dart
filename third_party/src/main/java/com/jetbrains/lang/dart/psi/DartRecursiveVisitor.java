package com.jetbrains.lang.dart.psi;

import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiRecursiveVisitor;
import org.jetbrains.annotations.NotNull;

public class DartRecursiveVisitor extends DartVisitor implements PsiRecursiveVisitor {
  @Override
  public void visitElement(@NotNull PsiElement element) {
    element.acceptChildren(this);
  }
}
