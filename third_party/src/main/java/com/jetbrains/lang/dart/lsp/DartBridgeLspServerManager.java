/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.LspServerManager;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.disposer.Disposable;
import consulo.logging.Logger;
import consulo.project.Project;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Future;

/**
 * Project-level service that manages the lifecycle of the LSP bridge server.
 * <p>
 * This manager sets up a local TCP {@link ServerSocket} on a dynamically allocated port
 * and spawns a background thread to listen for incoming connection requests from the JetBrains
 * native LSP client. When {@link #startBridgeServer} is called, it opens the socket and manually
 * triggers the JetBrains LSP client framework via {@link LspServerManager#ensureServerStarted}
 * to connect to this bridge.
 */
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartBridgeLspServerManager implements Disposable {
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DartBridgeLspServerManager.class);

  private final @NotNull Project project;

  private @Nullable ServerSocket serverSocket;
  private @Nullable Future<?> listenFuture;
  // Written on the thread that accepts the client connections and read from the threads that
  // talk to the server, e.g. the one that pushes the configuration.
  private volatile @Nullable ActiveConnection activeConnection;

  @Inject
  public DartBridgeLspServerManager(@NotNull Project project) {
    this.project = project;
  }

  public int getPort() {
    ServerSocket socket = serverSocket;
    return socket != null ? socket.getLocalPort() : -1;
  }

  /**
   * The bridge of the connected LSP client, or {@code null} while no client is connected.
   */
  public @Nullable DartBridgeLspServer getBridgeServer() {
    ActiveConnection connection = activeConnection;
    return connection != null ? connection.bridgeServer : null;
  }

  public boolean isClientConnected() {
    DartBridgeLspServer bridgeServer = getBridgeServer();
    return bridgeServer != null && bridgeServer.isClientConnected();
  }

  public boolean isSemanticTokensAdvertised() {
    DartBridgeLspServer bridgeServer = getBridgeServer();
    return bridgeServer != null && bridgeServer.isSemanticTokensAdvertised();
  }

  public void startBridgeServer() {
    if (serverSocket != null) {
      logger.warn("Bridge LSP Server is already running");
      return;
    }
    try {
      // Bind to a random free port on localhost. The OS will assign an available port.
      // This port is then queried by DartLspServerDescriptor to tell the JetBrains LSP client where to connect.
      serverSocket = new ServerSocket(0, 50, InetAddress.getLoopbackAddress());
      logger.info("Bridge LSP Server socket listening on port " + getPort());
      startListening();

      // Manually trigger the JetBrains LSP client to connect to our bridge.
      // This replaces the automatic file-opened trigger.
      LspServerManager.getInstance(project).ensureServerStarted(DartLspServerSupportProvider.class, new DartLspServerDescriptor(project));
      logger.info("Triggered LSP client start via ensureServerStarted");
    }
    catch (IOException e) {
      logger.error("Failed to start Bridge ServerSocket", e);
    }
  }

  public void stopBridgeServer() {
    logger.info("Stopping Bridge LSP Server");
    // Disconnect the JetBrains LSP client.
    LspServerManager.getInstance(project).stopServers(DartLspServerSupportProvider.class);

    ServerSocket socket = serverSocket;
    if (socket != null) {
      try {
        socket.close();
      }
      catch (IOException e) {
        // Consulo: Kotlin's ServerSocket.close() call propagated IOException unchecked; it is logged here instead.
        logger.warn("Failed to close Bridge ServerSocket", e);
      }
    }
    serverSocket = null;

    Future<?> future = listenFuture;
    if (future != null) {
      future.cancel(true);
    }
    listenFuture = null;

    ActiveConnection connection = activeConnection;
    if (connection != null) {
      connection.close();
    }
    activeConnection = null;
  }

  private void startListening() {
    // Start a background thread to listen for incoming connections from the JetBrains LSP client.
    listenFuture = ApplicationManager.getApplication().executeOnPooledThread((Runnable)() -> {
      while (isServerSocketOpen()) {
        try {
          // accept() is a blocking call. The thread suspends here and consumes no CPU
          // until the JetBrains LSP client initiates a connection.
          ServerSocket server = serverSocket;
          if (server == null) continue;
          Socket socket = server.accept();
          logger.info("Accepted connection from client: " + socket.getRemoteSocketAddress());
          handleClientConnection(socket);
        }
        catch (IOException e) {
          if (isServerSocketOpen()) {
            logger.error("Error accepting connection", e);
          }
        }
      }
    });
  }

  private boolean isServerSocketOpen() {
    ServerSocket socket = serverSocket;
    return socket != null && !socket.isClosed();
  }

  private void handleClientConnection(@NotNull Socket socket) {
    // Close existing connection if any.
    ActiveConnection existing = activeConnection;
    if (existing != null) {
      existing.close();
    }

    DartBridgeLspServer bridgeServer = null;
    try {
      bridgeServer = new DartBridgeLspServer(project);
      Launcher<DartLanguageClient> launcher =
        Launcher.createLauncher(bridgeServer, DartLanguageClient.class, socket.getInputStream(), socket.getOutputStream());

      bridgeServer.connect(launcher.getRemoteProxy());
      Future<Void> startListeningFuture = launcher.startListening();

      activeConnection = new ActiveConnection(socket, bridgeServer, startListeningFuture);
    }
    catch (Exception e) {
      logger.error("Failed to handle client connection", e);
      if (bridgeServer != null) {
        bridgeServer.stop();
      }
      try {
        socket.close();
      }
      catch (IOException ex) {
        // Ignore
      }
    }
  }

  @Override
  public void dispose() {
    logger.info("Disposing DartBridgeLspServerManager");
    stopBridgeServer();
  }

  private final class ActiveConnection {
    final @NotNull Socket socket;
    final @NotNull DartBridgeLspServer bridgeServer;
    final @NotNull Future<?> listeningFuture;

    ActiveConnection(@NotNull Socket socket, @NotNull DartBridgeLspServer bridgeServer, @NotNull Future<?> listeningFuture) {
      this.socket = socket;
      this.bridgeServer = bridgeServer;
      this.listeningFuture = listeningFuture;
    }

    void close() {
      logger.info("Closing active connection");
      bridgeServer.stop();
      listeningFuture.cancel(true);
      try {
        socket.close();
      }
      catch (IOException e) {
        // Ignore
      }
    }
  }
}
