// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.structure;

import consulo.platform.base.icon.PlatformIconGroup;
import consulo.localize.LocalizeValue;
import static consulo.application.AllIcons.Nodes.*;
import consulo.ui.image.Image;
import consulo.application.AllIcons;
import consulo.fileEditor.structureView.StructureViewTreeElement;
import consulo.language.editor.structureView.PsiTreeElementBase;
import consulo.language.psi.PsiNavigationSupport;
import consulo.document.util.TextRange;
import consulo.util.lang.StringUtil;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ui.image.ImageEffects;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import org.dartlang.analysis.server.protocol.Element;
import org.dartlang.analysis.server.protocol.ElementKind;
import org.dartlang.analysis.server.protocol.Outline;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.Collections;



final class DartStructureViewElement extends PsiTreeElementBase<PsiElement> {
  private static final Image STATIC_FINAL_FIELD_ICON = ImageEffects.layered(AllIcons.Nodes.Field,
      AllIcons.Nodes.StaticMark,
      AllIcons.Nodes.FinalMark);
  private static final Image FINAL_FIELD_ICON = ImageEffects.layered(Field, AllIcons.Nodes.FinalMark);
  private static final Image STATIC_FIELD_ICON = ImageEffects.layered(Field,  AllIcons.Nodes.StaticMark);
  private static final Image STATIC_METHOD_ICON = ImageEffects.layered(Method,  AllIcons.Nodes.StaticMark);
  private static final Image TOP_LEVEL_FUNCTION_ICON = ImageEffects.layered(Lambda,  AllIcons.Nodes.StaticMark);
  private static final Image TOP_LEVEL_VAR_ICON = ImageEffects.layered(Variable,  AllIcons.Nodes.StaticMark);
  private static final Image CONSTRUCTOR_INVOCATION_ICON = ImageEffects.layered(AllIcons.Nodes.Class, TabPin);
  private static final Image FUNCTION_INVOCATION_ICON = ImageEffects.layered(Method, TabPin);
  private static final Image TOP_LEVEL_CONST_ICON = ImageEffects.layered(Variable, AllIcons.Nodes.StaticMark, AllIcons.Nodes.FinalMark);

  private final @NotNull PsiFile myPsiFile;
  private final @NotNull Outline myOutline;
  private final @NotNull String myPresentableText;

  DartStructureViewElement(final @NotNull PsiFile psiFile, final @NotNull Outline outline) {
    super(findBestPsiElementForOutline(psiFile, outline));
    myPsiFile = psiFile;
    myOutline = outline;
    myPresentableText = getPresentableText(outline);
  }

  @Override
  public @NotNull Collection<StructureViewTreeElement> getChildrenBase() {
    if (myOutline.getChildren().isEmpty()) return Collections.emptyList();
    return ContainerUtil.map(myOutline.getChildren(), outline -> new DartStructureViewElement(myPsiFile, outline));
  }

  @Override
  public void navigate(boolean requestFocus) {
    final DartAnalysisServerService service = DartAnalysisServerService.getInstance(myPsiFile.getProject());
    final int offset = service.getConvertedOffset(myPsiFile.getVirtualFile(), myOutline.getElement().getLocation().getOffset());
    PsiNavigationSupport.getInstance().createNavigatable(myPsiFile.getProject(), myPsiFile.getVirtualFile(), offset).navigate(requestFocus);
  }

  @Override
  public @NotNull LocalizeValue getPresentableText() {
    return LocalizeValue.of(myPresentableText);
  }

  private static @NotNull String getPresentableText(final @NotNull Outline outline) {
    final Element element = outline.getElement();
    final StringBuilder b = new StringBuilder();
    if (ElementKind.EXTENSION.equals(element.getKind())) {
      b.append("extension ");
    }
    b.append(element.getName());
    if (StringUtil.isNotEmpty(element.getTypeParameters())) {
      b.append(element.getTypeParameters());
    }
    if (StringUtil.isNotEmpty(element.getParameters())) {
      b.append(element.getParameters());
    }
    if (StringUtil.isNotEmpty(element.getReturnType())) {
      b.append(" ").append(DartPresentableUtil.RIGHT_ARROW).append(" ").append(element.getReturnType());
    }
    return b.toString();
  }

