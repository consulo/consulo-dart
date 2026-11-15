/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.websocket;

import java.util.Objects;

public final class WebSocketMessage {
  private final String text;

  public WebSocketMessage(String text) {
    this.text = text;
  }

  public String getText() {
    return text;
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof WebSocketMessage other && Objects.equals(text, other.text);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(text);
  }

  @Override
  public String toString() {
    return "WebSocketMessage(text=" + text + ")";
  }
}
