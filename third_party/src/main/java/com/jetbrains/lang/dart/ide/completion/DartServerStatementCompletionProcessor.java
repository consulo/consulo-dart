// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.completion;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.language.Language;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.CommonBundle;
import consulo.language.editor.action.SmartEnterProcessor;
import consulo.codeEditor.Editor;
import consulo.project.Project;
import consulo.language.psi.PsiFile;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.assists.AssistUtils;
import com.jetbrains.lang.dart.assists.DartSourceEditException;
import org.dartlang.analysis.server.protocol.Position;
import org.dartlang.analysis.server.protocol.SourceChange;
import org.dartlang.analysis.server.protocol.SourceEdit;
import org.dartlang.analysis.server.protocol.SourceFileEdit;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartServerStatementCompletionProcessor extends SmartEnterProcessor {
  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }


  @Override
  public boolean process(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile psiFile) {
    final long startTime = System.nanoTime();
    final int offset = editor.getCaretModel().getOffset();
    final DartAnalysisServerService service = DartAnalysisServerService.getInstance(psiFile.getProject());
    service.updateFilesContent();
    SourceChange sourceChange = service.edit_getStatementCompletion(psiFile.getVirtualFile(), offset);
    final boolean handled = sourceChange != null && !isNoop(sourceChange);
    String outcome = sourceChange == null ? "no_result" : "no_change";
    if (handled) {
      try {
        AssistUtils.applySourceChange(project, sourceChange, true);
        // Normal return includes write-preparation refusal; it does not guarantee a document change.
        outcome = "application_completed";
        Position position = sourceChange.getSelection();
        if (position != null) {
          // The position should never be null but it might be if unit tests are flaky.
          editor.getCaretModel().moveToOffset(service.getConvertedOffset(psiFile.getVirtualFile(), position.getOffset()));
        }
      }
      catch (DartSourceEditException e) {
        CommonRefactoringUtil.showErrorHint(project, editor, e.getMessage(), CommonBundle.getErrorTitle(), null);
        outcome = "apply_error";
      }
    }
    return handled;
  }

  private static boolean isNoop(final @NotNull SourceChange sourceChange) {
    for (SourceFileEdit fileEdit : sourceChange.getEdits()) {
      for (SourceEdit edit : fileEdit.getEdits()) {
        if (edit.getLength() != 0 || !edit.getReplacement().isEmpty()) {
          return false;
        }
      }
    }
    return true;
  }
}
