// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.marker;

import consulo.language.editor.DaemonCodeAnalyzerSettings;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.codeEditor.CodeInsightColors;
import consulo.colorScheme.EditorColorsManager;
import consulo.colorScheme.EditorColorsScheme;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.codeEditor.markup.SeparatorPlacement;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiWhiteSpace;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.ide.impl.idea.util.FunctionUtil;
import consulo.ide.impl.idea.util.IconUtil;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.psi.impl.AbstractDartMethodDeclarationImpl;
import org.jetbrains.annotations.NotNull;

public final class DartMethodLineMarkerProvider implements LineMarkerProvider {
  @Override
  public LineMarkerInfo<?> getLineMarkerInfo(final @NotNull PsiElement element) {
    if (!DaemonCodeAnalyzerSettings.getInstance().SHOW_METHOD_SEPARATORS) {
      return null;
    }

    // only continue if element is one of the markable elements (such as methods)
    if (isMarkableElement(element)) {

      // the method line markers are not nestable, aka, methods inside of methods, are not marked
      if (PsiTreeUtil.findFirstParent(element, true, DartMethodLineMarkerProvider::isMarkableElement) != null) {
        return null;
      }

      // move the marker to previous siblings until comments have been included
      PsiElement markerLocation = element;
      while (markerLocation.getPrevSibling() != null &&
             (markerLocation.getPrevSibling() instanceof PsiComment || (markerLocation.getPrevSibling() instanceof PsiWhiteSpace &&
                                                                        markerLocation.getPrevSibling().getPrevSibling() != null &&
                                                                        markerLocation.getPrevSibling()
                                                                          .getPrevSibling() instanceof PsiComment))) {
        markerLocation = markerLocation.getPrevSibling();
      }

      // if the markerLocation element doesn't have a previous sibling (not whitespace), do not mark
      PsiElement prevElement = markerLocation;
      while (prevElement.getPrevSibling() != null && prevElement.getPrevSibling() instanceof PsiWhiteSpace) {
        prevElement = prevElement.getPrevSibling();
      }
      if (prevElement.getPrevSibling() == null) {
        return null;
      }

      PsiElement anchor = PsiTreeUtil.getDeepestFirst(markerLocation);
      // finally, create the marker
        LineMarkerInfo<PsiElement> info = new LineMarkerInfo<>(anchor,
                anchor.getTextRange(),
                IconUtil.getEmptyIcon(true),
                FunctionUtil.<Object, String>nullConstant(),
                null,
                GutterIconRenderer.Alignment.RIGHT,
                () -> "Method separator");
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
      info.separatorColor = scheme.getColor(CodeInsightColors.METHOD_SEPARATORS_COLOR);
      info.separatorPlacement = SeparatorPlacement.TOP;
      return info;
    }
    return null;
  }

  /**
   * Return true if this is such a PsiElement type that is separated by this LineMarkerProvider.
   */
  private static boolean isMarkableElement(final @NotNull PsiElement element) {
    return element instanceof DartMethodDeclaration ||
           element instanceof DartFunctionDeclarationWithBody ||
           element instanceof DartFunctionDeclarationWithBodyOrNative ||
           element instanceof DartGetterDeclaration ||
           element instanceof DartSetterDeclaration ||
           element instanceof DartFactoryConstructorDeclaration ||
           element instanceof AbstractDartMethodDeclarationImpl ||
           element instanceof DartNamedConstructorDeclaration ||
           element instanceof DartIncompleteDeclaration;
  }
}
