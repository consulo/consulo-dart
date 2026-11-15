/*
 * Copyright 2025 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */

package com.jetbrains.lang.dart.dtd;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonCommandLineKt;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonConsumer;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonListener;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonRequestHandler;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonResponse;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.websocket.WebSocket;
import com.jetbrains.lang.dart.websocket.WebSocketEventHandler;
import com.jetbrains.lang.dart.websocket.WebSocketException;
import com.jetbrains.lang.dart.websocket.WebSocketMessage;
import consulo.application.ApplicationManager;
import consulo.disposer.Disposable;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.proxy.EventDispatcher;
import consulo.util.dataholder.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DTDProcess {
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DTDProcess.class);

  private ProcessHandler osProcessHandler;

  private final Map<String, DartToolingDaemonConsumer> consumerMap = new HashMap<>();

  private boolean processRunning = false;

  private @Nullable String uri;
  private @Nullable String secret;

  private final AtomicInteger nextRequestId = new AtomicInteger();

  private WebSocket webSocket;
  private boolean webSocketReady = false;
  private final Map<String, DartToolingDaemonRequestHandler> servicesMap = new HashMap<>();

  private final EventDispatcher<DartToolingDaemonListener> eventDispatcher = EventDispatcher.create(DartToolingDaemonListener.class);

  private volatile @Nullable DTDProcessListener listener;

  public @Nullable String getUri() {
    return uri;
  }

  public boolean getWebSocketReady() {
    return webSocketReady;
  }

  public @Nullable DTDProcessListener getListener() {
    return listener;
  }

  public void setListener(@Nullable DTDProcessListener listener) {
    this.listener = listener;
  }

  private void connectToWebSocket(@NotNull String uri) {
    try {
      webSocket = new WebSocket(new URI(uri));
      webSocket.setEventHandler(new WebSocketEventHandler() {
        @Override
        public void onClose() {
          DTDProcessListener l = listener;
          if (l != null) l.onWebSocketClose();
          listener = null;
        }

        @Override
        public void onMessage(@NotNull WebSocketMessage message) {
          String text = message.getText();
          handleWebSocketMessage(text);
          DTDProcessListener l = listener;
          if (l != null) l.onWebSocketMessage(text);
        }

        @Override
        public void onOpen() {
          webSocketReady = true;
          DTDProcessListener l = listener;
          if (l != null) l.onWebSocketOpen();
        }

        @Override
        public void onPing() {
        }

        @Override
        public void onPong() {
        }
      });
      webSocket.connect();
    }
    catch (Exception e) {
      logger.error("Failed to connect to Dart Tooling Daemon, uri: " + uri, e);
    }
  }

  // TODO (pq): call from DartToolingDaemonService
  public void addToolingDaemonListener(@NotNull DartToolingDaemonListener listener, @NotNull Disposable parentDisposable) {
    eventDispatcher.addListener(listener, parentDisposable);
  }

  private void handleWebSocketMessage(@NotNull String text) {
    logger.debug("<-- " + text);

    JsonObject json;
    try {
      json = (JsonObject)JsonParser.parseString(text);
    }
    catch (Exception e) {
      logger.warn("Failed to parse message, error: " + e.getMessage() + ", message: " + text);
      return;
    }

    String method = json.has("method") ? json.get("method").getAsString() : null;
    DartToolingDaemonRequestHandler serviceConsumer = method != null ? servicesMap.get(method) : null;
    if ("streamNotify".equals(method)) {
      JsonObject params = json.get("params").getAsJsonObject();
      String streamId = params.get("streamId").getAsString();
      eventDispatcher.getMulticaster().received(streamId, json);
    }
    else if (serviceConsumer != null) {
      JsonObject params = json.get("params").getAsJsonObject();
      String id = json.get("id").getAsString();
      ApplicationManager.getApplication().executeOnPooledThread(() -> {
        DartToolingDaemonResponse response = serviceConsumer.handleRequest(params);
        sendResponse(id, response.getResult(), response.getError());
      });
    }
    else {
      String id = json.get("id").getAsString();
      DartToolingDaemonConsumer consumer = consumerMap.remove(id);
      if (consumer != null) consumer.received(json);
    }
  }

  public void start(@NotNull DartSdk sdk) throws ExecutionException {
    GeneralCommandLine commandLine = DartToolingDaemonCommandLineKt.createDtdCommandLine(sdk);

    // Consulo: KillableProcessHandler with BaseOutputReader.Options.forMostlySilentProcess()
    osProcessHandler = ProcessHandlerBuilder.create(commandLine).killable().silentReader().build();

    osProcessHandler.addProcessListener(new OSProcessListener());
    osProcessHandler.startNotify();
  }

  public void terminate() {
    if (webSocket != null) {
      ApplicationManager.getApplication().executeOnPooledThread(() -> {
        try {
          webSocket.close();
        }
        catch (Exception e) {
          logger.warn("Failed to close DTD web socket", e);
        }
      });
    }
    if (osProcessHandler != null && !osProcessHandler.isProcessTerminated()) {
      ApplicationManager.getApplication().executeOnPooledThread(() -> {
        if (!osProcessHandler.isProcessTerminated()) {
          osProcessHandler.destroyProcess();
        }
      });
    }
    listener = null;
  }

  private void onProcessStarted(@Nullable String uri, @Nullable String secret) {
    processRunning = true;
    this.uri = uri;
    this.secret = secret;

    if (uri != null) {
      connectToWebSocket(uri);
    }

    DTDProcessListener l = listener;
    if (l != null) l.onProcessStarted(uri);
  }

  private void sendResponse(@NotNull String id, @Nullable JsonObject result, @Nullable JsonObject error) {
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
      webSocket.send(responseString);
    }
    catch (WebSocketException e) {
      logger.warn("Failed to send DTD response", e);
    }
  }

  public void sendRequest(@NotNull String method, @NotNull JsonObject params, boolean includeSecret, @NotNull DartToolingDaemonConsumer consumer)
    throws WebSocketException {
    if (!webSocketReady) {
      logger.warn("sendRequest(\"" + method + "\") called when the socket is not ready");
      return;
    }

    JsonObject request = new JsonObject();
    request.addProperty("jsonrpc", "2.0");
    request.addProperty("method", method);

    String id = String.valueOf(nextRequestId.incrementAndGet());
    request.addProperty("id", id);
    if (includeSecret && secret != null) {
      params.addProperty("secret", secret);
    }
    request.add("params", params);

    consumerMap.put(id, consumer);

    String requestString = request.toString();
    logger.debug("--> " + requestString);
    webSocket.send(requestString);
  }

  private final class OSProcessListener implements ProcessListener {
    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
      processRunning = false;
      webSocketReady = false;
      uri = null;
      logger.info("DTD process terminated, exit code: " + event.getExitCode());
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
      logger.debug("DTD output: " + event.getText());

      if (processRunning) return;

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
        onProcessStarted(uri, secret);
      }
    }
  }
}
