package com.jetbrains.lang.dart.ide.template.macro;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartClassNameMethodNameMacro extends DartMacroBase {
  @Override
  public String getName() {
    return "dartClassNameMethodName";
  }

  @Override
  public Result calculateResult(Expression @NotNull [] params, final ExpressionContext context) {
    final Result classNameResult = (new DartClassNameMacro()).calculateResult(params, context);
    final Result methodNameResult = (new DartMethodNameMacro()).calculateResult(params, context);

    if (classNameResult != null && methodNameResult != null) {
      return new TextResult(classNameResult + "." + methodNameResult);
    }
    else if (classNameResult == null && methodNameResult != null) {
      return new TextResult(methodNameResult.toString());
    }
    else if (classNameResult != null) {
      return new TextResult(classNameResult.toString());
    }
    else {
      return null;
    }
  }
}

