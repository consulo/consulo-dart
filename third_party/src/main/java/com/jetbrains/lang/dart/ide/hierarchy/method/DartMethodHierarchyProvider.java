// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.method;

import consulo.project.Project;
import consulo.language.editor.hierarchy.StandardHierarchyKinds;
import consulo.language.editor.hierarchy.HierarchyModel;
import consulo.language.editor.hierarchy.HierarchyKind;
import consulo.language.Language;
import consulo.annotation.access.RequiredReadAction;
import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.hierarchy.HierarchyProvider;
import consulo.dataContext.DataContext;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartGetterDeclaration;
import com.jetbrains.lang.dart.psi.DartMethodDeclaration;
import com.jetbrains.lang.dart.psi.DartSetterDeclaration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartMethodHierarchyProvider implements HierarchyProvider<PsiElement> {
  @Override
  public HierarchyKind getKind() {
    return StandardHierarchyKinds.METHOD;
  }

  @RequiredReadAction
  @Override
  public @Nullable PsiElement getTarget(@NotNull DataContext dataContext) {
    PsiElement element = DartHierarchyUtil.getResolvedElementAtCursor(dataContext);
    if ((element instanceof DartMethodDeclaration ||
         element instanceof DartGetterDeclaration ||
         element instanceof DartSetterDeclaration) &&
        PsiTreeUtil.getParentOfType(element, DartClass.class) != null) {
      return element;
    }
    return null;
  }

  // Consulo: the hierarchy view is built from DartMethodHierarchyModel
  @RequiredReadAction
  @Override
  public HierarchyModel<PsiElement> createModel(Project project, PsiElement target) {
    return new DartMethodHierarchyModel(project, target);
  }

  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
