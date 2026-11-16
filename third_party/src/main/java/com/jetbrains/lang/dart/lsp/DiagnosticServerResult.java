/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import org.jetbrains.annotations.NotNull;

public final class DiagnosticServerResult {
  private final int port;

  public DiagnosticServerResult(int port) {
    this.port = port;
  }

  public int getPort() {
    return port;
  }

  public int component1() {
    return port;
  }

  public @NotNull DiagnosticServerResult copy(int port) {
    return new DiagnosticServerResult(port);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DiagnosticServerResult that)) return false;
    return port == that.port;
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(port);
  }

  @Override
  public String toString() {
    return "DiagnosticServerResult(port=" + port + ")";
  }
}
