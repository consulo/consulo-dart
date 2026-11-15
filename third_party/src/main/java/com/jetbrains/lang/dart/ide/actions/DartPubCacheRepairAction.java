// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.actions;

import consulo.annotation.component.ActionImpl;
import consulo.application.CommonBundle;
import consulo.project.Project;
import consulo.ui.ex.awt.Messages;

import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ActionImpl(id = "Dart.pub.cache.repair")
public class DartPubCacheRepairAction extends DartPubActionBase {
  public DartPubCacheRepairAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Dart.pub.cache.repair.text"));
    getTemplatePresentation().setDescription(DartBundle.messageValue("action.Dart.pub.cache.repair.description"));
  }

  @Override
  protected @NotNull String getTitle(final @NotNull VirtualFile pubspecYamlFile) {
    return DartBundle.message("dart.pub.cache.repair.title");
  }

    @Override
    @Nullable protected String[] calculatePubParameters(final @NotNull Project project, final @NotNull VirtualFile pubspecYamlFile) {
        final int choice = Messages.showOkCancelDialog(project, DartBundle.message("dart.pub.cache.repair.message"),
                DartBundle.message("dart.pub.cache.repair.title"), CommonBundle.getYesButtonText(),
                CommonBundle.getNoButtonText(), Messages.getWarningIcon());
        return choice == Messages.OK ? new String[]{"cache", "repair"} : null;
    }
}
