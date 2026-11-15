// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import consulo.project.Project;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.awt.CopyPasteManager;
import org.jetbrains.annotations.NotNull;

import java.awt.datatransfer.StringSelection;

public class DartCopyDtdUriAction extends AnAction implements AnActionWithSyncUpdate {
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