  @Override
  public @Nullable Image getIcon(boolean unused) {
    final Element element = myOutline.getElement();
    final boolean finalOrConst = element.isConst() || element.isFinal();

    return switch (element.getKind()) {
      case ElementKind.CLASS -> element.isAbstract() ? AbstractClass : AllIcons.Nodes.Class;
      // Consulo: no Include node icon
      case ElementKind.EXTENSION -> AllIcons.Nodes.Trait;
      case ElementKind.MIXIN -> AbstractClass;
      case ElementKind.CONSTRUCTOR -> Method;
      case ElementKind.CONSTRUCTOR_INVOCATION -> CONSTRUCTOR_INVOCATION_ICON;
      case ElementKind.ENUM -> AllIcons.Nodes.Enum;
      case ElementKind.ENUM_CONSTANT -> STATIC_FINAL_FIELD_ICON;
      case ElementKind.FIELD -> {
        if (finalOrConst && element.isTopLevelOrStatic()) yield STATIC_FINAL_FIELD_ICON;
        if (finalOrConst) yield FINAL_FIELD_ICON;
        if (element.isTopLevelOrStatic()) yield STATIC_FIELD_ICON;
        yield Field;
      }
      case ElementKind.FUNCTION -> element.isTopLevelOrStatic() ? TOP_LEVEL_FUNCTION_ICON : Lambda;
      case ElementKind.FUNCTION_INVOCATION -> FUNCTION_INVOCATION_ICON;
      case ElementKind.FUNCTION_TYPE_ALIAS, ElementKind.TYPE_ALIAS -> DartComponentType.TYPEDEF.getIcon();
      case ElementKind.GETTER -> element.isTopLevelOrStatic() ? PropertyReadStatic : PropertyRead;
      case ElementKind.METHOD -> {
        if (element.isAbstract()) yield AbstractMethod;
        yield element.isTopLevelOrStatic() ? STATIC_METHOD_ICON : Method;
      }
      case ElementKind.SETTER -> element.isTopLevelOrStatic() ? PropertyWriteStatic : PropertyWrite;
      case ElementKind.TOP_LEVEL_VARIABLE -> finalOrConst ? TOP_LEVEL_CONST_ICON : TOP_LEVEL_VAR_ICON;
      case ElementKind.UNIT_TEST_GROUP -> PlatformIconGroup.modulesTestroot();
      case ElementKind.UNIT_TEST_TEST -> AllIcons.RunConfigurations.Junit;
      case ElementKind.CLASS_TYPE_ALIAS, ElementKind.COMPILATION_UNIT, ElementKind.FILE, ElementKind.LABEL, ElementKind.LIBRARY,
        ElementKind.LOCAL_VARIABLE, ElementKind.PARAMETER, ElementKind.PREFIX, ElementKind.TYPE_PARAMETER, ElementKind.UNKNOWN ->
        // unexpected
        null;
      default -> null;
    };
  }

  static @Nullable PsiElement findBestPsiElementForOutline(@NotNull PsiFile psiFile, @NotNull Outline outline) {
    DartAnalysisServerService das = DartAnalysisServerService.getInstance(psiFile.getProject());
    int startOffset = das.getConvertedOffset(psiFile.getVirtualFile(), outline.getCodeOffset());
    int endOffset = das.getConvertedOffset(psiFile.getVirtualFile(), outline.getCodeOffset() + outline.getCodeLength());
    TextRange outlineRange = TextRange.create(startOffset, endOffset);

    PsiElement element = psiFile.findElementAt(startOffset);
    while ((element instanceof PsiComment || element instanceof PsiWhiteSpace) && element.getTextRange().getEndOffset() < endOffset) {
      PsiElement next = element.getNextSibling();
      if (next != null) {
        element = PsiTreeUtil.getDeepestFirst(next);
      }
      else {
        break;
      }
    }

    if (element != null) {
      while (!(element instanceof PsiFile) && element.getParent() != null && outlineRange.contains(element.getParent().getTextRange())) {
        element = element.getParent();
      }
    }

    return element;
  }
}
