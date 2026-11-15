// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.base;

import consulo.virtualFileSystem.fileType.FileType;
import consulo.project.Project;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.execution.debug.evaluation.XDebuggerEditorsProviderBase;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.util.DartElementGenerator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartDebuggerEditorsProvider extends XDebuggerEditorsProviderBase {
  @Override
  public @NotNull FileType getFileType() {
    return DartFileType.INSTANCE;
  }

  @Override
  protected PsiFile createExpressionCodeFragment(@NotNull Project project,
                                                 @NotNull String text,
                                                 @Nullable PsiElement context,
                                                 boolean isPhysical) {
    return DartElementGenerator.createExpressionCodeFragment(project, text, context);
  }
}