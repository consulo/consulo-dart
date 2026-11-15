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

import com.jetbrains.lang.dart.DartBundle;
import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.fileChooser.FileChooserTextBoxBuilder;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.IntBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;

/**
 * HTML file and webdev port of upstream {@code DartWebdevConfigurationEditorForm}.
 */
class DartWebdevFragment implements RunConfigurationFragment<DartWebdevConfiguration> {
  @Override
  public String getId() {
    return "dartWebdev";
  }

  @Override
  public LocalizeValue getName() {
    return DartBundle.messageValue("webdev.debug.configuration.name");
  }

  @Override
  public boolean isRemovable() {
    return false;
  }

  @RequiredUIAccess
  @Override
  public RunConfigurationFragmentEditor<DartWebdevConfiguration> createEditor(Project project, Disposable parent) {
    FileChooserTextBoxBuilder.Controller htmlFileField = FileChooserTextBoxBuilder.create(project)
      .fileChooserDescriptor(new FileChooserDescriptor(true, false, false, false, false, false)
                               .withFileFilter(file -> "html".equalsIgnoreCase(file.getExtension())))
      .uiDisposable(parent)
      .build();
    IntBox portField = IntBox.create();

    return new RunConfigurationFragmentEditor<>() {
      @RequiredUIAccess
      @Override
      public void addRows(RunConfigurationFragmentLayout layout) {
        layout.addLabeled(DartBundle.messageValue("web.run.config.label.html.file"), htmlFileField.getComponent());
        layout.addLabeled(DartBundle.messageValue("web.run.config.label.webdev.port"), portField);
      }

      @RequiredUIAccess
      @Override
      public void reset(DartWebdevConfiguration target) {
        DartWebdevParameters parameters = target.getParameters();
        htmlFileField.setValue(FileUtil.toSystemDependentName(parameters.getHtmlFilePath()), false);
        portField.setValue(parameters.getWebdevPort());
      }

      @RequiredUIAccess
      @Override
      public void apply(DartWebdevConfiguration target) {
        DartWebdevParameters parameters = target.getParameters();
        parameters.setHtmlFilePath(FileUtil.toSystemIndependentName(htmlFileField.getValue().trim()));
        Integer port = portField.getValue();
        if (port != null) parameters.setWebdevPort(port);
        target.setParameters(parameters);
      }
    };
  }
}
