// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.application.ApplicationManager;
import consulo.disposer.Disposer;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.project.ui.wm.ToolWindowManager;
import consulo.ui.ex.action.ActionGroup;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.ActionToolbar;
import consulo.ui.ex.awt.SimpleToolWindowPanel;
import consulo.ui.ex.content.Content;
import consulo.ui.ex.content.ContentFactory;
import consulo.ui.ex.toolWindow.ToolWindow;
import consulo.ui.ex.toolWindow.ToolWindowAnchor;
import org.jetbrains.annotations.NotNull;

/**
 * {@code org.jetbrains.builtInWebServer.ConsoleManager} of IntelliJ.
 */
public final class ConsoleManager {
  private ConsoleView console;

  public @NotNull ConsoleView getConsole(@NotNull NetService netService) {
    if (console == null) {
      createConsole(netService);
    }
    return console;
  }

  private void createConsole(final @NotNull NetService netService) {
    TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(netService.getProject());
    netService.configureConsole(consoleBuilder);
    console = consoleBuilder.getConsole();

    ApplicationManager.getApplication().invokeLater(() -> {
      ActionGroup actionGroup = netService.getConsoleToolWindowActions();
      ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("BuiltInServer", actionGroup, false);

      SimpleToolWindowPanel toolWindowPanel = new SimpleToolWindowPanel(false, true);
      toolWindowPanel.setContent(console.getComponent());
      toolWindowPanel.setToolbar(toolbar.getComponent());

      ToolWindow toolWindow = ToolWindowManager.getInstance(netService.getProject())
        .registerToolWindow(netService.getConsoleToolWindowId(), false, ToolWindowAnchor.BOTTOM, netService.getProject(), true);
      toolWindow.setIcon(netService.getConsoleToolWindowIcon());

      Content content = ContentFactory.getInstance().createContent(toolWindowPanel, "", false);
      Disposer.register(content, console);

      toolWindow.getContentManager().addContent(content);
    }, netService.getProject()::isDisposed);
  }
}
