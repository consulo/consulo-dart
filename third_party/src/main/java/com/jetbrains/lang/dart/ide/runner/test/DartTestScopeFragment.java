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

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.test.DartTestRunnerParameters.Scope;
import consulo.dart.localize.DartConsuloLocalize;
import consulo.disposer.Disposable;
import consulo.execution.configuration.fragment.RunConfigurationFragment;
import consulo.execution.configuration.fragment.RunConfigurationFragmentEditor;
import consulo.execution.configuration.fragment.RunConfigurationFragmentLayout;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.ui.ComboBox;
import consulo.ui.TextBox;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.util.lang.StringUtil;

/**
 * Test mode, group or test name and test runner options of upstream {@code DartTestConfigurationEditorForm}.
 */
class DartTestScopeFragment implements RunConfigurationFragment<DartTestRunConfiguration> {
  private static final Scope[] UI_SCOPES = {Scope.FOLDER, Scope.FILE, Scope.GROUP_OR_TEST_BY_NAME};

  @Override
  public String getId() {
    return "dartTestScope";
  }

  @Override
  public LocalizeValue getName() {
    return DartConsuloLocalize.runConfigurationFragmentTestKind();
  }

  @Override
  public boolean isRemovable() {
    return false;
  }

  @RequiredUIAccess
  @Override
  public RunConfigurationFragmentEditor<DartTestRunConfiguration> createEditor(Project project, Disposable parent) {
    ComboBox<Scope> scopeCombo = ComboBox.create(UI_SCOPES);
    scopeCombo.setTextRenderer(scope -> scope == null ? LocalizeValue.empty() : LocalizeValue.of(scope.getPresentableName()));
    TextBox testNameField = TextBox.create();
    TextBox testRunnerOptionsField = TextBox.create();
    scopeCombo.addValueListener(event -> testNameField.setEnabled(event.getValue() == Scope.GROUP_OR_TEST_BY_NAME));

    return new RunConfigurationFragmentEditor<>() {
      @RequiredUIAccess
      @Override
      public void addRows(RunConfigurationFragmentLayout layout) {
        layout.addLabeled(DartBundle.messageValue("test.run.config.label.test.mode"), scopeCombo);
        layout.addLabeled(DartBundle.messageValue("test.run.config.label.group.or.test.name"), testNameField);
        layout.addLabeled(DartBundle.messageValue("test.run.config.label.test.runner.options"), testRunnerOptionsField);
      }

      @RequiredUIAccess
      @Override
      public void reset(DartTestRunConfiguration target) {
        Scope scope = target.getScope();
        scopeCombo.setValue(scope);
        testNameField.setValue(scope == Scope.GROUP_OR_TEST_BY_NAME ? StringUtil.notNullize(target.getTestName()) : "");
        testNameField.setEnabled(scope == Scope.GROUP_OR_TEST_BY_NAME);
        testRunnerOptionsField.setValue(StringUtil.notNullize(target.getTestRunnerOptions()));
      }

      @RequiredUIAccess
      @Override
      public void apply(DartTestRunConfiguration target) {
        Scope scope = scopeCombo.getValue();
        target.setScope(scope == null ? Scope.FILE : scope);
        target.setTestName(scope == Scope.GROUP_OR_TEST_BY_NAME ? testNameField.getValue() : null);
        target.setTestRunnerOptions(testRunnerOptionsField.getValue());
      }
    };
  }
}
