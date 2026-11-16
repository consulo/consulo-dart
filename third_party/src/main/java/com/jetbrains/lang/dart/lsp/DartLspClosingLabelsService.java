/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.jetbrains.lang.dart.analyzer.DartFileInfo;
import com.jetbrains.lang.dart.analyzer.DartFileInfoKt;
import com.jetbrains.lang.dart.analyzer.DartLocalFileInfo;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.codeEditor.Editor;
import consulo.codeEditor.EditorFactory;
import consulo.language.editor.inlay.DeclarativeInlayHintsPassScheduler;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartLspClosingLabelsService {
  public static @NotNull DartLspClosingLabelsService getInstance(@NotNull Project project) {
    return project.getInstance(DartLspClosingLabelsService.class);
  }

  private final @NotNull Project project;

  private final Key<List<DartLspClosingLabel>> closingLabelsKey = Key.create("dart.lsp.closing.labels");

  @Inject
  public DartLspClosingLabelsService(@NotNull Project project) {
    this.project = project;
  }

  public void updateClosingLabels(@Nullable String uri, @Nullable List<DartLspClosingLabel> labels) {
    if (project.isDisposed() || StringUtil.isEmpty(uri)) return;
    DartFileInfo fileInfo = DartFileInfoKt.getDartFileInfo(project, uri);
    VirtualFile vFile = fileInfo.findFile();
    if (vFile == null && fileInfo instanceof DartLocalFileInfo localFileInfo) {
      String path = FileUtil.toSystemIndependentName(localFileInfo.getFilePath());
      vFile = VirtualFileManager.getInstance().findFileByUrl("temp://" + path);
    }
    if (vFile == null) return;
    updateClosingLabels(vFile, labels);
  }

  public void updateClosingLabels(@NotNull VirtualFile vFile, @Nullable List<DartLspClosingLabel> labels) {
    if (project.isDisposed() || !vFile.isValid()) return;
    List<DartLspClosingLabel> validLabels = new ArrayList<>();
    if (labels != null) {
      for (DartLspClosingLabel label : labels) {
        if (!StringUtil.isEmptyOrSpaces(label.getLabel()) && label.getRange() != null && label.getRange().getEnd() != null) {
          validLabels.add(label);
        }
      }
    }
    vFile.putUserData(closingLabelsKey, validLabels.isEmpty() ? null : validLabels);
    for (Editor editor : EditorFactory.getInstance().getAllEditors()) {
      if (vFile.equals(editor.getVirtualFile())) {
        DeclarativeInlayHintsPassScheduler.getInstance().scheduleRecompute(editor, project);
      }
    }
  }

  public @NotNull List<DartLspClosingLabel> getClosingLabels(@NotNull VirtualFile file) {
    List<DartLspClosingLabel> labels = file.getUserData(closingLabelsKey);
    return labels != null ? labels : Collections.emptyList();
  }
}
