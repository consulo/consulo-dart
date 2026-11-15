// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.completion;

import consulo.language.editor.completion.lookup.Lookup;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupManager;
import consulo.language.editor.navigation.GotoDeclarationHandlerBase;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DartGotoDeclarationHandler extends GotoDeclarationHandlerBase {
  @Override
  public @Nullable PsiElement getGotoDeclarationTarget(final @Nullable PsiElement element, final @NotNull Editor editor) {
    final Lookup activeLookup = element != null ? LookupManager.getInstance(element.getProject()).getActiveLookup() : null;
    final LookupElement item = activeLookup != null ? activeLookup.getCurrentItem() : null;
    final Object lookupObject = item != null && item.isValid() ? item.getObject() : null;
    return lookupObject instanceof DartLookupObject ? ((DartLookupObject)lookupObject).findPsiElement() : null;
  }
}
