// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.ui.ex.action.ActionUpdateThread;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.project.ui.wm.ToolWindowManager;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReanalyzeDartSourcesAction extends AnAction implements DumbAware {

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  public void actionPerformed(final @NotNull AnActionEvent e) {
    final Project project = e.getProject();
    if (isApplicable(project)) {
      DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
      if (das.isServerProcessActive()) {
        das.analysis_reanalyze();
      }
      else {
        // Ensure the server is properly stopped.
        das.restartServer();
      }
    }

  }

  @Override
  public void update(final @NotNull AnActionEvent e) {
    e.getPresentation().setEnabledAndVisible(isApplicable(e.getProject()));
  }

  private static boolean isApplicable(final @Nullable Project project) {
    return project != null && ToolWindowManager.getInstance(project).getToolWindow(DartProblemsView.TOOLWINDOW_ID) != null;
  }
}
