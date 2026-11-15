package com.jetbrains.lang.dart.ide.runner;

import consulo.execution.ui.console.ConsoleFilterProvider;
import consulo.execution.ui.console.Filter;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;

public final class DartConsoleFilterProvider implements ConsoleFilterProvider {
  @Override
  public Filter @NotNull [] getDefaultFilters(final @NotNull Project project) {
    return new Filter[]{new DartConsoleFilter(project)};
  }
}
