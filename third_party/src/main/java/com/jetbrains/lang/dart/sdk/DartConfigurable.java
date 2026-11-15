// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
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
package com.jetbrains.lang.dart.sdk;

import consulo.ide.setting.ShowSettingsUtil;
import consulo.project.Project;
import consulo.project.ProjectPropertiesComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Consulo: upstream's "Languages &amp; Frameworks | Dart" page configures the "Dart SDK" library and the modules depending on it.
 * In Consulo the SDK and Dart support are set per module by {@link consulo.dart.module.extension.DartModuleExtension},
 * so only the static API used by upstream code is kept. LSP features are not supported.
 */
public final class DartConfigurable {
  public static final int WEBDEV_PORT_DEFAULT = 53322;
  private static final String WEBDEV_PORT_PROPERTY_NAME = "dart.webdev.port";

  private DartConfigurable() {
  }

  public static void openDartSettings(final @NotNull Project project) {
    ShowSettingsUtil.getInstance().showProjectStructureDialog(project);
  }

  public static int getWebdevPort(@NotNull Project project) {
    return ProjectPropertiesComponent.getInstance(project).getInt(WEBDEV_PORT_PROPERTY_NAME, WEBDEV_PORT_DEFAULT);
  }

  // Consulo: public, used by consulo.dart.settings.DartSettingsConfigurable
  public static void setWebdevPort(@NotNull Project project, int port) {
    ProjectPropertiesComponent.getInstance(project).setValue(WEBDEV_PORT_PROPERTY_NAME, port, WEBDEV_PORT_DEFAULT);
  }

  public static boolean isExperimentalLspFeaturesEnabled(@NotNull Project project) {
    return false;
  }

  public static boolean isLspCodeActionsEnabled(@NotNull Project project) {
    return false;
  }
}
