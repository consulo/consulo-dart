// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.pubServer;

import consulo.platform.base.icon.PlatformIconGroup;
import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ActionImpl;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.project.Project;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

@ActionImpl(id = "Dart.stop.dart.webdev.server")
public class StopDartWebdevServerAction extends DumbAwareAction implements AnActionWithSyncUpdate {
  public StopDartWebdevServerAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Dart.stop.dart.webdev.server.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.Dart.stop.dart.webdev.server.description"));
    getTemplatePresentation().setIcon(PlatformIconGroup.actionsSuspend());
  }

  @Override
  public void update(final @NotNull AnActionEvent e) {
    e.getPresentation().setEnabled(e.getData(Project.KEY) != null && PubServerManager.getInstance(e.getData(Project.KEY)).hasAlivePubServerProcesses());
  }


  @Override
  public void actionPerformed(final @NotNull AnActionEvent e) {
    if (e.getData(Project.KEY) != null) {
      PubServerManager.getInstance(e.getData(Project.KEY)).stopAllPubServerProcesses();
    }

  }
}
