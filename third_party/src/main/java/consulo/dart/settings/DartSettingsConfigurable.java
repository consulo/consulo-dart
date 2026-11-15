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
package consulo.dart.settings;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ProjectConfigurable;
import consulo.configurable.SimpleConfigurableByProperties;
import consulo.configurable.StandardConfigurableIds;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import consulo.application.ReadAction;
import consulo.dart.analyzer.DartAnalysisServerSettings;
import consulo.language.editor.DaemonCodeAnalyzer;
import consulo.dart.localize.DartConsuloLocalize;
import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.CheckBox;
import consulo.ui.Component;
import consulo.ui.IntBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.util.LabeledBuilder;
import jakarta.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Project-level Dart settings. The SDK and Dart support per module are set with the Dart module extension,
 * so only the settings of upstream {@code DartConfigurable} which are not part of it are here.
 */
@ExtensionImpl
public final class DartSettingsConfigurable extends SimpleConfigurableByProperties implements ProjectConfigurable {
  private final Project myProject;

  @Inject
  public DartSettingsConfigurable(@NotNull Project project) {
    myProject = project;
  }

  @Override
  public @NotNull String getId() {
    return "dart.settings";
  }

  @Override
  public @Nullable String getParentId() {
    return StandardConfigurableIds.EXECUTION_GROUP;
  }

  @Override
  public @NotNull LocalizeValue getDisplayName() {
    return DartBundle.messageValue("dart.title");
  }

  @RequiredUIAccess
  @Override
  protected @NotNull Component createLayout(@NotNull PropertyBuilder propertyBuilder, @NotNull Disposable uiDisposable) {
    VerticalLayout layout = VerticalLayout.create();

    CheckBox analysisServerEnabled = CheckBox.create(DartConsuloLocalize.settingsAnalysisServerEnabled());
    propertyBuilder.add(analysisServerEnabled,
                        () -> DartAnalysisServerSettings.isEnabled(myProject),
                        value -> {
                          if (value == DartAnalysisServerSettings.isEnabled(myProject)) return;
                          DartAnalysisServerSettings.setEnabled(myProject, value);
                          // starts or stops the server, as on project open, and re-highlights with or without its results
                          myProject.getApplication().executeOnPooledThread(() -> {
                            ReadAction.run(() -> DartAnalysisServerService.getInstance(myProject).serverReadyForRequest());
                            DaemonCodeAnalyzer.getInstance(myProject).restart();
                          });
                        });
    layout.add(analysisServerEnabled);

    IntBox webdevPort = IntBox.create(DartConfigurable.WEBDEV_PORT_DEFAULT);
    propertyBuilder.add(webdevPort,
                        () -> DartConfigurable.getWebdevPort(myProject),
                        value -> DartConfigurable.setWebdevPort(myProject, value));
    layout.add(LabeledBuilder.sided(DartBundle.messageValue("settings.page.label.webdev.server.port"), webdevPort));

    return layout;
  }
}
