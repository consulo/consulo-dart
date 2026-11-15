// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ActionImpl;
import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.awt.CopyPasteManager;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

@ActionImpl(id = "DartCopyDtdUriAction")
public class DartCopyDtdUriAction extends AnAction implements AnActionWithSyncUpdate {
  public DartCopyDtdUriAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.DartCopyDtdUriAction.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.DartCopyDtdUriAction.description"));
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    Project project = e.getData(Project.KEY);
    e.getPresentation().setEnabled(project != null && DartToolingDaemonService.getInstance(project).getWebSocketReady());
  }

  @RequiredUIAccess
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = e.getData(Project.KEY);
    if (project == null) return;
    String uri = DartToolingDaemonService.getInstance(project).getUri();
    if (uri != null) {
      // Consulo: no CopyPasteManager.copyTextToClipboard
      CopyPasteManager.getInstance().setContents(new StringSelection(uri));
    }
  }
}
