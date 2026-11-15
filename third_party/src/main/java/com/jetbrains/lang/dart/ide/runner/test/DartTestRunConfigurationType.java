// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.test;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.ConfigurationTypeUtil;
import consulo.execution.configuration.RunConfiguration;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.language.psi.search.FileTypeIndex;
import consulo.language.psi.scope.GlobalSearchScope;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartTestRunConfigurationType extends ConfigurationTypeBase implements DumbAware {
  public DartTestRunConfigurationType() {
    super("DartTestRunConfigurationType", DartBundle.messageValue("runner.test.configuration.name"),
          DartBundle.messageValue("runner.test.configuration.description"), DartIcons.Dart_test);
    addFactory(new DartTestConfigurationFactory(this));
  }

  public static DartTestRunConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType(DartTestRunConfigurationType.class);
  }

  public static class DartTestConfigurationFactory extends ConfigurationFactory {
    protected DartTestConfigurationFactory(DartTestRunConfigurationType type) {
      super(type);
    }

    @Override
    public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
      return new DartTestRunConfiguration(project, this, "Dart");
    }

    @Override
    public boolean isApplicable(@NotNull Project project) {
      return FileTypeIndex.containsFileOfType(DartFileType.INSTANCE, GlobalSearchScope.projectScope(project));
    }

    @Override
    public @NotNull String getId() {
      return "Dart Test";
    }
  }
}
