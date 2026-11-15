// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

/**
 * This is a test only listener.
 * In case it's needed to be used in production, use publish-subscribe pattern just like the eventDispatcher
 */
public interface DartToolingDaemonWebSocketListener {
  default void onWebSocketMessage(String text) {
  }

  default void onWebSocketRequest(int id, String method, String text) {
  }
}
