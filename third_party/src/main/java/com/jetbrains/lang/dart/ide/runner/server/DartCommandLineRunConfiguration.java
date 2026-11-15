// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.process.ExecutionException;
import consulo.execution.executor.Executor;
import consulo.execution.configuration.RunConfiguration;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.server.ui.DartCommandLineConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartCommandLineRunConfiguration extends DartRunConfigurationBase {
  private @NotNull DartCommandLineRunnerParameters myRunnerParameters = new DartCommandLineRunnerParameters();

  public DartCommandLineRunConfiguration(String name, Project project, DartCommandLineRunConfigurationType configurationType) {
    super(project, configurationType.getConfigurationFactories()[0], name);
  }

  @Override
  public @NotNull DartCommandLineRunnerParameters getRunnerParameters() {
    return myRunnerParameters;
  }

  @Override
  public @NotNull SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new DartCommandLineConfigurationEditorForm(getProject());
  }

  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    return new DartCommandLineRunningState(env);
  }

  @Override
  public @Nullable String suggestedName() {
    final String filePath = myRunnerParameters.getFilePath();
    return filePath == null ? null : PathUtil.getFileName(filePath);
  }

  @Override
  public DartCommandLineRunConfiguration clone() {
    final DartCommandLineRunConfiguration clone = (DartCommandLineRunConfiguration)super.clone();
      try {
          clone.myRunnerParameters = myRunnerParameters.clone();
      } catch (CloneNotSupportedException e) {
          throw new RuntimeException(e);
      }
      return clone;
  }
}
