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
package com.jetbrains.lang.dart.ide.runner.server.webdev;

import com.jetbrains.lang.dart.sdk.DartConfigurable;
import consulo.execution.configuration.RunConfigurationOptions;
import consulo.util.xml.serializer.annotation.Attribute;
import org.jetbrains.annotations.NotNull;

/**
 * Persisted settings of {@link DartWebdevConfiguration}, upstream keeps them in {@link DartWebdevParameters};
 * attribute names are those upstream serializes, so existing configurations are read as is.
 */
public class DartWebdevConfigurationOptions extends RunConfigurationOptions {
  private @NotNull String myHtmlFilePath = "";
  private int myWebdevPort = DartConfigurable.WEBDEV_PORT_DEFAULT;

  @Attribute("htmlFilePath")
  public @NotNull String getHtmlFilePath() {
    return myHtmlFilePath;
  }

  public void setHtmlFilePath(@NotNull String htmlFilePath) {
    myHtmlFilePath = htmlFilePath;
  }

  @Attribute("webdevPort")
  public int getWebdevPort() {
    return myWebdevPort;
  }

  public void setWebdevPort(int webdevPort) {
    myWebdevPort = webdevPort;
  }
}
