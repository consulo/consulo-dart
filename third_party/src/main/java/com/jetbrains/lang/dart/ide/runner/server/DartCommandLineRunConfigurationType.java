// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.ConfigurationTypeUtil;
import consulo.execution.configuration.RunConfiguration;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.application.util.NotNullLazyValue;
import consulo.language.psi.search.FileTypeIndex;
import consulo.language.psi.scope.GlobalSearchScope;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;

public final class DartCommandLineRunConfigurationType extends ConfigurationTypeBase implements DumbAware {
  public static DartCommandLineRunConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType(DartCommandLineRunConfigurationType.class);
  }

  public DartCommandLineRunConfigurationType() {
    super("DartCommandLineRunConfigurationType",
          DartBundle.message("runner.command.line.configuration.name"),
          DartBundle.message("runner.command.line.configuration.description"),
          NotNullLazyValue.createValue(() -> DartIcons.Dart_16));
    addFactory(new ConfigurationFactory(this) {
      @Override
      public @NotNull String getName() {
        return DartBundle.message("runner.command.line.configuration.name");
      }

      @Override
      public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DartCommandLineRunConfiguration("Dart", project, DartCommandLineRunConfigurationType.this);
      }

      @Override
      public boolean isApplicable(@NotNull Project project) {
        return FileTypeIndex.containsFileOfType(DartFileType.INSTANCE, GlobalSearchScope.projectScope(project));
      }

      @Override
      public @NotNull String getId() {
        return "Dart Command Line Application";
      }
    });
  }
}
