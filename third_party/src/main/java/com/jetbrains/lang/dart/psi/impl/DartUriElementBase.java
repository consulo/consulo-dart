// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.ast.ASTNode;
import consulo.util.lang.function.Condition;
import consulo.document.util.TextRange;
import consulo.util.lang.StringUtil;
import consulo.language.psi.AbstractElementManipulator;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileFactory;
import consulo.language.psi.PsiFileSystemItem;
import consulo.language.psi.PsiReference;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.psi.DartImportStatement;
import com.jetbrains.lang.dart.psi.DartUriElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DartUriElementBase extends DartPsiCompositeElementImpl implements DartUriElement {

  private static final Condition<PsiFileSystemItem> DART_FILE_OR_DIR_FILTER = item -> item.isDirectory() || item instanceof DartFile;

  public DartUriElementBase(final @NotNull ASTNode node) {
    super(node);
  }

  @Override
  public @Nullable PsiReference getReference() {
    final PsiReference[] references = getReferences();
    return references.length == 0 ? null : references[0];
  }

  @Override
  public PsiReference @NotNull [] getReferences() {
    return new PsiReference[]{new DartFileReference(this, getUriStringAndItsRange().first)};
  }

  @ExtensionImpl
  public static final class DartUriElementManipulator extends AbstractElementManipulator<DartUriElement> {
    @Override
    public @NotNull Class<DartUriElement> getElementClass() {
      return DartUriElement.class;
    }

    @Override
    public DartUriElement handleContentChange(final @NotNull DartUriElement oldUriElement,
                                              final @NotNull TextRange range,
                                              final @NotNull String newContent) {
      final String newUriElementText = StringUtil.replaceSubstring(oldUriElement.getText(), getRangeInElement(oldUriElement).getStartOffset(), getRangeInElement(oldUriElement).getEndOffset(), newContent);
      final PsiFile fileFromText = PsiFileFactory.getInstance(oldUriElement.getProject())
        .createFileFromText("foo.bar", DartLanguage.INSTANCE, "import " + newUriElementText + ";");

      final DartImportStatement importStatement = PsiTreeUtil.findChildOfType(fileFromText, DartImportStatement.class);
      assert importStatement != null : fileFromText.getText();

      return (DartUriElement)oldUriElement.replace(importStatement.getUriElement());
    }

    @Override
    public @NotNull TextRange getRangeInElement(final @NotNull DartUriElement element) {
      return element.getUriStringAndItsRange().second;
    }
  }
}
