// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.devtools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkUtil;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.disposer.Disposable;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;

@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartDevToolsService implements Disposable {
  private static final String MIN_SDK_VERSION = "2.15";
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DartDevToolsService.class);

  private final @NotNull Project myProject;

  private @Nullable ProcessHandler processHandler;
  private volatile boolean serviceRunning = false;

  private volatile @Nullable String devToolsHostAndPort;

  @Inject
  public DartDevToolsService(@NotNull Project project) {
    myProject = project;
  }

  public static @NotNull DartDevToolsService getInstance(@NotNull Project project) {
    return project.getInstance(DartDevToolsService.class);
  }

  public @Nullable String getDevToolsHostAndPort() {
    return devToolsHostAndPort;
  }

  public void startService() {
    startService(null);
  }

  public void startService(@Nullable String dtdUri) {
    if (serviceRunning) {
      logger.error("Ignoring startService() call. Dart Tooling Daemon is already running.");
      return;
    }

    DartSdk sdk = DartSdk.getDartSdk(myProject);
    if (sdk == null || !isDartSdkVersionSufficient(sdk)) return;

    GeneralCommandLine commandLine = new GeneralCommandLine().withWorkDirectory(sdk.getHomePath());
    commandLine.setCharset(StandardCharsets.UTF_8);
    commandLine.setExePath(FileUtil.toSystemDependentName(DartSdkUtil.getDartExePath(sdk)));
    commandLine.addParameter("devtools");
    commandLine.addParameter("--machine");
    if (dtdUri != null) {
      commandLine.addParameter("--dtd-uri=" + dtdUri);
    }

    logger.info("Starting Dart DevTools, sdk " + sdk.getVersion());
    try {
      processHandler = ProcessHandlerBuilder.create(commandLine).killable().silentReader().build();
    }
    catch (ExecutionException e) {
      logger.warn("Failed to start Dart DevTools", e);
      return;
    }
    processHandler.addProcessListener(new DevToolsProcessListener());
    processHandler.startNotify();
  }

  @Override
  public void dispose() {
    ProcessHandler handler = processHandler;
    if (handler != null && !handler.isProcessTerminated()) {
      ApplicationManager.getApplication().executeOnPooledThread(() -> {
        if (!handler.isProcessTerminated()) {
          handler.destroyProcess();
        }
      });
    }
  }

  private static boolean isDartSdkVersionSufficient(@NotNull DartSdk sdk) {
    return StringUtil.compareVersionNumbers(sdk.getVersion(), MIN_SDK_VERSION) >= 0;
  }

  private final class DevToolsProcessListener implements ProcessListener {
    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
      logger.debug("DevTools output: " + event.getText());
      if (serviceRunning) return;

      // The first line of text is the command issued, which can be ignored.
      String text = event.getText().trim();
      if (text.contains(" devtools --machine")) return;

      try {
        JsonObject json = (JsonObject)JsonParser.parseString(text);
        JsonElement methodElement = json.get("method");
        String method = methodElement == null ? null : methodElement.getAsString();
        if ("server.started".equals(method)) {
          serviceRunning = true;
          JsonObject params = json.get("params").getAsJsonObject();
          String host = params.get("host").getAsString();
          int port = params.get("port").getAsInt();
          devToolsHostAndPort = host + ":" + port;
        }
      }
      catch (Exception e) {
        logger.warn("Failed to parse message, error: " + e.getMessage() + ", message: " + text);
      }
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
      serviceRunning = false;
      devToolsHostAndPort = null;
      logger.info("DevTools terminated, exit code: " + event.getExitCode());
    }
  }
}
