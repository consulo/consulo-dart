// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import consulo.application.AllIcons;
import consulo.language.ast.ASTNode;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import com.intellij.ui.IconManager;
import com.intellij.ui.icons.RowIcon;
import consulo.ide.impl.idea.util.PlatformIcons;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public abstract class DartFunctionExpressionBase extends DartExpressionImpl {
  private static final RowIcon ICON = IconManager.getInstance().createRowIcon(AllIcons.Nodes.Lambda, PlatformIcons.PRIVATE_ICON);

  public DartFunctionExpressionBase(ASTNode node) {
    super(node);
  }

  @Override
  public Icon getIcon(int flags) {
    return ICON;
  }

  @Override
  public ItemPresentation getPresentation() {
    // Hard to believe there is no marker interface to identify a declaration that contains executable code...
    // TODO: Create one! Executable code marker, that is.
    final PsiElement parent = PsiTreeUtil.getParentOfType(this, DartFunctionExpressionBase.class, DartMethodDeclaration.class,
                                                          DartFunctionDeclarationWithBodyOrNative.class,
                                                          DartFactoryConstructorDeclaration.class,
                                                          DartNamedConstructorDeclaration.class, DartFunctionDeclarationWithBody.class,
                                                          DartGetterDeclaration.class, DartSetterDeclaration.class);
    if (parent != null) {
      return new ItemPresentation() {
        @Override
        public @Nullable String getPresentableText() {
          ItemPresentation container = ((NavigationItem)parent).getPresentation();
          return container == null ? null : "() in " + container.getPresentableText();
        }

        @Override
        public @Nullable String getLocationString() {
          ItemPresentation container = ((NavigationItem)parent).getPresentation();
          return container == null ? null : container.getLocationString();
        }

        @Override
        public @Nullable Icon getIcon(boolean unused) {
          return DartFunctionExpressionBase.this.getIcon(0);
        }
      };
    }
    return null;
  }
}
