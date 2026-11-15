// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.formatter;

import consulo.module.Module;
import consulo.language.util.ModuleUtilCore;
import consulo.project.Project;
import consulo.module.content.ProjectFileIndex;
import consulo.document.util.TextRange;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.PostFormatProcessor;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.actions.DartStyleAction;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import org.jetbrains.annotations.NotNull;

final class DartPostFormatProcessor implements PostFormatProcessor {
  @Override
  public @NotNull PsiElement processElement(final @NotNull PsiElement source, final @NotNull CodeStyleSettings settings) {
    return source;
  }

  @Override
  public @NotNull TextRange processText(final @NotNull PsiFile psiFile,
                                        final @NotNull TextRange rangeToReformat,
                                        final @NotNull CodeStyleSettings settings) {
    if (!isApplicable(psiFile)) return rangeToReformat;

    return DartStyleAction.reformatRangeAsPostFormatProcessor(psiFile, rangeToReformat);
  }

  private static boolean isApplicable(final @NotNull PsiFile psiFile) {
    if (!(psiFile instanceof DartFile)) return false;
    final Project project = psiFile.getProject();
    final VirtualFile vFile = psiFile.getVirtualFile();
    if (!DartAnalysisServerService.isLocalAnalyzableFile(vFile)) return false;
    final Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
    if (module == null || !DartSdkLibUtil.isDartSdkEnabled(module)) return false;
    if (!ProjectFileIndex.getInstance(project).isInContent(vFile)) return false;
      return DartAnalysisServerService.getInstance(project).serverReadyForRequest();
  }
}
