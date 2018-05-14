package com.jetbrains.lang.dart.ide;

import com.intellij.lang.BracePair;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.lang.dart.DartTokenTypes;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Maxim.Mossienko
 * Date: 10/12/11
 * Time: 9:07 PM
 */
public class DartBraceMatcher implements com.intellij.lang.PairedBraceMatcher {
  private static BracePair[] ourBracePairs =
    {
      new BracePair(DartTokenTypes.LBRACE, DartTokenTypes.RBRACE, true),
      new BracePair(DartTokenTypes.LBRACKET, DartTokenTypes.RBRACKET, false),
      new BracePair(DartTokenTypes.LPAREN, DartTokenTypes.RPAREN, false)
    };

  @Override
  public BracePair[] getPairs() {
    return ourBracePairs;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@Nonnull IElementType lbraceType, @Nullable IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
