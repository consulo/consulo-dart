package com.jetbrains.lang.dart.psi;

import consulo.navigation.NavigationItem;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.psi.PsiNamedElement;
import org.jetbrains.annotations.Nullable;

public interface DartNamedElement extends DartPsiCompositeElement, PsiNamedElement, NavigationItem, PsiNameIdentifierOwner {
  @Nullable
  DartId getId();
}
