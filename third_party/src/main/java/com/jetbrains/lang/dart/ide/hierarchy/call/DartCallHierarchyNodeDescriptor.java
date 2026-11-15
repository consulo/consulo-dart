// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.call;

import consulo.language.editor.hierarchy.HierarchyNodeDescriptor;
import consulo.ui.ex.tree.NodeDescriptor;
import consulo.navigation.ItemPresentation;
import consulo.ui.ex.util.CompositeAppearance;
import consulo.util.lang.Comparing;
import consulo.language.psi.NavigatablePsiElement;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartMethodDeclaration;
import org.jetbrains.annotations.NotNull;

public class DartCallHierarchyNodeDescriptor extends HierarchyNodeDescriptor {

  public DartCallHierarchyNodeDescriptor(final NodeDescriptor parentDescriptor, final @NotNull PsiElement element, final boolean isBase) {
    super(element.getProject(), parentDescriptor, element, isBase);
  }

  @Override
  public boolean update() {
    boolean changes = super.update();
    final CompositeAppearance oldText = myHighlightedText;
    myHighlightedText = new CompositeAppearance();
    NavigatablePsiElement element = (NavigatablePsiElement)getPsiElement();
    if (element == null) {
      return invalidElement();
    }

    final ItemPresentation presentation = element.getPresentation();
    if (presentation != null) {
      if (element instanceof DartMethodDeclaration) {
        if (DartComponentType.typeOf(element) != DartComponentType.CONSTRUCTOR) {
          // Do not print constructors as Class.Class.Class(args) Class
          final DartClass cls = PsiTreeUtil.getParentOfType(element, DartClass.class);
          if (cls != null) {
            myHighlightedText.getEnding().addText(cls.getName() + ".");
          }
        }
      }
      myHighlightedText.getEnding().addText(presentation.getPresentableText());
      PsiFile file = element.getContainingFile();
      if (file != null) {
        myHighlightedText.getEnding().addText(" (" + file.getName() + ")", HierarchyNodeDescriptor.getPackageNameAttributes());
      }
    }
    myName = myHighlightedText.getText();
    if (!Comparing.equal(myHighlightedText, oldText)) {
      changes = true;
    }
    return changes;
  }
}
