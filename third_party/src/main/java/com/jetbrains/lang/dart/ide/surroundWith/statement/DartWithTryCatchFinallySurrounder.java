// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.surroundWith.statement;

import org.jetbrains.annotations.NotNull;
import consulo.localize.LocalizeValue;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.psi.DartTryStatement;
import org.jetbrains.annotations.Nullable;

public class DartWithTryCatchFinallySurrounder extends DartBlockStatementSurrounderBase {
  @Override
  public @NotNull LocalizeValue getTemplateDescription() {
    String description = "try / catch / finally";
    return LocalizeValue.of(description);
  }

  @Override
  protected String getTemplateText() {
    return "try {\n} catch (e) {\ncaret_here: print(e);\n} finally {\n}";
  }

  @Override
  protected @Nullable PsiElement findElementToDelete(PsiElement surrounder) {
    //noinspection ConstantConditions
    return ((DartTryStatement)surrounder).getOnPartList().getFirst().getBlock().getStatements().getFirstChild(); // todo preselect print(e);
  }
}
