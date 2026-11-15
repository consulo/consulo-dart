// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.actions;

import consulo.application.CommonBundle;
import consulo.project.Project;
import consulo.ui.ex.awt.Messages;

import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartPubCacheRepairAction extends DartPubActionBase {
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
