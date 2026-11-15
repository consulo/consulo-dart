// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server.webdev;

import consulo.execution.configuration.CommandLineState;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.ide.impl.idea.execution.filters.TextConsoleBuilderImpl;
import consulo.ide.impl.idea.execution.impl.ConsoleViewImpl;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import consulo.language.psi.scope.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

/**
 * A console view that filters out JSON messages sent in daemon mode.
 */
public class DartWebdevConsoleView extends ConsoleViewImpl {

  public static void install(@NotNull Project project, @NotNull CommandLineState launcher) {
    // Create our own console builder.
    //
    // We need to filter input to this console without affecting other consoles, so we cannot use
    // a consoleFilterInputProvider.
    final TextConsoleBuilder builder = new TextConsoleBuilderImpl(project, GlobalSearchScope.allScope(project)) {
      @Override
      protected @NotNull ConsoleView createConsole() {
        return new DartWebdevConsoleView(project, getScope());
      }
    };

    // Set up basic console filters. (More may be added later.)
    launcher.setConsoleBuilder(builder);
  }

  public DartWebdevConsoleView(final @NotNull Project project, final @NotNull GlobalSearchScope searchScope) {
    super(project, searchScope, true, false);
  }

  @Override
  public void print(@NotNull String text, @NotNull ConsoleViewContentType contentType) {
    if (!text.startsWith("[{")) {
      super.print(text, contentType);
    }
    final String logMessage = DartDaemonParserUtil.getLogMessage(text.trim());
    if (StringUtil.isNotEmpty(logMessage)) {
      super.print(logMessage + "\n", contentType);
    }
  }
}
