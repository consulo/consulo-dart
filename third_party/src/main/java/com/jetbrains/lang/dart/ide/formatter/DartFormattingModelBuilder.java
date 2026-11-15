// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.formatter;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.codeStyle.FormattingContext;
import consulo.language.codeStyle.FormattingModel;
import consulo.language.codeStyle.FormattingModelBuilder;
import consulo.language.ast.ASTNode;
import consulo.language.psi.PsiFile;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.DocumentBasedFormattingModel;
import com.jetbrains.lang.dart.psi.DartFile;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartFormattingModelBuilder implements FormattingModelBuilder {

  @Override
  public @NotNull FormattingModel createModel(@NotNull FormattingContext formattingContext) {
    // element can be DartFile, DartEmbeddedContent, DartExpressionCodeFragment
    final PsiFile psiFile = formattingContext.getContainingFile();
    CodeStyleSettings settings = formattingContext.getCodeStyleSettings();
    final ASTNode rootNode = psiFile instanceof DartFile ? psiFile.getNode() : formattingContext.getNode();
    final DartBlockContext context = new DartBlockContext(settings, formattingContext.getFormattingMode());
    final DartBlock rootBlock = new DartBlock(rootNode, null, null, settings, context);
    return new DocumentBasedFormattingModel(rootBlock, formattingContext.getProject(), settings, psiFile.getFileType(), psiFile);
  }
}
