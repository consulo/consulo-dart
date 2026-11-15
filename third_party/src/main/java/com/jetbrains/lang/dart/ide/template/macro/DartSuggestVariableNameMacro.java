// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.template.macro;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartSuggestVariableNameMacro extends DartMacroBase {
  @Override
  public String getName() {
    return "dartSuggestVariableName";
  }

  @Override
  public @NotNull String getDefaultValue() {
    return "o";
  }

  @Override
  public Result calculateResult(Expression @NotNull [] params, ExpressionContext context) {
    return null;
  }
}
