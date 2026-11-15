// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.execution.executor.Executor;
import consulo.execution.configuration.EmptyRunProfileState;
import consulo.execution.configuration.LegacyRunConfigurationConverter;
import consulo.execution.configuration.RunConfigurationBase;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.configuration.RunConfigurationWithSuppressedDefaultRunAction;
import consulo.project.Project;
import consulo.module.content.ProjectRootManager;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DartRemoteDebugConfiguration extends RunConfigurationBase implements RunConfigurationWithSuppressedDefaultRunAction {
  protected DartRemoteDebugConfiguration(final @NotNull Project project,
                                         final @NotNull DartRemoteDebugConfigurationType configType,
                                         final @NotNull String name) {
    super(project, configType.getConfigurationFactories()[0], name);
  }

  // Consulo: settings are kept in DartRemoteDebugConfigurationOptions and edited by DartRemoteDebugFragment,
  // getParameters() is a snapshot, changes go through setParameters()
  @Override
  protected Class<DartRemoteDebugConfigurationOptions> getOptionsClass() {
    return DartRemoteDebugConfigurationOptions.class;
  }

  @Override
  public DartRemoteDebugConfigurationOptions getOptions() {
    return (DartRemoteDebugConfigurationOptions)super.getOptions();
  }

  @Override
  protected void convertLegacyState(LegacyRunConfigurationConverter converter) {
    super.convertLegacyState(converter);

    DartRemoteDebugParameters old = converter.readXml(DartRemoteDebugParameters.class);
    DartRemoteDebugConfigurationOptions options = new DartRemoteDebugConfigurationOptions();
    options.setDartProjectPath(old.getDartProjectPath());
    converter.setOptions(options);
  }

  @Override
  public List<RunConfigurationFragment<?>> createFragments() {
    return List.of(new DartRemoteDebugFragment());
  }

  public @NotNull DartRemoteDebugParameters getParameters() {
    DartRemoteDebugParameters parameters = new DartRemoteDebugParameters();
    parameters.setDartProjectPath(getOptions().getDartProjectPath());
    return parameters;
  }

  public void setParameters(@NotNull DartRemoteDebugParameters parameters) {
    getOptions().setDartProjectPath(parameters.getDartProjectPath());
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationError {
    final String path = getParameters().getDartProjectPath();
    if (path.isEmpty()) {
      throw new RuntimeConfigurationError(DartBundle.message("dialog.message.dart.project.path.not.specified"));
    }

    final VirtualFile dir = LocalFileSystem.getInstance().findFileByPath(path);
    if (dir == null || !dir.isDirectory()) {
      throw new RuntimeConfigurationError(DartBundle.message("dialog.message.folder.not.found", FileUtil.toSystemDependentName(path)));
    }

    if (!ProjectRootManager.getInstance(getProject()).getFileIndex().isInContent(dir)) {
      throw new RuntimeConfigurationError(
        DartBundle.message("dialog.message.folder.not.in.project.content", FileUtil.toSystemDependentName(path)));
    }
  }

  @Override
  public @Nullable RunProfileState getState(final @NotNull Executor executor,
                                            final @NotNull ExecutionEnvironment environment) {
    return EmptyRunProfileState.INSTANCE;
  }
}
