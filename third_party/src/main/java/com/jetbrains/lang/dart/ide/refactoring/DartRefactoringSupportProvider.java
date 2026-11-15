// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.refactoring;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.refactoring.RefactoringSupportProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.editor.refactoring.action.RefactoringActionHandler;
import com.jetbrains.lang.dart.ide.refactoring.extract.DartServerExtractMethodHandler;
import com.jetbrains.lang.dart.ide.refactoring.introduce.DartServerExtractLocalVariableHandler;
import com.jetbrains.lang.dart.psi.DartNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartRefactoringSupportProvider extends RefactoringSupportProvider {
  @Override
  public boolean isInplaceRenameAvailable(@NotNull PsiElement element, PsiElement context) {
    return element instanceof DartNamedElement &&
           element.getUseScope() instanceof LocalSearchScope;
  }

  @Override
  public RefactoringActionHandler getIntroduceVariableHandler() {
    return new DartServerExtractLocalVariableHandler();
  }

  @Override
  public @Nullable RefactoringActionHandler getExtractMethodHandler() {
    return new DartServerExtractMethodHandler();
  }
}
