package com.jetbrains.lang.dart.ide.template.macro;

import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.PsiElementResult;
import consulo.language.editor.template.Result;
import consulo.language.psi.PsiElement;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public abstract class DartFilterByClassMacro extends DartMacroBase {
  @Override
  public Result calculateResult(Expression @NotNull [] params, ExpressionContext context) {
    final PsiElement at = context.getPsiElementAtStartOffset();
    final Set<DartComponentName> variables = DartRefactoringUtil.collectUsedComponents(at);
    final List<DartComponentName> filtered = ContainerUtil.filter(variables, name -> {
      final PsiElement nameParent = name.getParent();
      if (nameParent instanceof DartClass) {
        return false;
      }
      final DartClassResolveResult result = DartResolveUtil.getDartClassResolveResult(nameParent);
      final DartClass dartClass = result.getDartClass();
      return dartClass != null && filter(dartClass);
    });
    return filtered.isEmpty() ? null : new PsiElementResult(filtered.getFirst());
  }

  protected abstract boolean filter(@NotNull DartClass dartClass);
}
