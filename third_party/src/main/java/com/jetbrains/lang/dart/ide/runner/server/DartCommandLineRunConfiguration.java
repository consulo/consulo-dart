// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.lang.dart.ide.runner.server;

import com.jetbrains.lang.dart.ide.runner.base.DartFileFragment;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.base.DartVmOptionsFragment;
import consulo.dart.localize.DartConsuloLocalize;
import consulo.execution.configuration.LegacyRunConfigurationConverter;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.executor.Executor;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Consulo: settings are kept in {@link DartCommandLineRunConfigurationOptions} and edited by fragments,
 * {@link #getRunnerParameters()} is a snapshot, changes go through {@link #setRunnerParameters}.
 */
public class DartCommandLineRunConfiguration extends DartRunConfigurationBase {
  public DartCommandLineRunConfiguration(String name, Project project, DartCommandLineRunConfigurationType configurationType) {
    super(project, configurationType.getConfigurationFactories()[0], name);
  }

  @Override
  protected Class<DartCommandLineRunConfigurationOptions> getOptionsClass() {
    return DartCommandLineRunConfigurationOptions.class;
  }

  @Override
  protected void convertLegacyState(LegacyRunConfigurationConverter converter) {
    super.convertLegacyState(converter);

    DartCommandLineRunConfigurationLegacyState old = converter.readXml(DartCommandLineRunConfigurationLegacyState.class);
    convertLegacyCommandLineState(converter, old, new DartCommandLineRunConfigurationOptions());
  }

  @Override
  public List<RunConfigurationFragment<?>> createFragments() {
    return List.of(
      new DartFileFragment(DartConsuloLocalize.runConfigurationFragmentDartFile(), DartConsuloLocalize.runConfigurationDartFileLabel()),
      new DartVmOptionsFragment()
    );
  }

  @Override
  public @NotNull DartCommandLineRunnerParameters getRunnerParameters() {
    DartCommandLineRunnerParameters parameters = new DartCommandLineRunnerParameters();
    fillRunnerParameters(parameters);
    return parameters;
  }

  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    return new DartCommandLineRunningState(env);
  }

  @Override
  public @Nullable String suggestedName() {
    final String filePath = getFilePath();
    return filePath == null ? null : PathUtil.getFileName(filePath);
  }
}
