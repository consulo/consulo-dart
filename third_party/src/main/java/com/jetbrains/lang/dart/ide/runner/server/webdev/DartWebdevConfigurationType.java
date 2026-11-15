// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server.webdev;

import consulo.annotation.component.ExtensionImpl;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.ConfigurationTypeBase;
import consulo.execution.configuration.ConfigurationTypeUtil;
import consulo.execution.configuration.RunConfiguration;
import consulo.html.language.HtmlFileType;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.language.psi.search.FileTypeIndex;
import consulo.language.psi.scope.GlobalSearchScope;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartWebdevConfigurationType extends ConfigurationTypeBase implements DumbAware {
  public static @NotNull DartWebdevConfigurationType getInstance() {
    return ConfigurationTypeUtil.findConfigurationType(DartWebdevConfigurationType.class);
  }

  public DartWebdevConfigurationType() {
    super("DartWebdevConfigurationType",
          DartBundle.messageValue("webdev.debug.configuration.name"),
          DartBundle.messageValue("webdev.debug.configuration.description"),
          DartIcons.DartWeb);
    addFactory(new ConfigurationFactory(this) {
      @Override
      public @NotNull RunConfiguration createTemplateConfiguration(final @NotNull Project project) {
        return new DartWebdevConfiguration(project, this, "Dart Web");
      }

      @Override
      public boolean isApplicable(final @NotNull Project project) {
        return FileTypeIndex.containsFileOfType(DartFileType.INSTANCE, GlobalSearchScope.projectScope(project)) &&
               FileTypeIndex.containsFileOfType(HtmlFileType.INSTANCE, GlobalSearchScope.projectScope(project));
      }

      @Override
      public @NotNull String getId() {
        return "Dart Web";
      }
    });
  }
}
