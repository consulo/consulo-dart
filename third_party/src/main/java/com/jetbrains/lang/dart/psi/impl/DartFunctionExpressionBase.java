// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import consulo.application.AllIcons;
import consulo.ui.image.Image;
import consulo.language.ast.ASTNode;
import consulo.navigation.ItemPresentation;
import consulo.navigation.NavigationItem;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.ui.image.ImageEffects;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.Nullable;

public abstract class DartFunctionExpressionBase extends DartExpressionImpl {
  private static final Image ICON = ImageEffects.appendRight(AllIcons.Nodes.Lambda, AllIcons.Nodes.C_private);

  public DartFunctionExpressionBase(ASTNode node) {
    super(node);
  }

  // Consulo: not an override, used by consulo.dart.DartIconDescriptorUpdater
  public Image getIcon(int flags) {
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
        public LocalizeValue getPresentableText() {
          ItemPresentation container = ((NavigationItem)parent).getPresentation();
          return container == null ? LocalizeValue.empty() : LocalizeValue.join(LocalizeValue.of("() in "), container.getPresentableText());
        }

        @Override
        public @Nullable String getLocationString() {
          ItemPresentation container = ((NavigationItem)parent).getPresentation();
          return container == null ? null : container.getLocationString();
        }

        @Override
        public @Nullable Image getIcon(boolean unused) {
          return DartFunctionExpressionBase.this.getIcon(0);
        }
      };
    }
    return null;
  }
}
