// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.test;

import consulo.execution.executor.Executor;
import consulo.execution.configuration.RunConfigurationBase;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.test.action.AbstractRerunFailedTestsAction;
import consulo.ui.ex.action.ActionUpdateThread;
import consulo.module.Module;
import consulo.ui.ex.ComponentContainer;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfiguration;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import org.jetbrains.annotations.NotNull;

class DartTestRerunnerAction extends AbstractRerunFailedTestsAction {

  DartTestRerunnerAction(@NotNull ComponentContainer componentContainer) {
    super(componentContainer);
  }

  @Override
  public @NotNull ActionUpdateThread getActionUpdateThread() {
    return ActionUpdateThread.BGT;
  }

  @Override
  protected MyRunProfile getRunProfile(@NotNull ExecutionEnvironment environment) {
    final RunConfigurationBase configuration = (RunConfigurationBase)myConsoleProperties.getConfiguration();
    final DartTestRerunner runner = new DartTestRerunner(environment, getFailedTests(configuration.getProject()));
    return new RerunProfile(configuration, runner);
  }

  private static final class RerunProfile extends MyRunProfile implements DartRunConfiguration {
    private final DartTestRerunner runner;

    private RerunProfile(RunConfigurationBase configuration, DartTestRerunner runner) {
      super(configuration);
      this.runner = runner;
    }

    @Override
    public @NotNull DartCommandLineRunnerParameters getRunnerParameters() {
      return ((DartRunConfiguration)runner.getEnvironment().getRunProfile()).getRunnerParameters();
    }

    @Override
    public Module @NotNull [] getModules() {
      return runner.getModulesToCompile();
    }

    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) {
      return runner;
    }
  }
}
