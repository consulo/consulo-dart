/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.websocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A thin wrapper around the JDK {@link java.net.http.WebSocket} that originally mirrored the API of the
 * Weberknecht library's WebSocket to ease replacement.
 */
public class WebSocket {
  private static final long CONNECT_TIMEOUT_SECONDS = 10L;
  private static final long SEND_TIMEOUT_SECONDS = 10L;
  private static final long CLOSE_TIMEOUT_SECONDS = 10L;
  private static final HttpClient httpClient = HttpClient.newHttpClient();

  private final URI uri;
  private final AtomicReference<java.net.http.WebSocket> jdkWebSocket = new AtomicReference<>();
  private final Object sendLock = new Object();

  private volatile WebSocketEventHandler eventHandler;

  public WebSocket(URI uri) {
    this.uri = uri;
  }

  public WebSocketEventHandler getEventHandler() {
    return eventHandler;
  }

  public void setEventHandler(WebSocketEventHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  public void connect() throws WebSocketException {
    JdkListener listener = new JdkListener();
    CompletableFuture<java.net.http.WebSocket> future;
    try {
      future = httpClient.newWebSocketBuilder().buildAsync(uri, listener).toCompletableFuture();
    }
    catch (Exception e) {
      throw new WebSocketException("Failed to start WebSocket handshake to " + uri, e);
    }
    try {
      future.get(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
    catch (Exception e) {
      future.cancel(true);
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      throw new WebSocketException("WebSocket handshake to " + uri + " failed", e);
    }
  }

  public void send(String text) throws WebSocketException {
    synchronized (sendLock) {
      java.net.http.WebSocket webSocket = jdkWebSocket.get();
      if (webSocket == null) throw new WebSocketException("WebSocket is not connected");
      CompletableFuture<java.net.http.WebSocket> future = webSocket.sendText(text, true).toCompletableFuture();
      try {
        future.get(SEND_TIMEOUT_SECONDS, TimeUnit.SECONDS);
      }
      catch (Exception e) {
        future.cancel(true);
        if (e instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        throw new WebSocketException("Failed to send WebSocket message", e);
      }
    }
  }

  public void close() throws WebSocketException {
    java.net.http.WebSocket webSocket = jdkWebSocket.getAndSet(null);
    if (webSocket == null) return;
    CompletableFuture<java.net.http.WebSocket> future =
      webSocket.sendClose(java.net.http.WebSocket.NORMAL_CLOSURE, "Normal Closure").toCompletableFuture();
    try {
      future.get(CLOSE_TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }
    catch (Exception e) {
      future.cancel(true);
      if (e instanceof InterruptedException) {
        Thread.currentThread().interrupt();
      }
      throw new WebSocketException("Failed to close WebSocket gracefully", e);
    }
  }

  private final class JdkListener implements java.net.http.WebSocket.Listener {
    private final StringBuilder pendingText = new StringBuilder();

    @Override
    public void onOpen(java.net.http.WebSocket webSocket) {
      jdkWebSocket.set(webSocket);
      webSocket.request(Long.MAX_VALUE);
      WebSocketEventHandler handler = eventHandler;
      if (handler != null) handler.onOpen();
    }

    @Override
    public CompletionStage<?> onText(java.net.http.WebSocket webSocket, CharSequence data, boolean last) {
      pendingText.append(data);
      if (last) {
        String complete = pendingText.toString();
        pendingText.setLength(0);
        WebSocketEventHandler handler = eventHandler;
        if (handler != null) handler.onMessage(new WebSocketMessage(complete));
      }
      return null;
    }

    // We could use the statusCode and the reason for logging purposes
    @Override
    public CompletionStage<?> onClose(java.net.http.WebSocket webSocket, int statusCode, String reason) {
      pendingText.setLength(0);
      jdkWebSocket.compareAndSet(webSocket, null);
      WebSocketEventHandler handler = eventHandler;
      if (handler != null) handler.onClose();
      return null;
    }

    @Override
    public void onError(java.net.http.WebSocket webSocket, Throwable error) {
      pendingText.setLength(0);
      jdkWebSocket.compareAndSet(webSocket, null);
      WebSocketEventHandler handler = eventHandler;
      if (handler != null) handler.onClose();
    }

    @Override
    public CompletionStage<?> onPing(java.net.http.WebSocket webSocket, ByteBuffer message) {
      WebSocketEventHandler handler = eventHandler;
      if (handler != null) handler.onPing();
      return null;
    }

    @Override
    public CompletionStage<?> onPong(java.net.http.WebSocket webSocket, ByteBuffer message) {
      WebSocketEventHandler handler = eventHandler;
      if (handler != null) handler.onPong();
      return null;
    }
  }
}
