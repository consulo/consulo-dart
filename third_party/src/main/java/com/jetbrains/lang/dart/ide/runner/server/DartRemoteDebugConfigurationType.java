// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server;

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
public final class DartRemoteDebugConfigurationType extends ConfigurationTypeBase implements DumbAware {
  public static DartRemoteDebugConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType(DartRemoteDebugConfigurationType.class);
  }

  public DartRemoteDebugConfigurationType() {
    super("DartRemoteDebugConfigurationType",
          DartBundle.messageValue("remote.debug.configuration.name"),
          DartBundle.messageValue("remote.debug.configuration.description"),
          DartIcons.Dart_remote);
    addFactory(new ConfigurationFactory(this) {
      @Override
      public @NotNull RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DartRemoteDebugConfiguration(project, DartRemoteDebugConfigurationType.this, "Dart Remote");
      }

      @Override
      public boolean isApplicable(@NotNull Project project) {
        return FileTypeIndex.containsFileOfType(DartFileType.INSTANCE, GlobalSearchScope.projectScope(project));
      }

      @Override
      public @NotNull String getId() {
        return "Dart Remote Debug";
      }
    });
  }
}
