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
package consulo.dart.analyzer;

import consulo.project.Project;
import consulo.project.ProjectPropertiesComponent;

/**
 * The Dart analysis server is optional in Consulo: when it is disabled (the default) the server is not started,
 * server based features have no data and the plugin's own PSI based implementations are used.
 */
public final class DartAnalysisServerSettings {
  private static final String ENABLED_PROPERTY = "dart.analysis.server.enabled";

  private DartAnalysisServerSettings() {
  }

  public static boolean isEnabled(Project project) {
    return ProjectPropertiesComponent.getInstance(project).getBoolean(ENABLED_PROPERTY, false);
  }

  public static void setEnabled(Project project, boolean enabled) {
    ProjectPropertiesComponent.getInstance(project).setValue(ENABLED_PROPERTY, enabled, false);
  }
}
