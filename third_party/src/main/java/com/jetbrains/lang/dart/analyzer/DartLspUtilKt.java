// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.analyzer;

import consulo.document.Document;
import consulo.logging.Logger;
import consulo.util.lang.StringUtil;
import org.dartlang.analysis.server.protocol.DartLspPosition;
import org.dartlang.analysis.server.protocol.DartLspTextEdit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Top-level functions of upstream {@code DartLspUtil.kt}.
 */
public final class DartLspUtilKt {
  private static final Logger LOG = Logger.getInstance(DartLspUtilKt.class);

  private DartLspUtilKt() {
  }

  public static @Nullable Integer getOffsetInDocument(@NotNull Document document, @NotNull DartLspPosition position) {
    int lineCount = document.getLineCount();
    int line = position.getLine();
    int character = position.getCharacter();
    if (line == lineCount && character == 0) return document.getTextLength();
    if (line < 0 || line >= lineCount || character < 0) return null;

    int lineStartOffset = document.getLineStartOffset(line);
    if (line + 1 < lineCount && character > 0) {
      // Make sure that the `character` value doesn't exceed the line length.
      int nextLineStartOffset = document.getLineStartOffset(line + 1);
      character = Math.min(character, nextLineStartOffset - 1 - lineStartOffset);
    }
    else if (line + 1 == lineCount && character > 0) {
      // workaround for servers that send { "line": <last_line>, "character": 2147483647 }
      character = Math.min(character, document.getTextLength() - lineStartOffset);
    }

    int offset = lineStartOffset + character;
    return offset <= document.getTextLength() ? offset : null;
  }

  public static boolean applyTextEdits(@NotNull Document document, @NotNull List<DartLspTextEdit> textEdits) {
    List<DartLspTextEdit> sorted = new ArrayList<>(textEdits);
    // descending sorting needed to apply edits starting from the end of the document, so the edits they don't influence each other
    sorted.sort((edit1, edit2) -> {
      int lineDiff = edit2.getRange().getStart().getLine() - edit1.getRange().getStart().getLine();
      return lineDiff != 0 ? lineDiff : edit2.getRange().getStart().getCharacter() - edit1.getRange().getStart().getCharacter();
    });
    for (DartLspTextEdit edit : sorted) {
      if (!applyTextEdit(document, edit)) return false;
    }
    return true;
  }

  /**
   * @return `true` if `textEdit` was applied successfully;
   * `false` if the `textEdit` can't be applied to the `document` because `textEdit.range` is outside the `document` text range
   */
  public static boolean applyTextEdit(@NotNull Document document, @NotNull DartLspTextEdit textEdit) {
    Integer startOffset = getOffsetInDocument(document, textEdit.getRange().getStart());
    Integer endOffset = getOffsetInDocument(document, textEdit.getRange().getEnd());
    if (startOffset == null || endOffset == null) {
      LOG.warn("Ignoring TextEdit, its text range is outside the document text range.\n" +
               "document.lineCount = " + document.getLineCount() + ", document.textLength = " + document.getTextLength() +
               ", range: " + textEdit.getRange());
      return false;
    }

    String newText = StringUtil.convertLineSeparators(textEdit.getNewText());
    document.replaceString(startOffset, endOffset, newText);
    return true;
  }
}
