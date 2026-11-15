// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.structure;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.language.Language;
import consulo.dart.structure.DartPsiStructureViewModel;
import consulo.dart.analyzer.DartAnalysisServerSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartStructureViewFactory implements PsiStructureViewFactory {
  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Override
  public StructureViewBuilder getStructureViewBuilder(final @NotNull PsiFile psiFile) {
    // Consulo: no LSP; the outline of the analysis server is used only when the server is enabled,
    // otherwise the PSI based structure
    final boolean serverOutline = DartAnalysisServerSettings.isEnabled(psiFile.getProject());
    return new TreeBasedStructureViewBuilder() {
      @Override
      public @NotNull StructureViewModel createStructureViewModel(@Nullable Editor editor) {
        return serverOutline ? new DartStructureViewModel(editor, psiFile) : new DartPsiStructureViewModel(psiFile);
      }

      @Override
      public boolean isRootNodeShown() {
        return false;
      }
    };
  }
}