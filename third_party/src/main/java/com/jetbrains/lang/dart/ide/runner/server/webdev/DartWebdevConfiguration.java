// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server.webdev;

import java.util.List;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.LegacyRunConfigurationConverter;
import consulo.process.ExecutionException;
import consulo.execution.executor.Executor;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.LocatableConfigurationBase;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.util.io.PathUtil;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartWebdevConfiguration extends LocatableConfigurationBase {
  protected DartWebdevConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, @NotNull String name) {
    super(project, factory, name);
  }

  // Consulo: settings are kept in DartWebdevConfigurationOptions and edited by DartWebdevFragment,
  // getParameters() is a snapshot, changes go through setParameters()
  @Override
  protected Class<DartWebdevConfigurationOptions> getOptionsClass() {
    return DartWebdevConfigurationOptions.class;
  }

  @Override
  public DartWebdevConfigurationOptions getOptions() {
    return (DartWebdevConfigurationOptions)super.getOptions();
  }

  @Override
  protected void convertLegacyState(LegacyRunConfigurationConverter converter) {
    super.convertLegacyState(converter);

    DartWebdevParameters old = converter.readXml(DartWebdevParameters.class);
    DartWebdevConfigurationOptions options = new DartWebdevConfigurationOptions();
    options.setHtmlFilePath(old.getHtmlFilePath());
    options.setWebdevPort(old.getWebdevPort());
    converter.setOptions(options);
  }

  @Override
  public List<RunConfigurationFragment<?>> createFragments() {
    return List.of(new DartWebdevFragment());
  }

  public @NotNull DartWebdevParameters getParameters() {
    DartWebdevParameters parameters = new DartWebdevParameters();
    parameters.setHtmlFilePath(getOptions().getHtmlFilePath());
    parameters.setWebdevPort(getOptions().getWebdevPort());
    return parameters;
  }

  public void setParameters(@NotNull DartWebdevParameters parameters) {
    getOptions().setHtmlFilePath(parameters.getHtmlFilePath());
    getOptions().setWebdevPort(parameters.getWebdevPort());
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationError {
    getParameters().check(getProject());
  }

  @Override
  public @Nullable RunProfileState getState(final @NotNull Executor executor,
                                            final @NotNull ExecutionEnvironment env) throws ExecutionException {
    return new DartWebdevRunningState(env);
  }

  @Override
  public @Nullable String suggestedName() {
    // Attempt to compute the relative path to the HTML file, i.e. some "web/index.html"
    // If not successful, return at least the file name, i.e. some "index.html"
    final DartWebdevParameters myParameters = getParameters();
    final String htmlFilePath = myParameters.getHtmlFilePath();
    String htmlFilePathRelativeFromWorkingDir = null;
    try {
      VirtualFile workingDir = myParameters.getWorkingDirectory(getProject());
      htmlFilePathRelativeFromWorkingDir =
        htmlFilePath.startsWith(workingDir.getPath() + "/") ? htmlFilePath.substring(workingDir.getPath().length() + 1) : null;
    }
    catch (RuntimeConfigurationError ignore) {
    }

    if (StringUtil.isNotEmpty(htmlFilePathRelativeFromWorkingDir)) {
      return htmlFilePathRelativeFromWorkingDir;
    }

    return StringUtil.isEmpty(htmlFilePath) ? DartBundle.message("runner.web.app.configuration.name") : PathUtil.getFileName(htmlFilePath);
  }

}
