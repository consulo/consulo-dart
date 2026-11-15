// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.test;

import java.util.List;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.LegacyRunConfigurationConverter;
import consulo.dart.localize.DartConsuloLocalize;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import com.jetbrains.lang.dart.ide.runner.base.DartVmOptionsFragment;
import com.jetbrains.lang.dart.ide.runner.base.DartFileFragment;
import consulo.process.ExecutionException;
import consulo.execution.executor.Executor;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.project.Project;
import consulo.util.io.PathUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartTestRunConfiguration extends DartRunConfigurationBase {
  protected DartTestRunConfiguration(final Project project, final ConfigurationFactory factory, final String name) {
    super(project, factory, name);
  }

  // Consulo: settings are kept in DartTestRunConfigurationOptions and edited by fragments,
  // getRunnerParameters() is a snapshot, changes go through setRunnerParameters()
  @Override
  protected Class<DartTestRunConfigurationOptions> getOptionsClass() {
    return DartTestRunConfigurationOptions.class;
  }

  @Override
  public DartTestRunConfigurationOptions getOptions() {
    return (DartTestRunConfigurationOptions)super.getOptions();
  }

  @Override
  protected void convertLegacyState(LegacyRunConfigurationConverter converter) {
    super.convertLegacyState(converter);

    DartTestRunConfigurationLegacyState old = converter.readXml(DartTestRunConfigurationLegacyState.class);
    DartTestRunConfigurationOptions options = new DartTestRunConfigurationOptions();
    DartTestRunnerParameters.Scope scope = old.getScope();
    options.setScope(scope == null || scope == DartTestRunnerParameters.Scope.FILE ? null : scope.name());
    options.setTestName(old.getTestName());
    options.setTargetName(old.getTargetName());
    options.setTestRunnerOptions(old.getTestRunnerOptions());
    convertLegacyCommandLineState(converter, old, options);
  }

  @Override
  public List<RunConfigurationFragment<?>> createFragments() {
    return List.of(
      new DartTestScopeFragment(),
      new DartFileFragment(DartConsuloLocalize.runConfigurationFragmentTestFile(), DartConsuloLocalize.runConfigurationTestFileLabel()),
      new DartVmOptionsFragment()
    );
  }

  @Override
  public @NotNull DartTestRunnerParameters getRunnerParameters() {
    DartTestRunnerParameters parameters = new DartTestRunnerParameters();
    fillRunnerParameters(parameters);
    parameters.setScope(getScope());
    parameters.setTestName(getTestName());
    parameters.setTargetName(getOptions().getTargetName());
    parameters.setTestRunnerOptions(getTestRunnerOptions());
    return parameters;
  }

  @Override
  public void setRunnerParameters(@NotNull DartCommandLineRunnerParameters parameters) {
    super.setRunnerParameters(parameters);
    if (parameters instanceof DartTestRunnerParameters testParameters) {
      setScope(testParameters.getScope());
      setTestName(testParameters.getTestName());
      getOptions().setTargetName(testParameters.getTargetName());
      setTestRunnerOptions(testParameters.getTestRunnerOptions());
    }
  }

  public @NotNull DartTestRunnerParameters.Scope getScope() {
    String name = getOptions().getScope();
    if (name != null) {
      for (DartTestRunnerParameters.Scope scope : DartTestRunnerParameters.Scope.values()) {
        if (scope.name().equals(name)) {
          return scope;
        }
      }
    }
    return DartTestRunnerParameters.Scope.FILE;
  }

  public void setScope(@NotNull DartTestRunnerParameters.Scope scope) {
    getOptions().setScope(scope == DartTestRunnerParameters.Scope.FILE ? null : scope.name());
  }

  public @Nullable String getTestName() {
    return getOptions().getTestName();
  }

  public void setTestName(@Nullable String testName) {
    getOptions().setTestName(testName);
  }

  public @Nullable String getTestRunnerOptions() {
    return getOptions().getTestRunnerOptions();
  }

  public void setTestRunnerOptions(@Nullable String testRunnerOptions) {
    getOptions().setTestRunnerOptions(testRunnerOptions);
  }

  @Override
  public @Nullable RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
    return new DartTestRunningState(env);
  }

  @Override
  public @Nullable String suggestedName() {
    final String path = getFilePath();
    final String groupOrTestName = getTestName();
    if (path == null) return null;

    final String fileOrDirName = PathUtil.getFileName(path);
    switch (getScope()) {
      case GROUP_OR_TEST_BY_NAME:
        if (groupOrTestName != null) {
          return DartBundle.message("test.0.in.1", groupOrTestName, fileOrDirName);
        }
        // fall through
      case FILE:
        return DartBundle.message("all.tests.in.0", fileOrDirName);
      case FOLDER:
        final String dirName;
        if ("test".equals(fileOrDirName)) {
          final String parentPath = PathUtil.getParentPath(path);
          final String parentDirName = PathUtil.getFileName(parentPath);
          dirName = parentDirName.isEmpty() ? fileOrDirName : parentDirName + "/" + fileOrDirName;
        }
        else {
          dirName = fileOrDirName;
        }
        return DartBundle.message("all.tests.in.0", dirName);
      default:
        return null;
    }
  }

}
