// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
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
package com.jetbrains.lang.dart.pubServer;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.actions.DartPubActionBase;
import com.jetbrains.lang.dart.ide.runner.DartConsoleFilter;
import com.jetbrains.lang.dart.ide.runner.DartRelativePathsConsoleFilter;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.util.ProcessAdapter;
import consulo.application.ApplicationManager;
import consulo.application.util.function.Computable;
import consulo.builtinWebServer.http.HttpRequest;
import consulo.builtinWebServer.http.HttpResponse;
import consulo.dart.compat.ConsoleManager;
import consulo.dart.compat.NetService;
import consulo.dart.notification.DartNotificationGroups;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.UrlFilter;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.ProcessOutputTypes;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.project.Project;
import consulo.project.ui.notification.Notification;
import consulo.project.ui.notification.NotificationGroup;
import consulo.project.ui.notification.NotificationService;
import consulo.project.ui.notification.NotificationType;
import consulo.project.ui.wm.ToolWindowManager;
import consulo.ui.ModalityState;
import consulo.ui.ex.action.ActionGroup;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.ex.toolWindow.ToolWindow;
import consulo.ui.image.Image;
import consulo.util.dataholder.Key;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * Consulo: the built-in web server is not Netty based in its API. Upstream answers a request for a served file with a
 * redirect to the {@code webdev serve} port (its Netty client channel handler is not used for that), so here
 * {@link #sendToPubServer} answers with a deferred redirect {@link HttpResponse}.
 */
final class PubServerService extends NetService {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(PubServerService.class);

  private static final String DART_WEBDEV = "Dart Webdev";
  private static final NotificationGroup NOTIFICATION_GROUP = DartNotificationGroups.get("Dart Webdev");

  /**
   * Same as {@code BuiltInWebServerKt.TOKEN_PARAM_NAME}.
   */
  private static final String TOKEN_PARAM_NAME = "_ijt";

  private volatile VirtualFile firstServedDir;

  private final ConcurrentMap<VirtualFile, ServerInfo> servedDirToSocketAddress = new ConcurrentHashMap<>();

  private final Object myServerReadyLock = new Object();

  private static final class ServerInfo {
    private final InetSocketAddress address;

    private ServerInfo(InetSocketAddress address) {
      this.address = address;
    }
  }

  PubServerService(@NotNull Project project, @NotNull ConsoleManager consoleManager) {
    super(project, consoleManager);
  }

  @Override
  protected int getAvailableSocketPort() {
    int initialPort = DartConfigurable.getWebdevPort(getProject());
    int maxAttempts = 10;
    int currentPort = initialPort;

    while (true) {
      try (ServerSocket serverSocket = new ServerSocket(currentPort)) {
        return serverSocket.getLocalPort();
      }
      catch (IOException e) {/* try next port */}

      if (++currentPort - initialPort > maxAttempts) {
        return super.getAvailableSocketPort();
      }
    }
  }

  @Override
  protected @NotNull String getConsoleToolWindowId() {
    return DART_WEBDEV;
  }

  @Override
  protected @NotNull Image getConsoleToolWindowIcon() {
    return DartIcons.PubServeToolWindow;
  }

  @Override
  public @NotNull ActionGroup getConsoleToolWindowActions() {
    return new DefaultActionGroup(ActionManager.getInstance().getAction("Dart.stop.dart.webdev.server"));
  }

  @Override
  protected void configureConsole(final @NotNull TextConsoleBuilder consoleBuilder) {
    final VirtualFile servedDir = firstServedDir;
    // DartConsoleFilter's constructor resolves a DartUrlResolver for servedDir, which requires read access.
    final DartConsoleFilter dartConsoleFilter = ApplicationManager.getApplication()
      .runReadAction((Computable<DartConsoleFilter>)() -> new DartConsoleFilter(getProject(), servedDir));
    final String parentPath = servedDir == null ? null : ApplicationManager.getApplication().runReadAction((Computable<String>)() -> {
      final VirtualFile parent = servedDir.getParent();
      return parent != null ? parent.getPath() : null;
    });

    consoleBuilder.addFilter(dartConsoleFilter);
    if (parentPath != null) {
      consoleBuilder.addFilter(new DartRelativePathsConsoleFilter(getProject(), parentPath));
    }
    consoleBuilder.addFilter(new UrlFilter());
  }

  public boolean isPubServerProcessAlive() {
    ProcessHandler processHandler = getProcessHandler().getResultIfFullFilled();
    return processHandler != null && !processHandler.isProcessTerminated();
  }

  public @NotNull CompletableFuture<HttpResponse> sendToPubServer(final @NotNull HttpRequest clientRequest,
                                                                  final @NotNull VirtualFile servedDir,
                                                                  final @NotNull String pathForPubServer) {
    if (getProcessHandler().getResultIfFullFilled() != null) {
      return CompletableFuture.completedFuture(sendToServer(servedDir, clientRequest, pathForPubServer));
    }

    firstServedDir = servedDir;

    return getProcessHandler()
      .get()
      .handle((processHandler, throwable) -> throwable == null
                                             ? sendToServer(servedDir, clientRequest, pathForPubServer)
                                             : badGateway());
  }

  @Override
  protected @Nullable ProcessHandler createProcessHandler(final @NotNull Project project, final int port) throws ExecutionException {
    final DartSdk dartSdk = DartSdk.getDartSdk(project);
    if (dartSdk == null) return null;

    if (!DartWebdev.INSTANCE.getActivated()) {
      ApplicationManager.getApplication()
        .invokeAndWait(() -> DartWebdev.INSTANCE.ensureWebdevActivated(getProject()), ModalityState.any());
    }

    final VirtualFile servedDir = firstServedDir;
    final String[] workDirAndDirName = ApplicationManager.getApplication().runReadAction((Computable<String[]>)() -> {
      if (servedDir == null) {
        return new String[]{null, ""};
      }
      final VirtualFile parent = servedDir.getParent();
      return new String[]{parent != null ? parent.getPath() : null, servedDir.getName()};
    });
    final String workDir = workDirAndDirName[0];
    final String dirName = workDirAndDirName[1];
    final GeneralCommandLine commandLine = new GeneralCommandLine().withWorkDirectory(workDir);
    DartPubActionBase.setupPubExePath(commandLine, dartSdk);
    commandLine.withEnvironment(DartPubActionBase.PUB_ENV_VAR_NAME, DartPubActionBase.getPubEnvValue());

    commandLine.addParameters("global", "run", "webdev", "serve");
    commandLine.addParameter(dirName + ":" + port);
    commandLine.withEnvironment(DartPubActionBase.PUB_ENV_VAR_NAME, DartPubActionBase.getPubEnvValue() + ".webdev");

    final ProcessHandler processHandler = ProcessHandlerBuilder.create(commandLine).build();
    processHandler.addProcessListener(new PubServeOutputListener(project, myServerReadyLock));

    return processHandler;
  }

  @Override
  protected void connectToProcess(final @NotNull CompletableFuture<ProcessHandler> promise,
                                  final int port,
                                  final @NotNull ProcessHandler processHandler,
                                  final @NotNull Consumer<String> errorOutputConsumer) {
    synchronized (myServerReadyLock) {
      try {
        // wait for the Webdev server to start before redirecting, so that Chrome doesn't show error.
        //noinspection WaitNotInLoop
        myServerReadyLock.wait(15000);
      }
      catch (InterruptedException e) {/**/}
    }

    if (processHandler.isProcessTerminated()) {
      promise.cancel(false);
      return;
    }

    InetSocketAddress firstPubServerAddress = new InetSocketAddress(InetAddress.getLoopbackAddress(), port);
    ServerInfo old = servedDirToSocketAddress.put(firstServedDir, new ServerInfo(firstPubServerAddress));
    LOG.assertTrue(old == null);

    super.connectToProcess(promise, port, processHandler, errorOutputConsumer);
  }

  private static @NotNull HttpResponse badGateway() {
    return HttpResponse.create(HttpURLConnection.HTTP_BAD_GATEWAY, null, null);
  }

  @Override
  protected void closeProcessConnections() {
    servedDirToSocketAddress.clear();
  }

  @NotNull HttpResponse sendToServer(final @NotNull VirtualFile servedDir,
                                     final @NotNull HttpRequest clientRequest,
                                     final @NotNull String pathToPubServe) {
    ServerInfo serverInstanceInfo = servedDirToSocketAddress.get(servedDir);
    if (serverInstanceInfo == null) return badGateway();
    final InetSocketAddress address = serverInstanceInfo.address;

    final StringBuilder location = new StringBuilder("http://" + address.getHostString() + ":" + address.getPort() + pathToPubServe);
    boolean first = true;
    for (Map.Entry<String, List<String>> entry : clientRequest.getParameters().entrySet()) {
      if (TOKEN_PARAM_NAME.equals(entry.getKey())) continue;
      for (String value : entry.getValue()) {
        location.append(first ? '?' : '&')
          .append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
          .append('=')
          .append(URLEncoder.encode(value, StandardCharsets.UTF_8));
        first = false;
      }
    }

    // We can't use 301 (MOVED_PERMANENTLY) response status because Pub Serve port will change after restart, but browser will remember outdated redirection URL
    return HttpResponse.create(HttpURLConnection.HTTP_MOVED_TEMP, null, null).withHeader("Location", location.toString());
  }

  private static class PubServeOutputListener extends ProcessAdapter {
    private final Project myProject;
    private final Object myServerReadyLock;
    private boolean myNotificationAboutErrors;
    private Notification myNotification;

    PubServeOutputListener(@NotNull Project project, @NotNull Object serverReadyLock) {
      myProject = project;
      myServerReadyLock = serverReadyLock;
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
      synchronized (myServerReadyLock) {
        myServerReadyLock.notifyAll();
      }
    }

    @Override
    public void onTextAvailable(final @NotNull ProcessEvent event, final @NotNull Key outputType) {
      final String text = StringUtil.toLowerCase(event.getText());

      // Serving `web` on http://localhost:53322
      // or [INFO] Serving `web` on http://localhost:53322
      if (text.contains("serving ")) {
        synchronized (myServerReadyLock) {
          myServerReadyLock.notifyAll();
        }
      }

      if (outputType == ProcessOutputTypes.STDERR || text.startsWith("[error] ")) {
        final boolean error = text.contains("error");

        ApplicationManager.getApplication().invokeLater(() -> showNotificationIfNeeded(error));
      }
      else if (text.contains("could not run in the current directory.") ||
               text.contains("webdev could not run for this project.")) {
        ApplicationManager.getApplication().invokeLater(() -> showNotificationIfNeeded(true));
      }
    }

    private void showNotificationIfNeeded(final boolean isError) {
      final ToolWindow toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(DART_WEBDEV);
      if (toolWindow != null && toolWindow.isVisible()) {
        return;
      }

      if (myNotification != null && !myNotification.isExpired()) {
        if (myNotificationAboutErrors || !isError) {
          return; // already showing correct notification
        }
        myNotification.expire();
      }

      myNotificationAboutErrors = isError; // previous errors are already reported, so reset our flag

      final String message = DartBundle.message(myNotificationAboutErrors ? "dart.webdev.server.output.contains.errors"
                                                                          : "dart.webdev.server.output.contains.warnings");

      myNotification = NotificationService.getInstance()
        .newOfType(NOTIFICATION_GROUP, NotificationType.WARNING)
        .content(LocalizeValue.of(message))
        .addClosingAction(DartBundle.messageValue("open.dart.webdev.server.action.text"), () -> {
          final ToolWindow window = ToolWindowManager.getInstance(myProject).getToolWindow(DART_WEBDEV);
          if (window != null) window.activate(null);
        })
        .notifyAndGet(myProject);
    }
  }
}
