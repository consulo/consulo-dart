// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.refactoring.moveFile;

import consulo.application.ApplicationManager;
import consulo.project.Project;
import consulo.ui.ex.awt.Messages;

import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.editor.refactoring.move.MoveFileHandler;
import consulo.usage.UsageInfo;
import consulo.language.util.IncorrectOperationException;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.assists.AssistUtils;
import com.jetbrains.lang.dart.assists.DartSourceEditException;
import com.jetbrains.lang.dart.ide.refactoring.DartLspRenamePsiElementProcessor;
import com.jetbrains.lang.dart.ide.refactoring.status.RefactoringStatus;
import com.jetbrains.lang.dart.lsp.DartLspService;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.sdk.DartSdk;
import org.dartlang.analysis.server.protocol.SourceChange;
import org.eclipse.lsp4j.FileRename;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import consulo.logging.Logger;
import com.jetbrains.lang.dart.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import consulo.component.ProcessCanceledException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public final class DartServerMoveDartFileHandler extends MoveFileHandler {

  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartServerMoveDartFileHandler.class);

  @Override
  public boolean canProcessElement(PsiFile psiFile) {
    if (!(psiFile instanceof DartFile)) {
      return false;
    }
    final Project project = psiFile.getProject();
    if (DartSdk.getDartSdk(project) == null) {
      return false;
    }
    return DartAnalysisServerService.getInstance(project).isInIncludedRoots(psiFile.getVirtualFile());
  }

  @Override
  public void prepareMovedFile(PsiFile psiFile, PsiDirectory moveDestination, Map<PsiElement, PsiElement> oldToNewMap) {
    final Project project = psiFile.getProject();
    final VirtualFile virtualFile = psiFile.getVirtualFile();

    final String newFilePath = moveDestination.getVirtualFile().getPath() + "/" + virtualFile.getName();

    if (DartConfigurable.isExperimentalLspFeaturesEnabled(project)) {
      // Platform LSP does not currently support workspace/willRenameFiles (IJPL-249183).
      // If JetBrains implements workspace.fileOperations in the platform, this can be retired.
      DartAnalysisServerService.getInstance(project).updateFilesContent();
      final String oldUri = DartLspRenamePsiElementProcessor.getFileUri(virtualFile.getPath());
      final String newUri = DartLspRenamePsiElementProcessor.getFileUri(newFilePath);
      LOG.info("prepareMovedFile via LSP: oldUri=" + oldUri + ", newUri=" + newUri);
      final RenameFilesParams params = new RenameFilesParams(List.of(new FileRename(oldUri, newUri)));
      try {
        final WorkspaceEdit workspaceEdit = DartLspRenamePsiElementProcessor.awaitFutureCheckingCanceled(
            DartLspService.willRenameFiles(project, params), 10);
        LOG.debug("willRenameFiles response: " + workspaceEdit);
        if (workspaceEdit != null) {
          DartLspRenamePsiElementProcessor.applyWorkspaceEdit(project, workspaceEdit);
        }
      }
      catch (ProcessCanceledException e) {
        LOG.info("willRenameFiles for move was canceled");
        throw e;
      }
      catch (TimeoutException e) {
        LOG.warn("Timeout waiting for willRenameFiles for Dart move", e);
      }
      catch (Exception e) {
        LOG.error("Error executing willRenameFiles for Dart move", e);
      }
      return;
    }

    final MoveFileRefactoring refactoring = new MoveFileRefactoring(project, virtualFile, newFilePath);

    // Validate initial status.
    {
      final RefactoringStatus initialStatus = refactoring.checkInitialConditions();
      if (initialStatus == null) {
        return;
      }

      if (initialStatus.hasError()) {
        final String message = initialStatus.getMessage();
        assert message != null;
        return;
      }

      // validate final status
      {
        final RefactoringStatus finalConditions = refactoring.checkFinalConditions();
        if (finalConditions != null && finalConditions.isOK()) {
          SourceChange change = refactoring.getChange();
          assert change != null;
          try {
            AssistUtils.applySourceChange(project, change, false);
          }
          catch (DartSourceEditException exception) {
            // Exception when trying to apply the creating the refactoring
            showMoveFileExceptionDialog(project, DartBundle.message("dart.refactoring.move.file.dialog.error.applying.change"));
          }
        }
        else {
          // Exception when creating the refactoring
          String message = StringUtil.notNullize(finalConditions == null ? null : finalConditions.getMessage());
          showMoveFileExceptionDialog(project, DartBundle.message("dart.refactoring.move.file.dialog.error.computing.change", message));
        }
      }
    }
  }

  @Override
  public @Nullable @Unmodifiable List<UsageInfo> findUsages(@NotNull PsiFile psiFile, @NotNull PsiDirectory newParent, boolean searchInComments, boolean searchInNonJavaFiles) {
    return null;
  }

  @Override
  public void retargetUsages(@Unmodifiable @NotNull List<? extends UsageInfo> usageInfos, @NotNull Map<PsiElement, PsiElement> oldToNewMap) {
  }

  @Override
  public void updateMovedFile(PsiFile psiFile) throws IncorrectOperationException {
    // This is not used as the file move has already happened in the framework by this point meaning that the file changes described by
    // the Dart Analysis Server would now be pointing to incorrect file paths if we tried to use them at this point.
  }

  private static void showMoveFileExceptionDialog(final @NotNull Project project, @NotNull String message) {
    ApplicationManager.getApplication()
      .invokeLater(() -> Messages.showErrorDialog(project, message, DartBundle.message("dart.refactoring.move.file.dialog.title")));
  }
}
