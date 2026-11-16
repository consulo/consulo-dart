// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.analyzer;

import com.jetbrains.lang.dart.hints.DartClosingLabelsInlayHintsProvider;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.codeEditor.EditorFactory;
import consulo.language.editor.inlay.DeclarativeInlayHintsPassScheduler;
import consulo.language.editor.inlay.DeclarativeInlayHintsSettings;
import jakarta.inject.Singleton;

import java.util.Arrays;

@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
@Singleton
public class DartClosingLabelManager {
  public static DartClosingLabelManager getInstance() {
    return ApplicationManager.getApplication().getInstance(DartClosingLabelManager.class);
  }

  public void setShowClosingLabels(boolean value) {
    if (value != getShowClosingLabels()) {
      DeclarativeInlayHintsSettings.getInstance().setProviderEnabled(DartClosingLabelsInlayHintsProvider.PROVIDER_ID, value);

      // Consulo: DeclarativeInlayHintsPassFactory.scheduleRecompute -> DeclarativeInlayHintsPassScheduler.scheduleRecompute
      Arrays.stream(EditorFactory.getInstance().getAllEditors())
        .filter(editor -> editor.getProject() != null && DartAnalysisServerService.isLocalAnalyzableFile(editor.getVirtualFile()))
        .forEach(editor -> DeclarativeInlayHintsPassScheduler.getInstance().scheduleRecompute(editor, editor.getProject()));
    }
  }

  public boolean getShowClosingLabels() {
    return Boolean.TRUE.equals(DeclarativeInlayHintsSettings.getInstance().isProviderEnabled(DartClosingLabelsInlayHintsProvider.PROVIDER_ID));
  }
}
