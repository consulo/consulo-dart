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

public class DartShowCombinatorImpl extends DartPsiCompositeElementImpl implements DartShowCombinator {

  public DartShowCombinatorImpl(ASTNode node) {
    super(node);
  }

  @Override
  @NotNull
  public DartLibraryReferenceList getLibraryReferenceList() {
    return findNotNullChildByClass(DartLibraryReferenceList.class);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof DartVisitor) ((DartVisitor)visitor).visitShowCombinator(this);
    else super.accept(visitor);
  }

}
