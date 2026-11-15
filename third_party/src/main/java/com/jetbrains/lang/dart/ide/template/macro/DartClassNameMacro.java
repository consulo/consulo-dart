// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.template.macro;

import consulo.annotation.component.ExtensionImpl;
import com.google.common.annotations.VisibleForTesting;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartClassNameMacro extends DartMacroBase {
  @Override
  public String getName() {
    return "dartClassName";
  }

  @Override
  public Result calculateResult(Expression @NotNull [] params, final ExpressionContext context) {
    final String containingClassName = getContainingClassName(context.getPsiElementAtStartOffset());
    return containingClassName == null ? null : new TextResult(containingClassName);
  }

  @VisibleForTesting
  public @Nullable String getContainingClassName(final @Nullable PsiElement element) {
    if (element == null) return null;
    final DartClass dartClass = PsiTreeUtil.getParentOfType(element, DartClass.class);
    return dartClass == null ? null : dartClass.getName();
  }
}
