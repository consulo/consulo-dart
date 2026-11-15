// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.surroundWith.expression;

import consulo.localize.LocalizeValue;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartNamedArgument;
import org.jetbrains.annotations.NotNull;

public class DartWithBracketsExpressionSurrounder extends DartWithExpressionSurrounder {

  @Override
  public boolean isApplicable(PsiElement @NotNull [] elements) {
    // Limit this to named arguments; the intent is to convert a Flutter child: param to children:, which may involve creating red code.
    return super.isApplicable(elements) && elements[0].getParent() instanceof DartNamedArgument;
  }

  @Override
  public @NotNull LocalizeValue getTemplateDescription() {
    //noinspection DialogTitleCapitalization
    return LocalizeValue.of(DartBundle.message("dart.surround.with.brackets"));
  }

  @Override
  protected String getTemplateText(PsiElement expr) {
    return expr.textContains('\n')
           ? "[\n" + expr.getText() + ",]\n"
           : "[" + expr.getText() + "]";
  }
}
