// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.runner.base.DartRunConfigurationBase;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfiguration;
import com.jetbrains.lang.dart.ide.runner.server.DartRemoteDebugConfiguration;
import com.jetbrains.lang.dart.ide.runner.server.vmService.DartVmServiceDebugProcess;
import com.jetbrains.lang.dart.ide.runner.server.vmService.DartVmServiceDebugProcess.DebugType;
import com.jetbrains.lang.dart.ide.runner.server.webdev.DartWebdevConfiguration;
import com.jetbrains.lang.dart.ide.runner.test.DartTestRunConfiguration;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.util.DartUrlResolver;
import consulo.annotation.component.ExtensionImpl;
import consulo.document.FileDocumentManager;
import consulo.execution.ExecutionResult;
import consulo.execution.configuration.RunProfile;
import consulo.execution.configuration.RunProfileState;
import consulo.execution.configuration.RunnerSettings;
import consulo.execution.configuration.RuntimeConfigurationError;
import consulo.execution.debug.DefaultDebugExecutor;
import consulo.execution.debug.XDebugProcess;
import consulo.execution.debug.XDebugProcessStarter;
import consulo.execution.debug.XDebugSession;
import consulo.execution.debug.XDebuggerManager;
import consulo.execution.runner.ExecutionEnvironment;
import consulo.execution.runner.GenericProgramRunner;
import consulo.execution.ui.RunContentDescriptor;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.project.Project;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Consulo: upstream starts the session through the {@code XDebuggerManager.newSessionBuilder} API via reflection when it exists;
 * Consulo has no such API, so {@link XDebuggerManager#startSession} is used directly.
 */
@ExtensionImpl
public final class DartRunner extends GenericProgramRunner<RunnerSettings> {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartRunner.class);
  private static final int VM_SERVICE_TIMEOUT_IN_MS = 5000;

  @Override
  public @NotNull String getRunnerId() {
    return "DartRunner";
  }

  @Override
  public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
    return executorId.equals(DefaultDebugExecutor.EXECUTOR_ID) && (
      profile instanceof DartCommandLineRunConfiguration ||
      profile instanceof DartTestRunConfiguration ||
      profile instanceof DartRemoteDebugConfiguration ||
      profile instanceof DartWebdevConfiguration
    );
  }

  @Override
  protected @Nullable RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment environment)
    throws ExecutionException {
    String executorId = environment.getExecutor().getId();
    if (!executorId.equals(DefaultDebugExecutor.EXECUTOR_ID)) {
      LOG.error("Unexpected executor id: " + executorId);
      return null;
    }

    try {
      RunProfile runConfig = environment.getRunProfile();
      Project project = environment.getProject();
      DartAnalysisServerService analysisServer = DartAnalysisServerService.getInstance(project);
      String dasExecutionContextId;
      if (runConfig instanceof DartRunConfigurationBase runConfigBase && analysisServer.serverReadyForRequest()) {
        String path = Objects.requireNonNull(runConfigBase.getRunnerParameters().getFilePath());
        dasExecutionContextId = analysisServer.execution_createContext(path);
      }
      else {
        // No context ID if remote debugging or can't start DAS.
        dasExecutionContextId = null;
      }

      return doExecuteDartDebug(state, environment, dasExecutionContextId);
    }
    catch (RuntimeConfigurationError e) {
      throw new ExecutionException(e);
    }
  }

  private @Nullable RunContentDescriptor doExecuteDartDebug(@NotNull RunProfileState state,
                                                            @NotNull ExecutionEnvironment env,
                                                            @Nullable String dasExecutionContextId)
    throws RuntimeConfigurationError, ExecutionException {
    RunProfile runConfiguration = env.getRunProfile();
    Project project = env.getProject();

    ExecutionInfo executionInfo;
    if (runConfiguration instanceof DartRunConfigurationBase runConfigBase) {
      VirtualFile contextFileOrDir = runConfigBase.getRunnerParameters().getDartFileOrDirectory();
      String cwd = runConfigBase.getRunnerParameters().computeProcessWorkingDirectory(project);
      VirtualFile currentWorkingDirectory = LocalFileSystem.getInstance().findFileByPath(cwd);
      ExecutionResult executionResult = state.execute(env.getExecutor(), this);
      if (executionResult == null) return null;
      executionInfo = new ExecutionInfo(contextFileOrDir, currentWorkingDirectory, executionResult, DebugType.CLI);
    }
    else if (runConfiguration instanceof DartRemoteDebugConfiguration remoteDebugConfiguration) {
      String path = remoteDebugConfiguration.getParameters().getDartProjectPath();
      VirtualFile contextDirectory = LocalFileSystem.getInstance().findFileByPath(path);
      if (contextDirectory == null) {
        throw new RuntimeConfigurationError(DartBundle.message("dialog.message.folder.not.found", FileUtil.toSystemDependentName(path)));
      }
      executionInfo = new ExecutionInfo(contextDirectory, contextDirectory, null, DebugType.REMOTE);
    }
    else if (runConfiguration instanceof DartWebdevConfiguration webdevConfiguration) {
      VirtualFile contextFile = webdevConfiguration.getParameters().getHtmlFile();
      VirtualFile currentWorkingDirectory = webdevConfiguration.getParameters().getWorkingDirectory(project);
      ExecutionResult executionResult = state.execute(env.getExecutor(), this);
      if (executionResult == null) return null;
      executionInfo = new ExecutionInfo(contextFile, currentWorkingDirectory, executionResult, DebugType.WEBDEV);
    }
    else {
      LOG.error("Unexpected run configuration: " + runConfiguration.getName());
      return null;
    }

    FileDocumentManager.getInstance().saveAllDocuments();

    XDebugProcessStarter starter = new XDebugProcessStarter() {
      @Override
      public @NotNull XDebugProcess start(@NotNull XDebugSession session) {
        DartUrlResolver dartUrlResolver = DartUrlResolver.getInstance(project, executionInfo.contextFileOrDir());
        DartVmServiceDebugProcess dartVmServiceDebugProcess = new DartVmServiceDebugProcess(
          session,
          executionInfo.executionResult(),
          dartUrlResolver,
          dasExecutionContextId,
          executionInfo.debugType(),
          VM_SERVICE_TIMEOUT_IN_MS,
          executionInfo.currentWorkingDirectory()
        );

        dartVmServiceDebugProcess.start();
        return dartVmServiceDebugProcess;
      }
    };

    XDebugSession debugSession = XDebuggerManager.getInstance(project).startSession(env, starter);
    return debugSession.getRunContentDescriptor();
  }

  private record ExecutionInfo(@NotNull VirtualFile contextFileOrDir,
                               @Nullable VirtualFile currentWorkingDirectory,
                               @Nullable ExecutionResult executionResult,
                               @NotNull DebugType debugType) {
  }
}
