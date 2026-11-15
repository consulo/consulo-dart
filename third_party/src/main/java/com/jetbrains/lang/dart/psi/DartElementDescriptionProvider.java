// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.highlight.HighlightUsagesDescriptionLocation;
import consulo.util.lang.StringUtil;
import consulo.language.psi.ElementDescriptionLocation;
import consulo.language.psi.ElementDescriptionProvider;
import consulo.language.psi.PsiElement;
import consulo.localize.LocalizeValue;
import com.jetbrains.lang.dart.DartComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartElementDescriptionProvider implements ElementDescriptionProvider {
  @Override
  public LocalizeValue getElementDescription(final @NotNull PsiElement element, final @NotNull ElementDescriptionLocation location) {
    if (!(location instanceof HighlightUsagesDescriptionLocation)) return LocalizeValue.empty();

    if (element instanceof DartLibraryNameElement) return LocalizeValue.of("library " + ((DartLibraryNameElement)element).getName());

    if (element instanceof DartNamedElement) {
      final String name = ((DartNamedElement)element).getName();
      final DartComponentType type = DartComponentType.typeOf(element);
      if (type != null) {
        final String typeText = StringUtil.toLowerCase(type.toString());
        return LocalizeValue.of(name != null ? typeText + " " + name : typeText);
      }
    }

    return LocalizeValue.empty();
  }
}
