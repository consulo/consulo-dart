// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.platform.base.icon.PlatformIconGroup;
import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ActionImpl;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.project.Project;
import consulo.project.ui.wm.ToolWindowManager;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ActionImpl(id = "Dart.Restart.Analysis.Server")
public class RestartDartAnalysisServerAction extends DumbAwareAction implements AnActionWithSyncUpdate {
  public RestartDartAnalysisServerAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Dart.Restart.Analysis.Server.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.Dart.Restart.Analysis.Server.description"));
    getTemplatePresentation().setIcon(PlatformIconGroup.actionsForcerefresh());
  }



  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Project project = e.getData(Project.KEY);
    if (project != null) {
      DartAnalysisServerService.getInstance(project).restartServer();
      // The list of projects was probably lost when the server crashed. Prime it with the current project to get the server restarted.
      DartAnalysisServerService.getInstance(project).serverReadyForRequest();

    }
  }

  @Override
  public void update(final @NotNull AnActionEvent e) {
    e.getPresentation().setEnabledAndVisible(isApplicable(e.getData(Project.KEY)));
  }

  private static boolean isApplicable(final @Nullable Project project) {
    return project != null && ToolWindowManager.getInstance(project).getToolWindow(DartProblemsView.TOOLWINDOW_ID) != null;
  }
}
