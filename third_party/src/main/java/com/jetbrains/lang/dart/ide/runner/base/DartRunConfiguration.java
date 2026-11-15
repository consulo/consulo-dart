package com.jetbrains.lang.dart.ide.runner.base;

import consulo.execution.configuration.RunConfiguration;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import org.jetbrains.annotations.NotNull;

public interface DartRunConfiguration extends RunConfiguration {

  @NotNull
  DartCommandLineRunnerParameters getRunnerParameters();
}
