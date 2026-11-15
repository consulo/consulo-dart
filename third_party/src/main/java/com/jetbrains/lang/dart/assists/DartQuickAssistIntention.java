// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.assists;

import consulo.localize.LocalizeValue;
import consulo.application.CommonBundle;
import consulo.language.editor.inspection.FileModifier;
import consulo.language.editor.intention.IntentionAction;
import consulo.application.ApplicationManager;
import consulo.codeEditor.Editor;
import consulo.project.Project;

import consulo.language.psi.PsiFile;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import consulo.language.util.IncorrectOperationException;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.fixes.DartQuickFix;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import org.dartlang.analysis.server.protocol.SourceChange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DartQuickAssistIntention implements IntentionAction, Comparable<IntentionAction> {
  private final int myIndex;
  private @Nullable SourceChange sourceChange;

  public DartQuickAssistIntention(int index) {
    myIndex = index;
  }

  @Override
  public int compareTo(final @NotNull IntentionAction o) {
    if (o instanceof DartQuickAssistIntention other) {
      return myIndex - other.myIndex;
    }
    return 0;
  }

  public @NotNull String getFamilyName() {
    return DartBundle.message("dart.quick.assist.family.name");
  }

  @Override
  public @NotNull LocalizeValue getText() {
    if (sourceChange == null) return LocalizeValue.empty();

    String message = sourceChange.getMessage();
    return LocalizeValue.of(message);
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, PsiFile psiFile) throws IncorrectOperationException {
    if (editor == null || psiFile == null) {
      // not sure this can ever happen
      return;
    }

    if (sourceChange != null) {
      if (!psiFile.isPhysical() && !ApplicationManager.getApplication().isWriteAccessAllowed()) {
        DartQuickFix.doInvokeForPreview(psiFile, sourceChange);
        return;
      }

      DartAnalysisServerService.getInstance(project).fireBeforeQuickAssistIntentionInvoked(this, editor, psiFile);
      try {
        AssistUtils.applySourceChange(project, sourceChange, true);
      }
      catch (DartSourceEditException e) {
        CommonRefactoringUtil.showErrorHint(project, editor, e.getMessage(), CommonBundle.getErrorTitle(), null);
      }
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile psiFile) {
    if (editor == null || psiFile == null) {
      // not sure this can ever happen
      return false;
    }

    // Consulo: no LSP

    if (!(psiFile instanceof DartFile) || !DartAnalysisServerService.isLocalAnalyzableFile(psiFile.getVirtualFile())) {
      return false;
    }

    final List<SourceChange> sourceChanges = DartQuickAssistSet.getInstance().getQuickAssists(editor, psiFile);
    if (sourceChanges.size() <= myIndex) {
      sourceChange = null;
      return false;
    }
    sourceChange = sourceChanges.get(myIndex);
    return true;
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  @Override
  public @Nullable FileModifier getFileModifierForPreview(@NotNull PsiFile target) {
    return DartQuickFix.isPreviewAvailable(target, sourceChange) ? this : null;
  }

  //@formatter:off
  public static final class DartQuickAssistIntention0  extends DartQuickAssistIntention{ public DartQuickAssistIntention0()  {super(0); } }
  public static final class DartQuickAssistIntention1  extends DartQuickAssistIntention{ public DartQuickAssistIntention1()  {super(1); } }
  public static final class DartQuickAssistIntention2  extends DartQuickAssistIntention{ public DartQuickAssistIntention2()  {super(2); } }
  public static final class DartQuickAssistIntention3  extends DartQuickAssistIntention{ public DartQuickAssistIntention3()  {super(3); } }
  public static final class DartQuickAssistIntention4  extends DartQuickAssistIntention{ public DartQuickAssistIntention4()  {super(4); } }
  public static final class DartQuickAssistIntention5  extends DartQuickAssistIntention{ public DartQuickAssistIntention5()  {super(5); } }
  public static final class DartQuickAssistIntention6  extends DartQuickAssistIntention{ public DartQuickAssistIntention6()  {super(6); } }
  public static final class DartQuickAssistIntention7  extends DartQuickAssistIntention{ public DartQuickAssistIntention7()  {super(7); } }
  public static final class DartQuickAssistIntention8  extends DartQuickAssistIntention{ public DartQuickAssistIntention8()  {super(8); } }
  public static final class DartQuickAssistIntention9  extends DartQuickAssistIntention{ public DartQuickAssistIntention9()  {super(9); } }
  public static final class DartQuickAssistIntention10 extends DartQuickAssistIntention{ public DartQuickAssistIntention10() {super(10);} }
  public static final class DartQuickAssistIntention11 extends DartQuickAssistIntention{ public DartQuickAssistIntention11() {super(11);} }
  public static final class DartQuickAssistIntention12 extends DartQuickAssistIntention{ public DartQuickAssistIntention12() {super(12);} }
  public static final class DartQuickAssistIntention13 extends DartQuickAssistIntention{ public DartQuickAssistIntention13() {super(13);} }
  public static final class DartQuickAssistIntention14 extends DartQuickAssistIntention{ public DartQuickAssistIntention14() {super(14);} }
  //@formatter:on
}
