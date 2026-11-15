// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.editor;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.action.AbstractWordSelectioner;
import consulo.codeEditor.Editor;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.DartLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ExtensionImpl
public final class DartWordSelectionHandler extends AbstractWordSelectioner {

  @Override
  public boolean canSelect(final @NotNull PsiElement e) {
    return e.getLanguage() == DartLanguage.INSTANCE;
  }

  @Override
  public List<TextRange> select(final @NotNull PsiElement psiElement,
                                final @NotNull CharSequence editorText,
                                final int cursorOffset,
                                final @NotNull Editor editor) {
    final List<TextRange> ranges = super.select(psiElement, editorText, cursorOffset, editor);

    final PsiElement semicolon = DartSelectionFilter.getSiblingSemicolonIfExpression(psiElement);
    if (semicolon != null) {
      includeSemicolonInRanges(ranges, psiElement.getTextRange(), semicolon.getTextRange().getEndOffset());
    }

    return ranges;
  }

  private static void includeSemicolonInRanges(final @NotNull List<TextRange> ranges,
                                               final @NotNull TextRange elementWithoutSemicolonRange,
                                               final int semicolonEndOffset) {
    for (int i = 0; i < ranges.size(); i++) {
      final TextRange range = ranges.get(i);
      if (range.getStartOffset() <= elementWithoutSemicolonRange.getStartOffset() &&
          range.getEndOffset() == elementWithoutSemicolonRange.getEndOffset()) {
        ranges.set(i, TextRange.create(range.getStartOffset(), semicolonEndOffset));
      }
    }
  }
}
