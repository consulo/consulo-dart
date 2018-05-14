package com.jetbrains.lang.dart.ide.refactoring;

import javax.annotation.Nullable;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.refactoring.RefactoringActionHandler;
import com.jetbrains.lang.dart.ide.refactoring.extract.DartExtractMethodHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceFinalVariableHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartIntroduceVariableHandler;
import com.jetbrains.lang.dart.psi.DartNamedElement;

/**
 * @author: Fedor.Korotkov
 */
public class DartRefactoringSupportProvider extends RefactoringSupportProvider {
  @Override
  public boolean isInplaceRenameAvailable(PsiElement element, PsiElement context) {
    return element instanceof DartNamedElement &&
           element.getUseScope() instanceof LocalSearchScope;
  }

  @Override
  public RefactoringActionHandler getIntroduceVariableHandler() {
    return new DartIntroduceVariableHandler();
  }

  @Nullable
  @Override
  public RefactoringActionHandler getIntroduceConstantHandler() {
    return new DartIntroduceFinalVariableHandler();
  }

  @Nullable
  @Override
  public RefactoringActionHandler getExtractMethodHandler() {
    return new DartExtractMethodHandler();
  }
}
