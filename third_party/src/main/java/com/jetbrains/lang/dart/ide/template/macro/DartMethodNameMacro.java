// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.template.macro;

import com.google.common.annotations.VisibleForTesting;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DartMethodNameMacro extends DartMacroBase {
  @Override
  public String getName() {
    return "dartMethodName";
  }

  @Override
  public Result calculateResult(Expression @NotNull [] params, final ExpressionContext context) {
    final String containingFunctionName = getContainingFunctionName(context.getPsiElementAtStartOffset());
    return containingFunctionName == null ? null : new TextResult(containingFunctionName);
  }

  @VisibleForTesting
  public @Nullable String getContainingFunctionName(final @Nullable PsiElement element) {
    if (element == null) return null;
    final DartComponent parent = PsiTreeUtil.getParentOfType(element,
                                                             DartGetterDeclaration.class,
                                                             DartSetterDeclaration.class,
                                                             DartFunctionDeclarationWithBodyOrNative.class,
                                                             DartFactoryConstructorDeclaration.class,
                                                             DartNamedConstructorDeclaration.class,
                                                             DartMethodDeclaration.class,
                                                             DartFunctionDeclarationWithBody.class);
    if (parent == null) return null;
    final DartComponentName componentName = parent.getComponentName();
    return componentName == null ? null : componentName.getName();
  }
}

