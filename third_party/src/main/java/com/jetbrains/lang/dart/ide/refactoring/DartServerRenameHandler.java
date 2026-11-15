// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.refactoring;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.CommonBundle;
import consulo.language.editor.refactoring.TitledHandler;
import consulo.language.editor.CommonDataKeys;
import consulo.dataContext.DataContext;
import consulo.codeEditor.Caret;
import consulo.codeEditor.Editor;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.scope.LocalSearchScope;
import consulo.language.editor.refactoring.rename.RenameHandler;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.refactoring.status.RefactoringStatus;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;

// todo implement ContextAwareActionHandler?
@ExtensionImpl
public final class DartServerRenameHandler implements RenameHandler, TitledHandler {
  @Override
  public @NotNull LocalizeValue getActionTitleValue() {
    return DartBundle.messageValue("action.title.dart.rename.refactoring");
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext context) {
    showRenameDialog(project, editor, context);
  }

  @Override
  public void invoke(@NotNull Project project, PsiElement @NotNull [] elements, DataContext context) {
    // Dart file rename is not handled using server yet
  }

  @Override
  public boolean isAvailableOnDataContext(final @NotNull DataContext dataContext) {
    final Editor editor = dataContext.getData(CommonDataKeys.EDITOR);
    if (editor == null) return false;

    final VirtualFile file = dataContext.getData(CommonDataKeys.VIRTUAL_FILE);
    if (!DartAnalysisServerService.isLocalAnalyzableFile(file)) return false;

    final PsiElement psiElement = dataContext.getData(CommonDataKeys.PSI_ELEMENT);
    if (psiElement != null) {
      if (psiElement.getUseScope() instanceof LocalSearchScope) {
        // Standard VariableInplaceRenameHandler or PsiElementRenameHandler will do the trick (based on existing navigation data from Analysis Server)
        return false;
      }
      return psiElement.getLanguage() == DartLanguage.INSTANCE && !(psiElement instanceof PsiFile);
    }

    // in case of comment (that also may contain reference that is valid to rename) psiElement is null
    final PsiFile psiFile = dataContext.getData(CommonDataKeys.PSI_FILE);
    final PsiElement elementAtOffset = psiFile == null ? null : psiFile.findElementAt(editor.getCaretModel().getOffset());

    return elementAtOffset != null && elementAtOffset.getLanguage() == DartLanguage.INSTANCE;
  }

  private static void showRenameDialog(@NotNull Project project, @NotNull Editor editor, DataContext context) {
    final PsiFile psiFile = context.getData(CommonDataKeys.PSI_FILE);
    final VirtualFile virtualFile = context.getData(CommonDataKeys.VIRTUAL_FILE);
    if (psiFile == null || virtualFile == null) {
      return;
    }
    // Prepare the offset in the editor or of the selected element.
    final int offset;
    {
      final Caret caret = context.getData(CommonDataKeys.CARET);
      final PsiElement element = context.getData(CommonDataKeys.PSI_ELEMENT);
      if (caret != null) {
        offset = caret.getOffset();
      }
      else if (element != null) {
        offset = element.getTextOffset();
      }
      else {
        return;
      }
    }
    // Create the refactoring.
    final ServerRenameRefactoring refactoring = new ServerRenameRefactoring(project, virtualFile, offset, 0);
    // Validate initial status.
    {
      final RefactoringStatus initialStatus = refactoring.checkInitialConditions();
      if (initialStatus == null) {
        return;
      }
      if (initialStatus.hasError()) {
        final String message = initialStatus.getMessage();
        assert message != null;
        CommonRefactoringUtil.showErrorHint(project, editor, message, CommonBundle.getErrorTitle(), null);
        return;
      }
    }
    // Show the rename dialog.
    new DartRenameDialog(project, editor, refactoring).show();
  }
}

