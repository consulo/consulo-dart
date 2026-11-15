// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.hierarchy.method;

import consulo.application.AllIcons;
import consulo.ui.image.Image;
import consulo.ide.IdeBundle;
import consulo.language.editor.hierarchy.HierarchyNodeDescriptor;
import consulo.ui.ex.tree.NodeDescriptor;
import consulo.navigation.ItemPresentation;
import consulo.project.Project;
import consulo.ui.ex.util.CompositeAppearance;
import consulo.util.lang.Comparing;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import com.intellij.ui.IconManager;
import com.jetbrains.lang.dart.psi.DartClass;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DartMethodHierarchyNodeDescriptor extends HierarchyNodeDescriptor {
  private DartMethodHierarchyTreeStructure myTreeStructure;
  private Image myRawIcon;
  private Image myStateIcon;
  // If myIsImplementor => show '+' icon unless method is abstract, never filtered from view
  protected boolean myIsImplementor = false;
  // If myShouldImplement && !myIsImplementor => show '!' icon
  protected boolean myShouldImplement = false;
  // If myIsSuperclassOfImplementor && !myIsImplementor && !myShouldImplement => show '-' icon
  // If !myIsSuperclassOfImplementor && !myIsImplementor => may be filtered from view
  protected boolean myIsSuperclassOfImplementor = false;

  protected DartMethodHierarchyNodeDescriptor(@NotNull Project project,
                                              NodeDescriptor parentDescriptor,
                                              PsiElement type,
                                              boolean isBase,
                                              @NotNull DartMethodHierarchyTreeStructure treeStructure) {
    super(project, parentDescriptor, type, isBase);
    assert type instanceof DartClass;
    myTreeStructure = treeStructure;
  }

  public final void setTreeStructure(final DartMethodHierarchyTreeStructure treeStructure) {
    myTreeStructure = treeStructure;
  }

  public final DartClass getType() {
    return (DartClass)getPsiElement();
  }

  @Override
  public final boolean update() {
    boolean changes = super.update();
    final CompositeAppearance oldText = myHighlightedText;
    myHighlightedText = new CompositeAppearance();
    DartClass dartClass = getType();
    if (dartClass == null) {
      if (!myHighlightedText.getText().startsWith(getInvalidPrefix())) {
        myHighlightedText.getBeginning().addText(getInvalidPrefix(), HierarchyNodeDescriptor.getInvalidPrefixAttributes());
      }
      return true;
    }

    final ItemPresentation presentation = dartClass.getPresentation();
    Image baseIcon = null;
    Image stateIcon = null;
    if (presentation != null) {
      myHighlightedText.getEnding().addText(presentation.getPresentableText());
      PsiFile file = dartClass.getContainingFile();
      if (file != null) {
        myHighlightedText.getEnding().addText(" (" + file.getName() + ")", HierarchyNodeDescriptor.getPackageNameAttributes());
      }
      baseIcon = presentation.getIcon(false);
      stateIcon = calculateStateIcon();
    }

    if (changes || baseIcon != myRawIcon || stateIcon != myStateIcon) {
      changes = true;

      Image newIcon = myRawIcon = baseIcon;
      myStateIcon = stateIcon;
      if (myIsBase) {
        newIcon = getBaseMarkerIcon(newIcon);
      }
      if (myStateIcon != null) {
        newIcon = IconManager.getInstance().createRowIcon(myStateIcon, newIcon);
      }

      setIcon(newIcon);
    }
    myName = myHighlightedText.getText();
    if (!Comparing.equal(myHighlightedText, oldText)) {
      changes = true;
    }
    return changes;
  }

  private Image calculateStateIcon() {
    if (myIsImplementor) {
      return AllIcons.Hierarchy.MethodDefined;
    }
    if (myShouldImplement) {
      return AllIcons.Hierarchy.ShouldDefineMethod;
    }
    if (!myIsBase) {
      return AllIcons.Hierarchy.MethodNotDefined;
    }
    return null;
  }

  private static String getInvalidPrefix() {
    return IdeBundle.message("node.hierarchy.invalid");
  }
}