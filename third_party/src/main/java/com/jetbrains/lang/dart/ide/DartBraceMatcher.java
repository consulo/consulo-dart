package com.jetbrains.lang.dart.ide;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.BracePair;
import consulo.language.PairedBraceMatcher;
import consulo.language.psi.PsiFile;
import consulo.language.ast.IElementType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.DartTokenTypesSets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartBraceMatcher implements PairedBraceMatcher {
  private static final BracePair[] BRACE_PAIRS = {
    new BracePair(DartTokenTypes.LBRACE, DartTokenTypes.RBRACE, true),
    new BracePair(DartTokenTypes.LBRACKET, DartTokenTypes.RBRACKET, false),
    new BracePair(DartTokenTypes.LPAREN, DartTokenTypes.RPAREN, false),
    new BracePair(DartTokenTypes.LONG_TEMPLATE_ENTRY_START, DartTokenTypes.LONG_TEMPLATE_ENTRY_END, false)
  };

  @Override
  public BracePair @NotNull [] getPairs() {
    return BRACE_PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
    return !DartTokenTypesSets.STRINGS.contains(contextType);
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
