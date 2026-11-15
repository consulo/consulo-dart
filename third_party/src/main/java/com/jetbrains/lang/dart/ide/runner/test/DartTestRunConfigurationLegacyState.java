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
package com.jetbrains.lang.dart.ide.runner.test;

import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunConfigurationLegacyState;
import org.jetbrains.annotations.Nullable;

/**
 * XML of upstream {@link DartTestRunnerParameters}, read once to convert old run configurations.
 */
public class DartTestRunConfigurationLegacyState extends DartCommandLineRunConfigurationLegacyState {
  private @Nullable DartTestRunnerParameters.Scope myScope;
  private @Nullable String myTestName;
  private @Nullable String myTargetName;
  private @Nullable String myTestRunnerOptions;

  public @Nullable DartTestRunnerParameters.Scope getScope() {
    return myScope;
  }

  public void setScope(@Nullable DartTestRunnerParameters.Scope scope) {
    myScope = scope;
  }

  public @Nullable String getTestName() {
    return myTestName;
  }

  public void setTestName(@Nullable String testName) {
    myTestName = testName;
  }

  public @Nullable String getTargetName() {
    return myTargetName;
  }

  public void setTargetName(@Nullable String targetName) {
    myTargetName = targetName;
  }

  public @Nullable String getTestRunnerOptions() {
    return myTestRunnerOptions;
  }

  public void setTestRunnerOptions(@Nullable String testRunnerOptions) {
    myTestRunnerOptions = testRunnerOptions;
  }
}
