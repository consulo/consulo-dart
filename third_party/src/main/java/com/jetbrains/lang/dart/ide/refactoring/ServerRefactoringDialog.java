// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.refactoring;

import consulo.application.CommonBundle;
import consulo.application.ApplicationManager;
import consulo.ui.ModalityState;
import consulo.application.WriteAction;
import consulo.codeEditor.Editor;
import consulo.configurable.ConfigurationException;
import consulo.project.Project;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.ui.ex.awt.Messages;

import consulo.language.editor.refactoring.ui.RefactoringDialog;
import com.jetbrains.lang.dart.assists.AssistUtils;
import com.jetbrains.lang.dart.assists.DartSourceEditException;
import com.jetbrains.lang.dart.ide.refactoring.status.RefactoringStatus;
import com.jetbrains.lang.dart.logging.PluginLogger;
import org.dartlang.analysis.server.protocol.SourceChange;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.BooleanSupplier;

public abstract class ServerRefactoringDialog<T extends ServerRefactoring> extends RefactoringDialog {
  protected final @Nullable Editor myEditor;
  protected final @NotNull T myRefactoring;

  private boolean myHasPendingRequests;
  private RefactoringStatus myOptionsStatus;

  public ServerRefactoringDialog(final @NotNull Project project, final @Nullable Editor editor, final @NotNull T refactoring) {
    super(project, true);
    myEditor = editor;
    myRefactoring = refactoring;
    // Listen for responses.
    myRefactoring.setListener((hasPendingRequests, optionsStatus) -> {
      final Runnable runnable = () -> {
        myHasPendingRequests = hasPendingRequests;
        myOptionsStatus = optionsStatus;
        validateButtons();
      };

      final ModalityState modalityState = ApplicationManager.getApplication().getModalityStateForComponent(getWindow());

      final BooleanSupplier expired = () -> !isShowing();

      ApplicationManager.getApplication().invokeLater(runnable, modalityState, expired);
    });
  }

  @Override
  protected void canRun() throws ConfigurationException {
    // has pending requests
    if (myHasPendingRequests || myOptionsStatus == null) {
      throw new ConfigurationException(LocalizeValue.empty());
    }
    // has a fatal error
    if (myOptionsStatus.hasFatalError()) {
      throw new ConfigurationException(myOptionsStatus.getMessage());
    }
  }

  @Override
  protected void doAction() {
    // Validate final status.
    {
      final RefactoringStatus finalStatus = myRefactoring.checkFinalConditions();
      if (finalStatus == null) {
        return;
      }
      if (finalStatus.hasError()) {
        Messages.showErrorDialog(myProject, finalStatus.getMessage(), CommonBundle.getErrorTitle());
        return;
      }
    }

    if (hasPreviewButton() && isPreviewUsages() || isForcePreview()) {
      previewRefactoring();
    }
    else {
      doRefactoring(myRefactoring.getPotentialEdits());
    }

    close(DialogWrapper.OK_EXIT_CODE);
  }

  @Override
  protected boolean hasPreviewButton() {
    return false;
  }

  protected boolean isForcePreview() {
    return false;
  }

  protected void previewRefactoring() {
  }

  @Override
  protected boolean hasHelpAction() {
    return getHelpId() != null;
  }

  protected final void doRefactoring(final @NotNull Set<String> excludedIds) {
    final SourceChange change = myRefactoring.getChange();
    if (change == null) {
      PluginLogger.INSTANCE.createLogger(ServerRefactoringDialog.class)
        .warn(myRefactoring.getClass().getSimpleName() + ".getChange() == null\n" + myOptionsStatus);
      return;
    }

    String error = WriteAction.compute(() -> {
      try {
        AssistUtils.applySourceChange(myProject, change, false, excludedIds);
      }
      catch (DartSourceEditException e) {
        return e.getMessage();
      }

      return null;
    });

    if (error == null) {
      close(DialogWrapper.OK_EXIT_CODE);
    }
    else {
      Messages.showErrorDialog(myProject, error, CommonBundle.getErrorTitle());
    }
  }
}
