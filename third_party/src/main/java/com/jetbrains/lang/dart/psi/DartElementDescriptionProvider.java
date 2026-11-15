// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi;

import consulo.language.editor.highlight.HighlightUsagesDescriptionLocation;
import consulo.util.lang.StringUtil;
import consulo.language.psi.ElementDescriptionLocation;
import consulo.language.psi.ElementDescriptionProvider;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.DartComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DartElementDescriptionProvider implements ElementDescriptionProvider {
  @Override
  public @Nullable String getElementDescription(final @NotNull PsiElement element, final @NotNull ElementDescriptionLocation location) {
    if (!(location instanceof HighlightUsagesDescriptionLocation)) return null;

    if (element instanceof DartLibraryNameElement) return "library " + ((DartLibraryNameElement)element).getName();

    if (element instanceof DartNamedElement) {
      final String name = ((DartNamedElement)element).getName();
      final DartComponentType type = DartComponentType.typeOf(element);
      if (type != null) {
        final String typeText = StringUtil.toLowerCase(type.toString());
        return name != null ? typeText + " " + name : typeText;
      }
    }

    return null;
  }
}
