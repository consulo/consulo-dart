/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.ide.refactoring;

import com.intellij.platform.lsp.api.LspServer;
import com.intellij.platform.lsp.api.LspServerManager;
import com.intellij.platform.lsp.api.customization.LspIntentionAction;
import com.intellij.platform.lsp.util.Lsp4jUtilKt;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.lsp.DartLspServerSupportProvider;
import com.jetbrains.lang.dart.sdk.DartSdk;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressManager;
import consulo.codeEditor.Editor;
import consulo.codeEditor.SelectionModel;
import consulo.document.Document;
import consulo.language.editor.refactoring.RefactoringBundle;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionContext;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeActionTriggerKind;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Routes Dart refactoring actions (Extract Method, Extract Local Variable, Inline)
 * to LSP {@code textDocument/codeAction} requests when LSP code actions are enabled.
 */
public final class DartLspRefactoringHelper {
  public static final DartLspRefactoringHelper INSTANCE = new DartLspRefactoringHelper();

  public static final String EXTRACT_METHOD_TITLE = "Extract Method";
  public static final String EXTRACT_LOCAL_VARIABLE_TITLE = "Extract Local Variable";
  public static final String INLINE_LOCAL_VARIABLE_TITLE = "Inline Local Variable";
  public static final String INLINE_METHOD_TITLE = "Inline Method";

  private DartLspRefactoringHelper() {
  }

  public static void performExtractMethod(@NotNull Project project, @Nullable Editor editor, @Nullable PsiFile file) {
    if (editor == null || file == null) return;
    SelectionModel selection = editor.getSelectionModel();
    performRefactoring(
      project,
      editor,
      file,
      selection.getSelectionStart(),
      selection.getSelectionEnd() - selection.getSelectionStart(),
      CodeActionKind.RefactorExtract,
      Set.of(EXTRACT_METHOD_TITLE),
      DartBundle.message("progress.title.extract.method"),
      DartBundle.message("dart.refactoring.extract.method.error")
    );
  }

  public static void performExtractLocalVariable(@NotNull Project project, @Nullable Editor editor, @Nullable PsiFile file) {
    if (editor == null || file == null) return;
    SelectionModel selection = editor.getSelectionModel();
    performRefactoring(
      project,
      editor,
      file,
      selection.getSelectionStart(),
      selection.getSelectionEnd() - selection.getSelectionStart(),
      CodeActionKind.RefactorExtract,
      Set.of(EXTRACT_LOCAL_VARIABLE_TITLE),
      DartBundle.message("progress.title.extract.local.variable"),
      DartBundle.message("dart.refactoring.extract.local.variable.error")
    );
  }

  public static void performInline(@NotNull Project project, @Nullable Editor editor, @Nullable PsiFile file) {
    if (editor == null || file == null) return;
    performRefactoring(
      project,
      editor,
      file,
      editor.getCaretModel().getOffset(),
      0,
      CodeActionKind.RefactorInline,
      Set.of(INLINE_LOCAL_VARIABLE_TITLE, INLINE_METHOD_TITLE),
      RefactoringBundle.message("inline.command"),
      DartBundle.message("dart.refactoring.inline.error")
    );
  }

  public static void performRefactoring(@NotNull Project project,
                                        @NotNull Editor editor,
                                        @NotNull PsiFile file,
                                        int offset,
                                        int length,
                                        @NotNull String codeActionKind,
                                        @NotNull Set<String> targetTitles,
                                        @NotNull String progressTitle,
                                        @NotNull String errorTitle) {
    Iterator<LspServer> servers = LspServerManager.getInstance(project)
      .getServersForProvider(DartLspServerSupportProvider.class)
      .iterator();
    performRefactoring(project, editor, file, offset, length, codeActionKind, targetTitles, progressTitle, errorTitle,
                       servers.hasNext() ? servers.next() : null);
  }

  public static void performRefactoring(@NotNull Project project,
                                        @NotNull Editor editor,
                                        @NotNull PsiFile file,
                                        int offset,
                                        int length,
                                        @NotNull String codeActionKind,
                                        @NotNull Set<String> targetTitles,
                                        @NotNull String progressTitle,
                                        @NotNull String errorTitle,
                                        @Nullable LspServer lspServer) {
    if (DartSdk.getDartSdk(project) == null) return;
    if (!CommonRefactoringUtil.checkReadOnlyStatus(file)) return;
    VirtualFile virtualFile = file.getVirtualFile();
    if (virtualFile == null) return;

    PsiDocumentManager.getInstance(project).commitAllDocuments();

    if (lspServer == null) {
      showCannotRefactorErrorHint(
        project,
        editor,
        errorTitle,
        DartBundle.message("analysis.server.not.running")
      );
      return;
    }

    Document document = editor.getDocument();
    int textLength = document.getTextLength();
    int safeOffset = Math.max(0, Math.min(offset, textLength));
    int safeLength = Math.max(0, Math.min(length, textLength - safeOffset));
    Range range = Lsp4jUtilKt.getLsp4jRange(document, safeOffset, safeLength);
    CodeActionContext context = new CodeActionContext(Collections.emptyList(), List.of(codeActionKind));
    context.setTriggerKind(CodeActionTriggerKind.Invoked);
    CodeActionParams params = new CodeActionParams(lspServer.getDocumentIdentifier(virtualFile), range, context);

    AtomicBoolean serverResponded = new AtomicBoolean(false);
    AtomicReference<LspIntentionAction> matchingIntention = new AtomicReference<>();
    boolean completed = ProgressManager.getInstance().runProcessWithProgressSynchronously(
      () -> {
        List<Either<Command, CodeAction>> results = lspServer.sendRequestSync(it -> it.getTextDocumentService().codeAction(params));
        if (results == null) return;
        serverResponded.set(true);
        CodeAction matchingAction = null;
        for (Either<Command, CodeAction> result : results) {
          CodeAction action = result.getRight();
          if (action != null && action.getDisabled() == null && targetTitles.contains(action.getTitle())) {
            matchingAction = action;
            break;
          }
        }
        if (matchingAction == null) return;
        LspIntentionAction intention = new LspIntentionAction(lspServer, matchingAction);
        ApplicationManager.getApplication().runReadAction(() -> {
          if (intention.isAvailable()) {
            matchingIntention.set(intention);
          }
        });
      },
      LocalizeValue.of(progressTitle),
      true,
      project
    );

    if (!completed) return;

    if (!serverResponded.get()) {
      showCannotRefactorErrorHint(
        project,
        editor,
        errorTitle,
        DartBundle.message("dart.refactoring.lsp.no.response")
      );
      return;
    }

    LspIntentionAction intentionToInvoke = matchingIntention.get();
    if (intentionToInvoke == null) {
      showCannotRefactorErrorHint(project, editor, errorTitle);
      return;
    }

    intentionToInvoke.invoke(virtualFile);
  }

  private static void showCannotRefactorErrorHint(@NotNull Project project, @NotNull Editor editor, @NotNull String errorTitle) {
    showCannotRefactorErrorHint(project, editor, errorTitle, null);
  }

  private static void showCannotRefactorErrorHint(@NotNull Project project,
                                                  @NotNull Editor editor,
                                                  @NotNull String errorTitle,
                                                  @Nullable String reason) {
    String message = RefactoringBundle.getCannotRefactorMessage(reason);
    CommonRefactoringUtil.showErrorHint(project, editor, LocalizeValue.of(message), LocalizeValue.of(errorTitle), null);
  }
}
