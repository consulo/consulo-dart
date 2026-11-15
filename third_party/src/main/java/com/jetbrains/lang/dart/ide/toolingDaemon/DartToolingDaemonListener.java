// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.google.gson.JsonObject;

import java.util.EventListener;

@FunctionalInterface
public interface DartToolingDaemonListener extends EventListener {
  void received(String streamId, JsonObject message);
}
