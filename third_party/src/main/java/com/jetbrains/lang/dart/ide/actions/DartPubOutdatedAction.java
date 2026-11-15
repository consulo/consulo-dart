// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.actions;

import consulo.annotation.component.ActionImpl;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.action.AnActionEvent;
import consulo.project.Project;

import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ActionImpl(id = "Dart.pub.outdated")
public class DartPubOutdatedAction extends DartPubActionBase implements AnActionWithSyncUpdate {
  public DartPubOutdatedAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Dart.pub.outdated.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.Dart.pub.outdated.description"));
  }


  @Override
  public void update(@NotNull AnActionEvent e) {
    super.update(e);

    final Project project = e.getData(Project.KEY);
    DartSdk sdk = project != null ? DartSdk.getDartSdk(project) : null;
    if (sdk == null) {
      e.getPresentation().setEnabledAndVisible(false);
    }
  }

  @Override
  protected @NotNull String getTitle(final @NotNull VirtualFile pubspecYamlFile) {
    final String projectName = PubspecYamlUtil.getDartProjectName(pubspecYamlFile);
    final String prefix = projectName == null ? "" : ("[" + projectName + "] ");
    return prefix + DartBundle.message("dart.pub.outdated.title");
  }

  @Override
  @Nullable protected String[] calculatePubParameters(final @NotNull Project project, final @NotNull VirtualFile pubspecYamlFile) {
    return new String[]{"outdated"};
  }
}
