// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.template;

import consulo.language.editor.template.context.TemplateActionContext;
import consulo.language.editor.template.context.TemplateContextType;
import consulo.language.editor.highlight.SyntaxHighlighter;

import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.highlight.DartSyntaxHighlighter;
import com.jetbrains.lang.dart.psi.IDartBlock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DartTemplateContextType extends TemplateContextType {
  protected DartTemplateContextType(@NotNull String presentableName) {
    super(presentableName);
  }

  /**
   * @deprecated Set contextId and baseContextId in plugin.xml instead
   */
  @Deprecated
  protected DartTemplateContextType(@NotNull String id,
                                    @NotNull String presentableName,
                                    @Nullable Class<? extends TemplateContextType> baseContextType) {
    super(id, presentableName, baseContextType);
  }

  @Override
  public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
    PsiFile file = templateActionContext.getFile();
    if (file.getLanguage() instanceof DartLanguage) {
      PsiElement element = file.findElementAt(templateActionContext.getStartOffset());
      return element != null && isInContext(element);
    }
    return false;
  }

  protected abstract boolean isInContext(@NotNull PsiElement element);

  @Override
  public @Nullable SyntaxHighlighter createHighlighter() {
    return new DartSyntaxHighlighter();
  }

  public static final class Generic extends DartTemplateContextType {
    public Generic() {
      super(DartBundle.message("template.context.type.dart"));
    }

    @Override
    protected boolean isInContext(@NotNull PsiElement element) {
      return true;
    }
  }

  public static final class Statement extends DartTemplateContextType {
    public Statement() {
      super(DartBundle.message("template.context.type.dart.statement"));
    }

    @Override
    protected boolean isInContext(@NotNull PsiElement element) {
      return PsiTreeUtil.getNonStrictParentOfType(element, IDartBlock.class, PsiComment.class) instanceof IDartBlock;
    }
  }
}
