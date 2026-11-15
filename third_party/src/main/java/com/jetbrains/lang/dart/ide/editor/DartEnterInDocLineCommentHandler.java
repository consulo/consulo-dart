// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.editor;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.action.EnterHandlerDelegate;
import consulo.dataContext.DataContext;
import consulo.document.Document;
import consulo.codeEditor.Editor;
import consulo.codeEditor.action.EditorActionHandler;
import consulo.util.lang.ref.Ref;
import consulo.util.lang.StringUtil;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.util.lang.CharArrayUtil;
import com.intellij.xml.util.HtmlUtil;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartTokenTypesSets;
import com.jetbrains.lang.dart.ide.documentation.DartDocUtil;
import org.jetbrains.annotations.NotNull;


@ExtensionImpl
public final class DartEnterInDocLineCommentHandler implements EnterHandlerDelegate {

  @Override
  public boolean invokeInsideIndent(int newLineCharOffset, @NotNull Editor editor, @NotNull DataContext dataContext) {
    return EnterHandlerDelegate.super.invokeInsideIndent(newLineCharOffset, editor, dataContext);
  }

  // EnterInLineCommentHandler doesn't work well enough for Dart doc comments
  @Override
  public Result preprocessEnter(final @NotNull PsiFile file,
                                final @NotNull Editor editor,
                                final @NotNull Ref<Integer> caretOffsetRef,
                                final @NotNull Ref<Integer> caretAdvance,
                                final @NotNull DataContext dataContext,
                                final EditorActionHandler originalHandler) {
    if (file.getLanguage() != DartLanguage.INSTANCE && !HtmlUtil.isHtmlFile(file)) return Result.Continue;

    final int caretOffset = caretOffsetRef.get().intValue();
    if (caretOffset == 0) return Result.Continue;

    final Document document = editor.getDocument();
    PsiDocumentManager.getInstance(file.getProject()).commitDocument(editor.getDocument());
    final PsiElement psiAtOffset = file.findElementAt(caretOffset - 1);

    if (psiAtOffset != null && psiAtOffset.getNode().getElementType() == DartTokenTypesSets.SINGLE_LINE_DOC_COMMENT) {
      final CharSequence text = document.getCharsSequence();
      final int offset = CharArrayUtil.shiftForward(text, caretOffset, " \t");

      if (StringUtil.startsWith(text, offset, DartDocUtil.SINGLE_LINE_DOC_COMMENT)) {
        caretOffsetRef.set(offset);
        caretAdvance.set(DartDocUtil.SINGLE_LINE_DOC_COMMENT.length());
      }
      else {
        final String docText = StringUtil.trimStart(psiAtOffset.getText(), DartDocUtil.SINGLE_LINE_DOC_COMMENT);
        final int spacesBeforeText = StringUtil.isEmptyOrSpaces(docText) ? 1 : StringUtil.countChars(docText, ' ', 0, true);
        final int spacesToAdd = Math.max(0, spacesBeforeText - StringUtil.countChars(text, ' ', caretOffset, true));
        document.insertString(caretOffset, DartDocUtil.SINGLE_LINE_DOC_COMMENT + StringUtil.repeatSymbol(' ', spacesToAdd));
        caretAdvance.set(DartDocUtil.SINGLE_LINE_DOC_COMMENT.length() + spacesBeforeText);
      }
      return Result.Default;
    }

    return Result.Continue;
  }

  @Override
  public Result postProcessEnter(@NotNull PsiFile psiFile, @NotNull Editor editor, @NotNull DataContext dataContext) {
    return null;
  }

}
