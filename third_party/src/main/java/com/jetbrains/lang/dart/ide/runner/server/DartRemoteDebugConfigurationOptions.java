/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.execution.configuration.RunConfigurationOptions;
import consulo.util.xml.serializer.annotation.Attribute;
import org.jetbrains.annotations.NotNull;

/**
 * Settings of {@link DartRemoteDebugConfiguration}, the same data as {@link DartRemoteDebugParameters}.
 */
public class DartRemoteDebugConfigurationOptions extends RunConfigurationOptions {
  private @NotNull String myDartProjectPath = "";

  @Attribute("dartProjectPath")
  public @NotNull String getDartProjectPath() {
    return myDartProjectPath;
  }

  public void setDartProjectPath(@NotNull String dartProjectPath) {
    myDartProjectPath = dartProjectPath;
  }
}
