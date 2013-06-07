// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.jetbrains.lang.dart.DartTokenTypes.*;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPsiImplUtil;

public class DartNewExpressionImpl extends DartReferenceImpl implements DartNewExpression {

  public DartNewExpressionImpl(ASTNode node) {
    super(node);
  }

  @Override
  @Nullable
  public DartArguments getArguments() {
    return findChildByClass(DartArguments.class);
  }

  @Override
  @Nullable
  public DartReferenceExpression getReferenceExpression() {
    return findChildByClass(DartReferenceExpression.class);
  }

  @Override
  @Nullable
  public DartType getType() {
    return findChildByClass(DartType.class);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitNewExpression(this);
    else super.accept(visitor);
  }

}
