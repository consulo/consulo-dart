// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.call;

import com.intellij.ide.hierarchy.CallHierarchyBrowserBase;
import consulo.language.editor.hierarchy.HierarchyNodeDescriptor;
import consulo.language.editor.hierarchy.HierarchyTreeStructure;
import consulo.ui.ex.tree.NodeDescriptor;
import consulo.ui.ex.action.ActionGroup;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.IdeActions;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.language.psi.PsiElement;
import consulo.ui.ex.awt.PopupHandler;
import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import com.jetbrains.lang.dart.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Comparator;
import java.util.Map;

public class DartCallHierarchyBrowser extends CallHierarchyBrowserBase {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartCallHierarchyBrowser.class);

  public DartCallHierarchyBrowser(Project project, PsiElement method) {
    super(project, method);
  }

  @Override
  protected @Nullable PsiElement getElementFromDescriptor(@NotNull HierarchyNodeDescriptor descriptor) {
    if (descriptor instanceof DartCallHierarchyNodeDescriptor pyDescriptor) {
      return pyDescriptor.getPsiElement();
    }
    return null;
  }

  @Override
  protected void createTrees(@NotNull Map<? super String, ? super JTree> type2TreeMap) {
    ActionGroup group = (ActionGroup)ActionManager.getInstance().getAction(IdeActions.GROUP_CALL_HIERARCHY_POPUP);
    type2TreeMap.put(getCallerType(), createHierarchyTree(group));
    type2TreeMap.put(getCalleeType(), createHierarchyTree(group));
  }

  private JTree createHierarchyTree(ActionGroup group) {
    JTree tree = createTree(false);
    PopupHandler.installPopupMenu(tree, group, ActionPlaces.CALL_HIERARCHY_VIEW_POPUP);
    return tree;
  }

  @Override
  protected boolean isApplicableElement(@NotNull PsiElement element) {
    return DartHierarchyUtil.isExecutable(element);
  }

  @Override
  protected @Nullable HierarchyTreeStructure createHierarchyTreeStructure(@NotNull String typeName, @NotNull PsiElement psiElement) {
    if (getCallerType().equals(typeName)) {
      return new DartCallerTreeStructure(myProject, psiElement, getCurrentScopeType());
    }
    else if (getCalleeType().equals(typeName)) {
      return new DartCalleeTreeStructure(myProject, psiElement, getCurrentScopeType());
    }
    else {
      LOG.error("unexpected type: " + typeName);
      return null;
    }
  }

  @Override
  protected @Nullable Comparator<NodeDescriptor<?>> getComparator() {
    return DartHierarchyUtil.getComparator(myProject);
  }
}
