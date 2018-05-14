package com.jetbrains.lang.dart.psi.impl;

import javax.annotation.Nonnull;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.xml.XmlTag;
import com.intellij.psi.xml.XmlTagChild;
import com.jetbrains.lang.dart.psi.DartEmbeddedContent;

public class DartEmbeddedContentImpl extends DartPsiCompositeElementImpl implements DartEmbeddedContent {
  public DartEmbeddedContentImpl(@Nonnull ASTNode node) {
    super(node);
  }

  public XmlTag getParentTag() {
    final PsiElement parent = getParent();
    if (parent instanceof XmlTag) return (XmlTag)parent;
    return null;
  }

  public XmlTagChild getNextSiblingInTag() {
    PsiElement nextSibling = getNextSibling();
    if (nextSibling instanceof XmlTagChild) return (XmlTagChild)nextSibling;
    return null;
  }

  public XmlTagChild getPrevSiblingInTag() {
    final PsiElement prevSibling = getPrevSibling();
    if (prevSibling instanceof XmlTagChild) return (XmlTagChild)prevSibling;
    return null;
  }

  public boolean processElements(PsiElementProcessor processor, PsiElement place) {
    // TODO
    return true;
  }
}
