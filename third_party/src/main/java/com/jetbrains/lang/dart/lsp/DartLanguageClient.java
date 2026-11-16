/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import org.eclipse.lsp4j.jsonrpc.services.JsonNotification;
import org.eclipse.lsp4j.services.LanguageClient;
import org.jetbrains.annotations.NotNull;

/**
 * Custom Language Client interface for Dart to support custom LSP notifications from the server.
 */
public interface DartLanguageClient extends LanguageClient {
  @JsonNotification("dart/textDocument/publishClosingLabels")
  void publishClosingLabels(@NotNull DartPublishClosingLabelsParams params);
}
