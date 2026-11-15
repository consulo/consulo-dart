/*
 * Copyright 2025 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */

package com.jetbrains.lang.dart.dtd;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DTDProcessListener {
  default void onWebSocketMessage(@NotNull String text) {
  }

  default void onWebSocketClose() {
  }

  default void onWebSocketOpen() {
  }

  default void onProcessStarted(@Nullable String uri) {
  }
}
