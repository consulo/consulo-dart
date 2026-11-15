// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.structure;

import consulo.annotation.component.ExtensionImpl;
import consulo.fileEditor.structureView.StructureViewBuilder;
import consulo.fileEditor.structureView.StructureViewModel;
import consulo.fileEditor.structureView.TreeBasedStructureViewBuilder;
import consulo.language.editor.structureView.PsiStructureViewFactory;
import consulo.codeEditor.Editor;
import com.intellij.platform.dartlsp.impl.structureView.LspStructureViewFactory;
import consulo.language.psi.PsiFile;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartStructureViewFactory implements PsiStructureViewFactory {
  @Override
  public StructureViewBuilder getStructureViewBuilder(final @NotNull PsiFile psiFile) {
    if (DartConfigurable.isExperimentalLspFeaturesEnabled(psiFile.getProject())) {
      return new LspStructureViewFactory().getStructureViewBuilder(psiFile);
    }
    return new TreeBasedStructureViewBuilder() {
      @Override
      public @NotNull StructureViewModel createStructureViewModel(@Nullable Editor editor) {
        return new DartStructureViewModel(editor, psiFile);
      }

      @Override
      public boolean isRootNodeShown() {
        return false;
      }
    };
  }
}