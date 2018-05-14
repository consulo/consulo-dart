// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import javax.annotation.Nonnull;

import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPsiImplUtil;

public class DartNamedArgumentImpl extends DartPsiCompositeElementImpl implements DartNamedArgument {

  public DartNamedArgumentImpl(ASTNode node) {
    super(node);
  }

  public void accept(@Nonnull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitNamedArgument(this);
    else super.accept(visitor);
  }

  @Override
  @Nonnull
  public List<DartExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartExpression.class);
  }

  public DartExpression getParameterReferenceExpression() {
    return DartPsiImplUtil.getParameterReferenceExpression(this);
  }

  public DartExpression getExpression() {
    return DartPsiImplUtil.getExpression(this);
  }

}
