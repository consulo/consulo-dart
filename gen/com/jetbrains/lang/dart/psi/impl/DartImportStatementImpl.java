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

public class DartImportStatementImpl extends DartPsiCompositeElementImpl implements DartImportStatement {

  public DartImportStatementImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitImportStatement(this);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public DartComponentName getComponentName() {
    return findChildByClass(DartComponentName.class);
  }

  @Override
  @NotNull
  public List<DartHideCombinator> getHideCombinatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartHideCombinator.class);
  }

  @Override
  @NotNull
  public DartPathOrLibraryReference getPathOrLibraryReference() {
    return findNotNullChildByClass(DartPathOrLibraryReference.class);
  }

  @Override
  @NotNull
  public List<DartShowCombinator> getShowCombinatorList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, DartShowCombinator.class);
  }

  @NotNull
  public String getLibraryName() {
    return DartPsiImplUtil.getLibraryName(this);
  }

  @Override
  @NotNull
  public DartPathOrLibraryReference getLibraryExpression() {
    return getPathOrLibraryReference();
  }

  @Nullable
  public PsiElement getLibraryPrefix() {
    return DartPsiImplUtil.getLibraryPrefix(this);
  }

}
