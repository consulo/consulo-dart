/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.Lsp4jServer;
import org.eclipse.lsp4j.jsonrpc.services.JsonRequest;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * Custom Language Server interface for Dart to support custom LSP requests.
 */
public interface DartLanguageServer extends Lsp4jServer {
  /**
   * Returns the port of the diagnostic server.
   */
  @JsonRequest("dart/diagnosticServer")
  @NotNull CompletableFuture<DiagnosticServerResult> diagnosticServer();

  /**
   * Connects the analysis server to the Dart Tooling Daemon (DTD) at the given URI.
   */
  @JsonRequest("dart/connectToDtd")
  @NotNull CompletableFuture<Object> connectToDtd(@NotNull ConnectToDtdParams params);
}
