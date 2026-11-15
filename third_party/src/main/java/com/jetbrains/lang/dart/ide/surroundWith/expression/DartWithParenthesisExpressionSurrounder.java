// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.surroundWith.expression;

import org.jetbrains.annotations.NotNull;
import consulo.localize.LocalizeValue;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.DartBundle;

public class DartWithParenthesisExpressionSurrounder extends DartWithExpressionSurrounder {
  @Override
  protected String getTemplateText(PsiElement expr) {
    return "(" + expr.getText() + ")";
  }

  @Override
  public @NotNull LocalizeValue getTemplateDescription() {
    //noinspection DialogTitleCapitalization
    return LocalizeValue.of(DartBundle.message("dart.surround.with.parenthesis"));
  }
}
