/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ConnectToDtdParams {
  private final @NotNull String uri;

  public ConnectToDtdParams(@NotNull String uri) {
    this.uri = uri;
  }

  public @NotNull String getUri() {
    return uri;
  }

  public @NotNull String component1() {
    return uri;
  }

  public @NotNull ConnectToDtdParams copy(@NotNull String uri) {
    return new ConnectToDtdParams(uri);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ConnectToDtdParams that)) return false;
    return Objects.equals(uri, that.uri);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(uri);
  }

  @Override
  public String toString() {
    return "ConnectToDtdParams(uri=" + uri + ")";
  }
}
