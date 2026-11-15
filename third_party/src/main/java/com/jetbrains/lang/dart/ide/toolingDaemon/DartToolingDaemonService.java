// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.devtools.DartDevToolsService;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.websocket.WebSocket;
import com.jetbrains.lang.dart.websocket.WebSocketEventHandler;
import com.jetbrains.lang.dart.websocket.WebSocketException;
import com.jetbrains.lang.dart.websocket.WebSocketMessage;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.Application;
import consulo.application.ApplicationManager;
import consulo.application.ReadAction;
import consulo.application.util.concurrent.AppExecutorUtil;
import consulo.disposer.Disposable;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.ContentEntry;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.project.Project;
import consulo.proxy.EventDispatcher;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.io.URLUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartToolingDaemonService implements Disposable {
  private static final String MIN_SDK_VERSION = "3.4";
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DartToolingDaemonService.class);

  private record PendingRequest(String method, JsonObject params, boolean includeSecret, DartToolingDaemonConsumer consumer) {
  }

  private final @NotNull Project project;

  private @Nullable ProcessHandler dtdProcessHandler;
  private volatile boolean serviceRunning = false;

  private volatile @Nullable DartToolingDaemonWebSocketListener webSocketListener;

  private @Nullable WebSocket webSocket;
  private volatile boolean webSocketReady = false;

  private volatile @Nullable String uri;
  private volatile @Nullable String secret;

  private volatile List<String> lastSentRootUris = List.of();

  private final ConcurrentLinkedQueue<PendingRequest> pendingRequests = new ConcurrentLinkedQueue<>();

  private final AtomicInteger nextRequestId = new AtomicInteger();
  private final Map<Integer, DartToolingDaemonConsumer> consumerMap = Collections.synchronizedMap(new HashMap<>());
  private final Map<String, DartToolingDaemonRequestHandler> servicesMap = Collections.synchronizedMap(new HashMap<>());

  private final EventDispatcher<DartToolingDaemonListener> eventDispatcher = EventDispatcher.create(DartToolingDaemonListener.class);

  private boolean activeLocationChangeEventSupported = false;

  @Inject
  DartToolingDaemonService(@NotNull Project project) {
    this.project = project;
    new DartActiveLocationChangeHandler(this);
  }

  public static @NotNull DartToolingDaemonService getInstance(@NotNull Project project) {
    return project.getInstance(DartToolingDaemonService.class);
  }

  public @NotNull Project getProject() {
    return project;
  }

  public @Nullable DartToolingDaemonWebSocketListener getWebSocketListener() {
    return webSocketListener;
  }

  public void setWebSocketListener(@Nullable DartToolingDaemonWebSocketListener webSocketListener) {
    this.webSocketListener = webSocketListener;
  }

  public boolean getWebSocketReady() {
    return webSocketReady;
  }

  public @Nullable String getUri() {
    return uri;
  }

  public void startService() {
    if (serviceRunning) {
      logger.error("Ignoring startService() call. Dart Tooling Daemon is already running.");
      return;
    }

    DartSdk sdk = DartSdk.getDartSdk(project);
    if (sdk == null || !isDartSdkVersionSufficient(sdk)) return;

    activeLocationChangeEventSupported = DartAnalysisServerService.isDartSdkVersionSufficientForWorkspaceApplyEdits(sdk.getVersion());

    GeneralCommandLine commandLine = DartToolingDaemonCommandLineKt.createDtdCommandLine(sdk);

    logger.info("Starting Dart Tooling Daemon, sdk " + sdk.getVersion());
    try {
      dtdProcessHandler = ProcessHandlerBuilder.create(commandLine).killable().silentReader().build();
    }
    catch (ExecutionException e) {
      logger.warn("Failed to start Dart Tooling Daemon", e);
      return;
    }
    dtdProcessHandler.addProcessListener(new DtdProcessListener());
    dtdProcessHandler.startNotify();
  }

  private void onServiceStarted(@Nullable String uri, @Nullable String secret) {
    serviceRunning = true;
    this.uri = uri;
    this.secret = secret;
    DartDevToolsService.getInstance(project).startService(uri);
    if (uri != null) {
      connectToDtdWebSocket(uri);
      // Consulo: no LSP, DartLspService.connectToDtd is not called
    }
    new DtdEditorService(project, this).setUpService();
  }

  private void connectToDtdWebSocket(@NotNull String uri) {
    try {
      WebSocket socket = new WebSocket(new URI(uri));
      socket.setEventHandler(new DtdWebSocketEventHandler());
      webSocket = socket;
      socket.connect();
    }
    catch (Exception e) {
      logger.error("Failed to connect to Dart Tooling Daemon, uri: " + uri, e);
    }
  }

  public void stopService() {
    List<Future<?>> pendingShutdownTasks = new ArrayList<>();
    ProcessHandler handler = dtdProcessHandler;
    if (handler != null && !handler.isProcessTerminated()) {
      pendingShutdownTasks.add(ApplicationManager.getApplication().executeOnPooledThread(() -> {
        if (!handler.isProcessTerminated()) {
          handler.destroyProcess();
        }
      }));
    }
    WebSocket socket = webSocket;
    if (socket != null) {
      pendingShutdownTasks.add(ApplicationManager.getApplication().executeOnPooledThread(() -> {
        try {
          socket.close();
        }
        catch (Exception e) {
          logger.warn("Failed to close DTD web socket", e);
        }
      }));
    }
    for (Future<?> task : pendingShutdownTasks) {
      try {
        task.get();
      }
      catch (Exception e) {
        logger.warn(e);
      }
    }
    webSocketListener = null;
    serviceRunning = false;
    webSocketReady = false;
    uri = null;
    secret = null;
    lastSentRootUris = List.of();
    pendingRequests.clear();
    consumerMap.clear();
    servicesMap.clear();
  }

  @SuppressWarnings("unused") // for the Flutter plugin
  public void registerServiceMethod(@NotNull String service,
                                    @NotNull String method,
                                    @NotNull JsonObject capabilities,
                                    @NotNull DartToolingDaemonRequestHandler consumer) {
    JsonObject params = new JsonObject();
    params.addProperty("service", service);
    params.addProperty("method", method);
    params.add("capabilities", capabilities);
    try {
      sendRequest("registerService", params, false, response -> {
        JsonObject result = response.getAsJsonObject("result");
        if (result == null) {
          logger.error("No result from attempt to register service " + service + "." + method);
          return;
        }

        JsonElement type = result.getAsJsonPrimitive("type");
        if (type == null || !"Success".equals(type.getAsString())) {
          logger.error("Failed to register service " + service + "." + method);
          return;
        }

        servicesMap.put(service + "." + method, consumer);
      });
    }
    catch (WebSocketException e) {
      logger.warn("Failed to register service " + service + "." + method, e);
    }
  }

  public void sendRequest(@NotNull String method,
                          @NotNull JsonObject params,
                          boolean includeSecret,
                          @NotNull DartToolingDaemonConsumer consumer) throws WebSocketException {
    if (!webSocketReady || !pendingRequests.isEmpty()) {
      logger.debug("sendRequest(\"" + method + "\") queued because the socket is not ready or there are pending requests");
      pendingRequests.add(new PendingRequest(method, params, includeSecret, consumer));
      if (webSocketReady) {
        drainPendingRequests();
      }
      return;
    }

    doSendRequest(method, params, includeSecret, consumer);
  }

  private void drainPendingRequests() {
    while (webSocketReady) {
      PendingRequest pending = pendingRequests.poll();
      if (pending == null) break;
      try {
        doSendRequest(pending.method(), pending.params(), pending.includeSecret(), pending.consumer());
      }
      catch (Exception e) {
        logger.warn("Failed to send queued DTD request for " + pending.method(), e);
      }
    }
  }

  private void doSendRequest(@NotNull String method,
                             @NotNull JsonObject params,
                             boolean includeSecret,
                             @NotNull DartToolingDaemonConsumer consumer) throws WebSocketException {
    JsonObject request = new JsonObject();
    request.addProperty("jsonrpc", "2.0");
    request.addProperty("method", method);

    int id = nextRequestId.incrementAndGet();
    request.addProperty("id", id);
    String currentSecret = secret;
    if (includeSecret && currentSecret != null) {
      params.addProperty("secret", currentSecret);
    }
    request.add("params", params);

    consumerMap.put(id, consumer);

    String requestString = request.toString();
    DartToolingDaemonWebSocketListener listener = webSocketListener;
    if (listener != null) listener.onWebSocketRequest(id, method, requestString);
    logger.debug("--> " + requestString);
    Objects.requireNonNull(webSocket).send(requestString);
  }

  private void sendResponse(int id, @Nullable JsonObject result, @Nullable JsonObject error) {
    if (!webSocketReady) {
      logger.warn("sendResponse(\"" + id + "\", " + result + ") called when the socket is not ready");
      return;
    }

    JsonObject response = new JsonObject();
    response.addProperty("jsonrpc", "2.0");
    response.addProperty("id", id);
    if (error == null) {
      response.add("result", result);
    }
    else {
      response.add("error", error);
    }

    String responseString = response.toString();
    logger.debug("--> " + responseString);
    try {
      Objects.requireNonNull(webSocket).send(responseString);
    }
    catch (WebSocketException e) {
      logger.warn("Failed to send DTD response", e);
    }
  }

  @SuppressWarnings("unused") // for the Flutter plugin
  public void addToolingDaemonListener(@NotNull DartToolingDaemonListener listener, @NotNull Disposable parentDisposable) {
    eventDispatcher.addListener(listener, parentDisposable);
  }

  public void ensureRootsUpToDate() {
    if (!webSocketReady) return;

    ReadAction
      .nonBlocking(this::calcRootUris)
      .coalesceBy(this)
      .expireWith(this)
      .finishOnUiThread(Application::getNoneModalityState, rootUris -> {
        if (!webSocketReady) return;
        if (lastSentRootUris.equals(rootUris)) return;

        // https://pub.dev/documentation/dtd/latest/dtd/DTDConnection/setIDEWorkspaceRoots.html
        JsonObject params = new JsonObject();
        JsonArray rootUrisArray = new JsonArray();
        rootUris.forEach(rootUrisArray::add);
        params.add("roots", rootUrisArray);
        try {
          sendRequest("FileSystem.setIDEWorkspaceRoots", params, true, response -> { });
        }
        catch (WebSocketException e) {
          logger.warn("Failed to send workspace roots to DTD", e);
        }

        lastSentRootUris = rootUris;
      })
      .submit(AppExecutorUtil.getAppExecutorService());
  }

  /**
   * A simplified version of {@link com.jetbrains.lang.dart.analyzer.DartServerRootsHandler}'s calcIncludedAndExcludedDartRootPaths.
   */
  @RequiredReadAction
  private @NotNull List<String> calcRootUris() {
    if (DartSdk.getDartSdk(project) == null) return List.of();

    List<String> rootUris = new ArrayList<>();
    for (Module module : DartSdkLibUtil.getModulesWithDartSdkEnabled(project)) {
      for (ContentEntry contentEntry : ModuleRootManager.getInstance(module).getContentEntries()) {
        String contentEntryUrl = contentEntry.getUrl();
        if (contentEntryUrl.startsWith(URLUtil.FILE_PROTOCOL + URLUtil.SCHEME_SEPARATOR)) {
          String rootPath = VirtualFileUtil.urlToPath(contentEntryUrl);
          rootUris.add(getLocalFileUri(rootPath));
        }
      }
    }

    return rootUris;
  }

  @Override
  public void dispose() {
    stopService();
  }

  private static boolean isDartSdkVersionSufficient(@NotNull DartSdk sdk) {
    return StringUtil.compareVersionNumbers(sdk.getVersion(), MIN_SDK_VERSION) >= 0;
  }

  public @NotNull String getFileUri(@NotNull VirtualFile file) {
    return getLocalFileUri(file.getPath());
  }

  /**
   * URIs are constructed in the same way as in {@link DartAnalysisServerService}'s getLocalFileUri
   * but without falling back to plain OS-dependent paths for older SDK versions.
   */
  private static @NotNull String getLocalFileUri(@NotNull String localFilePath) {
    String escapedPath = URLUtil.encodePath(FileUtil.toSystemIndependentName(localFilePath));
    String url = VirtualFileManager.constructUrl(URLUtil.FILE_PROTOCOL, escapedPath);
    URI uri = VirtualFileUtil.toUri(url);
    return uri != null ? uri.toString() : url;
  }

  private final class DtdProcessListener implements ProcessListener {
    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
      logger.debug("DTD output: " + event.getText());
      if (serviceRunning) return;

      // The first line of text is the command issued, which can be ignored.
      String text = event.getText().trim();
      if (DartToolingDaemonCommandLineKt.isDtdCommandLine(text)) return;

      String uri = null;
      String secret = null;
      try {
        JsonObject json = (JsonObject)JsonParser.parseString(text);
        JsonObject details = json.get("tooling_daemon_details").getAsJsonObject();
        uri = details.get("uri").getAsString();
        secret = details.get("trusted_client_secret").getAsString();
      }
      catch (Exception e) {
        logger.warn("Failed to parse DTD init message. Error: " + e.getMessage() + ". DTD message: " + text);
      }
      finally {
        onServiceStarted(uri, secret);
      }
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
      serviceRunning = false;
      webSocketReady = false;
      uri = null;
      pendingRequests.clear();
      logger.info("DTD terminated, exit code: " + event.getExitCode());
    }
  }

  private final class DtdWebSocketEventHandler implements WebSocketEventHandler {
    @Override
    public void onOpen() {
      logger.info("Connected to DTD successfully");
      webSocketReady = true;
      ensureRootsUpToDate();
      drainPendingRequests();
    }

    @Override
    public void onMessage(@NotNull WebSocketMessage message) {
      String text = message.getText();
      logger.debug("<-- " + text);
      DartToolingDaemonWebSocketListener listener = webSocketListener;
      if (listener != null) listener.onWebSocketMessage(text);

      JsonObject json;
      try {
        json = (JsonObject)JsonParser.parseString(text);
      }
      catch (Exception e) {
        logger.warn("Failed to parse message, error: " + e.getMessage() + ", message: " + text);
        return;
      }

      JsonElement methodElement = json.get("method");
      String method = methodElement == null ? null : methodElement.getAsString();
      DartToolingDaemonRequestHandler serviceConsumer = method == null ? null : servicesMap.get(method);
      if ("streamNotify".equals(method)) {
        JsonObject params = json.get("params").getAsJsonObject();
        String streamId = params.get("streamId").getAsString();
        eventDispatcher.getMulticaster().received(streamId, json);
      }
      else if (serviceConsumer != null) {
        JsonElement paramsElement = json.get("params");
        JsonObject params = paramsElement == null ? new JsonObject() : paramsElement.getAsJsonObject();
        int id = json.get("id").getAsInt();
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
          DartToolingDaemonResponse response = serviceConsumer.handleRequest(params);
          sendResponse(id, response.getResult(), response.getError());
        });
      }
      else {
        int id = json.get("id").getAsInt();
        DartToolingDaemonConsumer consumer = consumerMap.remove(id);
        if (consumer != null) consumer.received(json);
      }
    }

    @Override
    public void onPing() {
    }

    @Override
    public void onPong() {
    }

    @Override
    public void onClose() {
      webSocketReady = false;
      secret = null;
      lastSentRootUris = List.of();
      pendingRequests.clear();
    }
  }
}
