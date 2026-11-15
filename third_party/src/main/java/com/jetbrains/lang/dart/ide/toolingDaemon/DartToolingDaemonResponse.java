// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.Nullable;

public class DartToolingDaemonResponse {
  private @Nullable JsonObject result;
  private @Nullable JsonObject error;

  public DartToolingDaemonResponse(@Nullable JsonObject result, @Nullable JsonObject error) {
    this.result = result;
    this.error = error;
  }

  public @Nullable JsonObject getResult() {
    return result;
  }

  public void setResult(@Nullable JsonObject result) {
    this.result = result;
  }

  public @Nullable JsonObject getError() {
    return error;
  }

  public void setError(@Nullable JsonObject error) {
    this.error = error;
  }
}
