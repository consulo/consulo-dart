// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.template.macro;

import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public final class DartSuggestIndexNameMacro extends DartMacroBase {
  @Override
  public String getName() {
    return "dartSuggestIndexName";
  }

  @Override
  public @NotNull String getDefaultValue() {
    return "i";
  }

  @Override
  public Result calculateResult(Expression @NotNull [] params, ExpressionContext context) {
    final PsiElement at = context.getPsiElementAtStartOffset();
    final Set<String> names = DartRefactoringUtil.collectUsedNames(at);
    for (char i = 'i'; i < 'z'; ++i) {
      if (!names.contains(Character.toString(i))) {
        return new TextResult(Character.toString(i));
      }
    }
    return null;
  }
}
