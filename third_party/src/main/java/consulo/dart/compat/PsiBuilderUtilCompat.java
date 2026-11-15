// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.language.ast.IElementType;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.WhitespacesBinders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@code PsiBuilderUtil} methods of IntelliJ which Consulo does not have.
 */
public final class PsiBuilderUtilCompat {
  private PsiBuilderUtilCompat() {
  }

  public static @Nullable PsiBuilder.Marker parseBlockLazy(@NotNull PsiBuilder builder,
                                                           @NotNull IElementType leftBrace,
                                                           @NotNull IElementType rightBrace,
                                                           @NotNull IElementType codeBlock) {
    if (builder.getTokenType() != leftBrace) return null;

    PsiBuilder.Marker marker = builder.mark();

    builder.advanceLexer();

    int braceCount = 1;

    while (braceCount > 0 && !builder.eof()) {
      IElementType tokenType = builder.getTokenType();
      if (tokenType == leftBrace) {
        braceCount++;
      }
      else if (tokenType == rightBrace) {
        braceCount--;
      }
      builder.advanceLexer();
    }

    marker.collapse(codeBlock);

    if (braceCount > 0) {
      marker.setCustomEdgeTokenBinders(null, WhitespacesBinders.GREEDY_RIGHT_BINDER);
    }

    return marker;
  }
}
