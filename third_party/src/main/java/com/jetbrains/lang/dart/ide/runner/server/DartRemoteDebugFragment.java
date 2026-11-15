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

import com.jetbrains.lang.dart.DartBundle;
import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.fileChooser.FileChooserDescriptorFactory;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.HtmlLabel;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.io.FileUtil;

/**
 * Dart project path and hint of upstream {@code DartRemoteDebugConfigurationEditor}.
 * Consulo: a folder chooser instead of the combo box of the project's pubspec.yaml folders.
 */
class DartRemoteDebugFragment implements RunConfigurationFragment<DartRemoteDebugConfiguration> {
  @Override
  public String getId() {
    return "dartRemoteDebug";
  }

  @Override
  public LocalizeValue getName() {
    return DartBundle.messageValue("remote.debug.search.sources.in");
  }

  @Override
  public boolean isRemovable() {
    return false;
  }

  @RequiredUIAccess
  @Override
  public RunConfigurationFragmentEditor<DartRemoteDebugConfiguration> createEditor(Project project, Disposable parent) {
    FileChooserTextBoxBuilder.Controller projectPathField = FileChooserTextBoxBuilder.create(project)
      .fileChooserDescriptor(FileChooserDescriptorFactory.createSingleFolderDescriptor())
      .dialogTitle(DartBundle.messageValue("button.browse.dialog.title.select.dart.project.path"))
      .uiDisposable(parent)
      .build();

    return new RunConfigurationFragmentEditor<>() {
      @RequiredUIAccess
      @Override
      public void addRows(RunConfigurationFragmentLayout layout) {
        layout.addLabeled(DartBundle.messageValue("remote.debug.search.sources.in"), projectPathField.getComponent());
        layout.addBottom(HtmlLabel.create(DartBundle.messageValue("remote.debug.hint")));
      }

      @RequiredUIAccess
      @Override
      public void reset(DartRemoteDebugConfiguration target) {
        projectPathField.setValue(FileUtil.toSystemDependentName(target.getParameters().getDartProjectPath()), false);
      }

      @RequiredUIAccess
      @Override
      public void apply(DartRemoteDebugConfiguration target) {
        DartRemoteDebugParameters parameters = target.getParameters();
        parameters.setDartProjectPath(FileUtil.toSystemIndependentName(projectPathField.getValue().trim()));
        target.setParameters(parameters);
      }
    };
  }
}
