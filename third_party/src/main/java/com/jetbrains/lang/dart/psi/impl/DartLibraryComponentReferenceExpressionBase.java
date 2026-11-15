// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import consulo.language.ast.ASTNode;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiPolyVariantReference;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ResolveResult;
import consulo.language.psi.resolve.ResolveCache;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.resolve.DartResolver;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DartLibraryComponentReferenceExpressionBase extends DartExpressionImpl implements DartReference, PsiPolyVariantReference {
  public DartLibraryComponentReferenceExpressionBase(ASTNode node) {
    super(node);
  }

  @Override
  public @NotNull PsiElement getElement() {
    return this;
  }

  @Override
  public PsiReference getReference() {
    return this;
  }

  @Override
  public @NotNull TextRange getRangeInElement() {
    final TextRange textRange = getTextRange();
    return new TextRange(0, textRange.getEndOffset() - textRange.getStartOffset());
  }

  @Override
  public @NotNull String getCanonicalText() {
    return getText();
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    final DartId identifier = PsiTreeUtil.getChildOfType(this, DartId.class);
    final DartId identifierNew = DartElementGenerator.createIdentifierFromText(getProject(), newElementName);
    if (identifier != null && identifierNew != null) {
      getNode().replaceChild(identifier.getNode(), identifierNew.getNode());
    }
    return this;
  }

  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    return this;
  }

  @Override
  public boolean isReferenceTo(@NotNull PsiElement element) {
    return resolve() == element;
  }

  @Override
  public boolean isSoft() {
    return false;
  }

  @Override
  public PsiElement resolve() {
    final ResolveResult[] resolveResults = multiResolve(true);

    return resolveResults.length != 1 ||
           !resolveResults[0].isValidResult() ? null : resolveResults[0].getElement();
  }

  @Override
  public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
    final List<? extends PsiElement> elements =
      ResolveCache.getInstance(getProject()).resolveWithCaching(this, DartResolver.INSTANCE, true, incompleteCode);
    return DartResolveUtil.toCandidateInfoArray(elements);
  }

  @Override
  public @NotNull DartClassResolveResult resolveDartClass() {
    return DartClassResolveResult.EMPTY;
  }
}