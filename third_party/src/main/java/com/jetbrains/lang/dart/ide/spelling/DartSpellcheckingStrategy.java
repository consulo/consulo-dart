// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.spelling;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.language.Language;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.dumb.DumbAware;
import consulo.util.lang.Pair;
import consulo.document.util.TextRange;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiNameIdentifierOwner;
import consulo.language.impl.psi.LeafPsiElement;
import consulo.language.spellcheker.tokenizer.splitter.PlainTextTokenSplitter;
import consulo.language.spellcheker.SpellcheckingStrategy;
import consulo.language.spellcheker.tokenizer.TokenConsumer;
import consulo.language.spellcheker.tokenizer.Tokenizer;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.ide.annotator.DartAnnotator;
import com.jetbrains.lang.dart.psi.DartComponentName;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartSpellcheckingStrategy extends SpellcheckingStrategy implements DumbAware {
  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  private final Tokenizer<PsiElement> myStringLiteralTokenizer = new DartStringLiteralTokenizer();

  @Override
  public @NotNull Tokenizer getTokenizer(final PsiElement element) {
    if (element instanceof PsiNameIdentifierOwner && !(element instanceof DartComponentName)) {
      return EMPTY_TOKENIZER;
    }
    else if (element instanceof LeafPsiElement && element.getNode().getElementType() == DartTokenTypes.REGULAR_STRING_PART) {
      return myStringLiteralTokenizer;
    }
    return super.getTokenizer(element);
  }

  private static class DartStringLiteralTokenizer extends Tokenizer<PsiElement> {
    @Override
    public void tokenize(@NotNull PsiElement element, @NotNull TokenConsumer consumer) {
      String text = element.getText();
      int startIndex = 0;
      for (Pair<TextRange, Boolean> rangeAndValidity : DartAnnotator.getEscapeSequenceRangesAndValidity(text)) {
        TextRange escapeRange = rangeAndValidity.first;
        int escapeStartOffset = escapeRange.getStartOffset();
        if (escapeStartOffset > startIndex) {
          consumer.consumeToken(element, text, false, 0, TextRange.create(startIndex, escapeStartOffset), PlainTextTokenSplitter.getInstance());
        }
        startIndex = escapeRange.getEndOffset();
      }

      if (startIndex < text.length()) {
        consumer.consumeToken(element, text, false, 0, TextRange.create(startIndex, text.length()), PlainTextTokenSplitter.getInstance());
      }
    }
  }
}
