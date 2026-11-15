package com.jetbrains.lang.dart.ide.surroundWith;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.surroundWith.SurroundDescriptor;
import consulo.language.editor.surroundWith.Surrounder;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import com.jetbrains.lang.dart.ide.surroundWith.statement.*;
import com.jetbrains.lang.dart.util.DartRefactoringUtil;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartStatementsSurroundDescriptor implements SurroundDescriptor {
  @Override
  public PsiElement @NotNull [] getElementsToSurround(PsiFile file, int startOffset, int endOffset) {
    return DartRefactoringUtil.findStatementsInRange(file, startOffset, endOffset);
  }

  @Override
  public Surrounder @NotNull [] getSurrounders() {
    return new Surrounder[]{
      new DartWithIfSurrounder(),
      new DartWithIfElseSurrounder(),
      new DartWithWhileSurrounder(),
      new DartWithDoWhileSurrounder(),
      new DartWithForSurrounder(),
      new DartWithTryCatchSurrounder(),
      new DartWithTryCatchFinallySurrounder()
    };
  }

  @Override
  public boolean isExclusive() {
    return false;
  }
}
