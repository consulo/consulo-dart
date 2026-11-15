// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.pubServer;

import consulo.ui.ex.action.ActionUpdateThread;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

public class StopDartWebdevServerAction extends DumbAwareAction {
  @Override
  public void update(final @NotNull AnActionEvent e) {
    e.getPresentation().setEnabled(e.getProject() != null && PubServerManager.getInstance(e.getProject()).hasAlivePubServerProcesses());
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public void actionPerformed(final @NotNull AnActionEvent e) {
    if (e.getProject() != null) {
      PubServerManager.getInstance(e.getProject()).stopAllPubServerProcesses();
    }

  }
}
