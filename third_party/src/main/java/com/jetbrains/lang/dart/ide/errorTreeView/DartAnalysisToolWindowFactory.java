// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ExtensionImpl;
import consulo.annotation.component.TopicImpl;
import jakarta.inject.Inject;
import consulo.project.ProjectPropertiesComponent;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.ui.ex.toolWindow.ToolWindow;
import consulo.project.ui.wm.ToolWindowFactory;
import consulo.project.ui.wm.ToolWindowManager;
import consulo.project.ui.wm.ToolWindowManagerListener;
import consulo.ui.ex.content.Content;
import consulo.ui.ex.content.ContentFactory;
import com.jetbrains.lang.dart.DartBundle;
import consulo.localize.LocalizeValue;
import consulo.ui.ex.toolWindow.ToolWindowAnchor;
import consulo.ui.image.Image;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartAnalysisToolWindowFactory implements ToolWindowFactory, DumbAware {
  private static final String TOOL_WINDOW_VISIBLE_PROPERTY = "dart.analysis.tool.window.visible";

  // Consulo: id, anchor, icon and flags from upstream plugin.xml <toolWindow>
  @Override
  public @NotNull String getId() {
    return DartProblemsView.TOOLWINDOW_ID;
  }

  @Override
  public @NotNull ToolWindowAnchor getAnchor() {
    return ToolWindowAnchor.BOTTOM;
  }

  @Override
  public @NotNull Image getIcon() {
    return DartIcons.Dart_13;
  }

  @Override
  public @NotNull LocalizeValue getDisplayName() {
    return DartBundle.messageValue("toolwindow.stripe.Dart_Analysis");
  }

  @Override
  public boolean isDoNotActivateOnStart() {
    return true;
  }

  @Override
  public boolean canCloseContents() {
    return false;
  }

  @Override
  public boolean shouldBeAvailable(@NotNull Project project) {
    return false; // Dart Analysis tool window shouldn't show up in non-Dart projects. Will be made available explicitly if needed.
  }

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    DartProblemsViewPanel panel = new DartProblemsViewPanel(project, DartProblemsView.getInstance(project).getPresentationHelper());
    Content content = ContentFactory.getInstance().createContent(panel, "", false);
    toolWindow.getContentManager().addContent(content);
    toolWindow.setAvailable(true);

    DartProblemsView.getInstance(project).panelCreated(panel);

    if (ProjectPropertiesComponent.getInstance(project).getBoolean(TOOL_WINDOW_VISIBLE_PROPERTY, true)) {
      toolWindow.activate(null, false);
    }
  }

  /**
   * Helps to initialize Dart Analysis tool window with the same visibility state as it was when the project was previously closed.
   * Standard tool window layout serialization in ToolWindowManager doesn't work because Dart Analysis tool window is not available by default.
   */
  @TopicImpl(ComponentScope.PROJECT)
  public static class DartToolWindowManagerListener implements ToolWindowManagerListener {
    private final @NotNull Project myProject;

    @Inject
    public DartToolWindowManagerListener(@NotNull Project project) {
      myProject = project;
    }

    @Override
    public void stateChanged(@NotNull ToolWindowManager toolWindowManager) {
      ToolWindow toolWindow = toolWindowManager.getToolWindow(DartProblemsView.TOOLWINDOW_ID);
      if (toolWindow != null) {
        ProjectPropertiesComponent.getInstance(myProject).setValue(TOOL_WINDOW_VISIBLE_PROPERTY, toolWindow.isVisible(), true);
      }
    }
  }
}
