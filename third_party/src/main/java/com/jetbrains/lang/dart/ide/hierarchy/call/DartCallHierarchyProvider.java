// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.call;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.dataContext.DataContext;
import consulo.language.Language;
import consulo.language.editor.hierarchy.HierarchyKind;
import consulo.language.editor.hierarchy.HierarchyModel;
import consulo.language.editor.hierarchy.HierarchyProvider;
import consulo.language.editor.hierarchy.StandardHierarchyKinds;
import consulo.language.psi.PsiElement;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Consulo: the hierarchy view is built from {@link DartCallHierarchyModel}. Upstream delegates to the LSP call hierarchy
 * when the experimental LSP features are on; the consulo-lsp call hierarchy is not API (and not reachable from the editor
 * yet, see consulo-lsp CONSULO.md), so the analysis server hierarchy is always used.
 */
@ExtensionImpl
public final class DartCallHierarchyProvider implements HierarchyProvider<PsiElement> {
  @Override
  public HierarchyKind getKind() {
    return StandardHierarchyKinds.CALL;
  }

  @RequiredReadAction
  @Override
  public @Nullable PsiElement getTarget(@NotNull DataContext dataContext) {
    return DartHierarchyUtil.getResolvedElementAtCursor(dataContext);
  }

  @RequiredReadAction
  @Override
  public HierarchyModel<PsiElement> createModel(Project project, PsiElement target) {
    return new DartCallHierarchyModel(project, target);
  }

  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
