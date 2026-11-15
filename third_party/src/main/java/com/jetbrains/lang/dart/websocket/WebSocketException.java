/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.websocket;

public class WebSocketException extends Exception {
  public WebSocketException(String message) {
    super(message);
  }

  public WebSocketException(String message, Throwable cause) {
    super(message, cause);
  }
}
