// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.method;

import consulo.ide.IdeBundle;
import consulo.language.editor.hierarchy.HierarchyNodeDescriptor;
import consulo.language.editor.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.hierarchy.MethodHierarchyBrowserBase;
import consulo.ui.ex.tree.NodeDescriptor;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.IdeActions;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ui.ex.awt.PopupHandler;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Comparator;
import java.util.Map;

import com.jetbrains.lang.dart.logging.PluginLogger;

public class DartMethodHierarchyBrowser extends MethodHierarchyBrowserBase {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartMethodHierarchyBrowser.class);

  public DartMethodHierarchyBrowser(Project project, PsiElement target) {
    super(project, target);
  }

  @Override
  protected @Nullable PsiElement getElementFromDescriptor(@NotNull HierarchyNodeDescriptor descriptor) {
    if (descriptor instanceof DartMethodHierarchyNodeDescriptor) {
      return descriptor.getPsiElement();
    }
    return null;
  }

  @Override
  protected void createTrees(@NotNull Map<? super String, ? super JTree> trees) {
    JTree tree = createTree(false);
    PopupHandler.installPopupMenu(tree, IdeActions.GROUP_METHOD_HIERARCHY_POPUP, ActionPlaces.METHOD_HIERARCHY_VIEW_POPUP);
    trees.put(getMethodType(), tree);
  }

  @Override
  protected @Nullable JPanel createLegendPanel() {
    return createStandardLegendPanel(IdeBundle.message("hierarchy.legend.method.is.defined.in.class"),
                                     IdeBundle.message("hierarchy.legend.method.defined.in.superclass"),
                                     IdeBundle.message("hierarchy.legend.method.should.be.defined"));
  }

  @Override
  protected boolean isApplicableElement(@NotNull PsiElement element) {
    return (element instanceof DartMethodDeclaration ||
            element instanceof DartGetterDeclaration ||
            element instanceof DartSetterDeclaration) &&
           PsiTreeUtil.getParentOfType(element, DartClass.class) != null;
  }

  @Override
  protected @Nullable HierarchyTreeStructure createHierarchyTreeStructure(@NotNull String type, @NotNull PsiElement psiElement) {
    if (!getMethodType().equals(type)) {
      LOG.error("unexpected type: " + type);
      return null;
    }
    return new DartMethodHierarchyTreeStructure(myProject, (DartComponent)psiElement);
  }

  @Override
  protected @Nullable Comparator<NodeDescriptor<?>> getComparator() {
    return null;
  }
}
