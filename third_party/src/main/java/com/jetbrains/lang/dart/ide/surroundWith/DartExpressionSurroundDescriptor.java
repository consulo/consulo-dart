package com.jetbrains.lang.dart.ide.surroundWith;

import consulo.language.editor.surroundWith.SurroundDescriptor;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithBracketsExpressionSurrounder;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithNotParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.ide.surroundWith.expression.DartWithParenthesisExpressionSurrounder;
import com.jetbrains.lang.dart.psi.DartExpression;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import org.jetbrains.annotations.NotNull;

public final class DartExpressionSurroundDescriptor implements SurroundDescriptor {
  @Override
  public PsiElement @NotNull [] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
    final DartExpression result = DartRefactoringUtil.findExpressionInRange(file, startOffset, endOffset);
    return result == null ? PsiElement.EMPTY_ARRAY : new PsiElement[]{result};
  }

  @Override
  public Surrounder @NotNull [] getSurrounders() {
    return new Surrounder[]{
      new DartWithParenthesisExpressionSurrounder(),
      new DartWithNotParenthesisExpressionSurrounder(),
      new DartWithBracketsExpressionSurrounder(),
    };
  }

  @Override
  public boolean isExclusive() {
    return false;
  }
}
