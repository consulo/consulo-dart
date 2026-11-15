// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.platform.base.icon.PlatformIconGroup;
import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ActionImpl;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.project.ui.wm.ToolWindowManager;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ActionImpl(id = "Dart.Reanalyze")
public class ReanalyzeDartSourcesAction extends AnAction implements DumbAware, AnActionWithSyncUpdate {
  public ReanalyzeDartSourcesAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Dart.Reanalyze.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.Dart.Reanalyze.description"));
    getTemplatePresentation().setIcon(PlatformIconGroup.actionsRestart());
  }



  @Override
  public void actionPerformed(final @NotNull AnActionEvent e) {
    final Project project = e.getData(Project.KEY);
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
    e.getPresentation().setEnabledAndVisible(isApplicable(e.getData(Project.KEY)));
  }

  private static boolean isApplicable(final @Nullable Project project) {
    return project != null && ToolWindowManager.getInstance(project).getToolWindow(DartProblemsView.TOOLWINDOW_ID) != null;
  }
}
