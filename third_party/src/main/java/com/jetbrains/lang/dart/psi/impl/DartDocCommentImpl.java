// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import consulo.language.impl.psi.ASTWrapperPsiElement;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.ReferenceProvidersRegistry;
import consulo.language.ast.IElementType;
import com.jetbrains.lang.dart.psi.DartDocComment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartDocCommentImpl extends ASTWrapperPsiElement implements DartDocComment {

  public DartDocCommentImpl(final @NotNull ASTNode node) {
    super(node);
  }

  @Override
  public @Nullable PsiElement getOwner() {
    return null; // todo
  }

  @Override
  public @NotNull IElementType getTokenType() {
    return getNode().getElementType();
  }

  @Override
  public PsiReference @NotNull [] getReferences() {
    return ReferenceProvidersRegistry.getReferencesFromProviders(this);
  }
}
