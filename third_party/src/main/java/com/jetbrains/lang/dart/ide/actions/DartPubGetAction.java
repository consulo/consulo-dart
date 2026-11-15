// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.actions;

import consulo.annotation.component.ActionImpl;
import consulo.project.Project;

import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ActionImpl(id = "Dart.pub.get")
public class DartPubGetAction extends DartPubActionBase {
  public DartPubGetAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Dart.pub.get.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.Dart.pub.get.description"));
  }

  @Override
  protected @NotNull String getTitle(final @NotNull VirtualFile pubspecYamlFile) {
    final String projectName = PubspecYamlUtil.getDartProjectName(pubspecYamlFile);
    final String prefix = projectName == null ? "" : ("[" + projectName + "] ");
    return prefix + DartBundle.message("dart.pub.get.title");
  }

  @Override
  @Nullable protected String[] calculatePubParameters(final @NotNull Project project, final @NotNull VirtualFile pubspecYamlFile) {
    return new String[]{"get"};
  }
}
