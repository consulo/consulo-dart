// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.actions;

import consulo.dart.compat.ReadonlyStatusHandlerCompat;
import consulo.application.CommonBundle;
import consulo.application.ApplicationManager;

import consulo.undoRedo.CommandProcessor;
import consulo.logging.Logger;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.document.Document;
import consulo.codeEditor.Editor;
import consulo.document.FileDocumentManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.project.Project;
import consulo.ui.ex.awt.Messages;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.ReadonlyStatusHandler;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.assists.AssistUtils;
import org.dartlang.analysis.server.protocol.SourceEdit;
import org.dartlang.analysis.server.protocol.SourceFileEdit;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DartSortMembersAction extends AbstractDartFileProcessingAction {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartSortMembersAction.class);

  @Override
  protected @NotNull String getActionTextForEditor() {
    return DartBundle.message("action.Dart.DartSortMembers.text");
  }

  @Override
  protected @NotNull String getActionTextForFiles() {
    return DartBundle.message("dart.sort.members.action.name.ellipsis"); // because with dialog
  }

  @Override
  protected void runOverEditor(final @NotNull Project project, final @NotNull Editor editor, final @NotNull PsiFile psiFile) {
    final Document document = editor.getDocument();
    if (!ReadonlyStatusHandlerCompat.ensureDocumentWritable(project, document)) return;

    final String path = psiFile.getVirtualFile().getPath();

    final DartAnalysisServerService service = DartAnalysisServerService.getInstance(project);
    service.updateFilesContent();
    final SourceFileEdit fileEdit = service.edit_sortMembers(path);

    if (fileEdit == null) {
      showHintLater(editor, DartBundle.message("dart.sort.members.hint.failed"), true);
      LOG.warn("Unexpected response from edit_sortMembers, fileEdit is null");
      return;
    }

    final List<SourceEdit> edits = fileEdit.getEdits();
    if (edits == null || edits.isEmpty()) {
      showHintLater(editor, DartBundle.message("dart.sort.members.hint.already.good"), false);
      return;
    }

    final Runnable runnable = () -> {
      AssistUtils.applySourceEdits(project, psiFile.getVirtualFile(), document, edits, Collections.emptySet());
      showHintLater(editor, DartBundle.message("dart.sort.members.hint.success"), false);
    };

    ApplicationManager.getApplication().runWriteAction(
      () -> CommandProcessor.getInstance().executeCommand(project, runnable, DartBundle.message("action.Dart.DartSortMembers.text"), null));
  }

  @Override
  protected void runOverFiles(final @NotNull Project project, final @NotNull List<VirtualFile> dartFiles) {
    if (dartFiles.isEmpty()) {
      Messages.showInfoMessage(project, DartBundle.message("dart.sort.members.files.no.dart.files"),
                               DartBundle.message("action.Dart.DartSortMembers.text"));
      return;
    }

    if (Messages.showOkCancelDialog(project, DartBundle.message("dart.sort.members.files.dialog.question", dartFiles.size()),
                                    DartBundle.message("action.Dart.DartSortMembers.text"), CommonBundle.getYesButtonText(),
                                    CommonBundle.getNoButtonText(), null) != Messages.OK) {
      return;
    }

    final Map<VirtualFile, SourceFileEdit> fileToFileEditMap = new HashMap<>();

    final Runnable runnable = () -> {
      double fraction = 0.0;
      for (final VirtualFile virtualFile : dartFiles) {
        fraction += 1.0;
        final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        if (indicator != null) {
          indicator.checkCanceled();
          indicator.setText2(FileUtil.toSystemDependentName(virtualFile.getPath()));
          if (dartFiles.size() > 1) {
            indicator.setIndeterminate(false);
            indicator.setFraction(fraction / dartFiles.size());
          }
        }

        final String path = virtualFile.getPath();
        final SourceFileEdit fileEdit = DartAnalysisServerService.getInstance(project).edit_sortMembers(path);
        if (fileEdit != null) {
          fileToFileEditMap.put(virtualFile, fileEdit);
        }
      }
    };

    DartAnalysisServerService.getInstance(project).updateFilesContent();

    final boolean ok = ProgressManager.getInstance()
      .runProcessWithProgressSynchronously(runnable, DartBundle.message("action.Dart.DartSortMembers.progress.title"), true, project);

    if (ok) {
      final Runnable onSuccessRunnable = () -> {
        for (Map.Entry<VirtualFile, SourceFileEdit> entry : fileToFileEditMap.entrySet()) {
          final VirtualFile file = entry.getKey();
          final Document document = FileDocumentManager.getInstance().getDocument(file);
          final SourceFileEdit fileEdit = entry.getValue();
          if (document != null) {
            AssistUtils.applySourceEdits(project, file, document, fileEdit.getEdits(), Collections.emptySet());
          }
        }
      };

      ApplicationManager.getApplication().runWriteAction(() -> CommandProcessor.getInstance()
        .executeCommand(project, onSuccessRunnable, DartBundle.message("action.Dart.DartSortMembers.text"), null));
    }
  }
}
