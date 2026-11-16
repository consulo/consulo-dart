/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public enum LspMethod {
  CODE_ACTION("textDocument/codeAction", true, "code actions"),
  COMPLETION("textDocument/completion", true, "completion"),
  COMPLETION_RESOLVE("completionItem/resolve", false),
  // Not gated by the experimental LSP flag because there is no fallback in legacy mode.
  CONNECT_TO_DTD("dart/connectToDtd", false),
  DEFINITION("textDocument/definition", true, "navigation"),
  DIAGNOSTIC_SERVER("dart/diagnosticServer", false),
  DOCUMENT_HIGHLIGHT("textDocument/documentHighlight", false),
  HOVER("textDocument/hover", false),
  PUBLISH_DIAGNOSTICS("textDocument/publishDiagnostics", true, "errors and warnings"),
  INITIALIZE("initialize"),
  // Not gated by the experimental LSP flag because there is no fallback in legacy mode.
  INLAY_HINT("textDocument/inlayHint", false),
  SEMANTIC_TOKENS_FULL("textDocument/semanticTokens/full", true, "syntax highlighting"),
  SHUTDOWN("shutdown"),
  TYPE_DEFINITION("textDocument/typeDefinition", false),
  REFERENCES("textDocument/references", true, "references"),
  DOCUMENT_SYMBOL("textDocument/documentSymbol", true, "document symbols"),
  SIGNATURE_HELP("textDocument/signatureHelp", true, "signature help");

  private final @NotNull String method;
  private final boolean isExperimental;
  private final @Nullable String presentableName;

  LspMethod(@NotNull String method) {
    this(method, false, null);
  }

  LspMethod(@NotNull String method, boolean isExperimental) {
    this(method, isExperimental, null);
  }

  LspMethod(@NotNull String method, boolean isExperimental, @Nullable String presentableName) {
    this.method = method;
    this.isExperimental = isExperimental;
    this.presentableName = presentableName;
  }

  public @NotNull String getMethod() {
    return method;
  }

  public boolean isExperimental() {
    return isExperimental;
  }

  public @Nullable String getPresentableName() {
    return presentableName;
  }

  public static @Nullable LspMethod fromMethod(@NotNull String method) {
    for (LspMethod entry : values()) {
      if (entry.method.equals(method)) {
        return entry;
      }
    }
    return null;
  }

  public static @NotNull List<LspMethod> getExperimentalFeatures() {
    List<LspMethod> result = new ArrayList<>();
    for (LspMethod entry : values()) {
      if (entry.isExperimental) {
        result.add(entry);
      }
    }
    return result;
  }
}
