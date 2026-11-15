// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.editor;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.action.EnterHandlerDelegate;
import consulo.language.ast.ASTNode;
import consulo.dataContext.DataContext;
import consulo.document.Document;
import consulo.codeEditor.Editor;
import consulo.codeEditor.action.EditorActionHandler;
import consulo.util.lang.ref.Ref;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.ast.IElementType;
import com.intellij.xml.util.HtmlUtil;
import com.jetbrains.lang.dart.DartLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jetbrains.lang.dart.DartTokenTypes.*;

@ExtensionImpl
public final class DartEnterInStringHandler implements EnterHandlerDelegate {

  @Override
  public boolean invokeInsideIndent(int newLineCharOffset, @NotNull Editor editor, @NotNull DataContext dataContext) {
    return EnterHandlerDelegate.super.invokeInsideIndent(newLineCharOffset, editor, dataContext);
  }

  @Override
  public Result preprocessEnter(final @NotNull PsiFile file,
                                final @NotNull Editor editor,
                                final @NotNull Ref<Integer> caretOffsetRef,
                                final @NotNull Ref<Integer> caretAdvanceRef,
                                final @NotNull DataContext dataContext,
                                final EditorActionHandler originalHandler) {
    if (file.getLanguage() != DartLanguage.INSTANCE && !HtmlUtil.isHtmlFile(file)) return Result.Continue;

    int caretOffset = caretOffsetRef.get().intValue();
    PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
    PsiElement psiAtOffset = file.findElementAt(caretOffset);
    int psiOffset;
    if (psiAtOffset == null || (psiOffset = psiAtOffset.getTextRange().getStartOffset()) > caretOffset) {
      return Result.Continue;
    }

    ASTNode node = psiAtOffset.getNode();
    IElementType nodeType = node.getElementType();

    if ((nodeType == SHORT_TEMPLATE_ENTRY_START || nodeType == LONG_TEMPLATE_ENTRY_START) && caretOffset == psiOffset) {
      node = node.getTreeParent();
      nodeType = node.getElementType();
    }

    if (nodeType == RAW_TRIPLE_QUOTED_STRING && caretOffset >= psiOffset + "r'''".length()) {
      return Result.DefaultSkipIndent; // Multiline string gets no indent
    }

    if (nodeType == RAW_SINGLE_QUOTED_STRING && caretOffset >= psiOffset + "r'".length()) {
      char quote = node.getText().charAt(1);
      breakString("r" + quote, String.valueOf(quote), caretOffsetRef, caretAdvanceRef, editor.getDocument());
      return Result.Default;
    }

    if (nodeType == REGULAR_STRING_PART ||
        nodeType == CLOSING_QUOTE ||
        nodeType == SHORT_TEMPLATE_ENTRY ||
        nodeType == LONG_TEMPLATE_ENTRY) {
      final String openingQuoteText = getOpeningQuoteText(node);
      if (openingQuoteText != null) {
        if (openingQuoteText.length() == 1) {
          breakString(openingQuoteText, openingQuoteText, caretOffsetRef, caretAdvanceRef, editor.getDocument());
          return Result.Default;
        }
        else {
          return Result.DefaultSkipIndent; // Multiline string gets no indent
        }
      }
    }

    return Result.Continue;
  }

  @Override
  public Result postProcessEnter(@NotNull PsiFile psiFile, @NotNull Editor editor, @NotNull DataContext dataContext) {
    return null;
  }

  private static @Nullable String getOpeningQuoteText(final @NotNull ASTNode node) {
    ASTNode prev = node.getTreePrev();
    while (prev != null) {
      if (prev.getElementType() == OPEN_QUOTE) {
        return prev.getText();
      }
      prev = prev.getTreePrev();
    }
    return null;
  }

  private static void breakString(final @NotNull String startQuote,
                                  final @NotNull String endQuote,
                                  final @NotNull Ref<Integer> caretOffsetRef,
                                  final @NotNull Ref<Integer> caretAdvanceRef,
                                  final @NotNull Document document) {
    // The final effect is to insert matching close-quote, newline, indent, matching open-quote.
    int caretOffset = caretOffsetRef.get().intValue();
    int caretAdvance = caretAdvanceRef.get().intValue();
    document.insertString(caretOffset, endQuote + startQuote);
    caretOffset += endQuote.length();
    caretAdvance += startQuote.length();
    caretOffsetRef.set(caretOffset);
    caretAdvanceRef.set(caretAdvance);
  }
}
