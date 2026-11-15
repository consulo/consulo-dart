// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.execution.action.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import consulo.execution.configuration.ConfigurationFactory;
import consulo.module.content.ProjectFileIndex;
import consulo.util.lang.ref.Ref;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ArrayUtil;
import com.jetbrains.lang.dart.ide.runner.test.DartTestRunConfigurationProducer;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.psi.DartImportStatement;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DartCommandLineRuntimeConfigurationProducer extends LazyRunConfigurationProducer<DartCommandLineRunConfiguration> {
  @Override
  public @NotNull ConfigurationFactory getConfigurationFactory() {
    return DartCommandLineRunConfigurationType.getInstance().getConfigurationFactories()[0];
  }

  @Override
  protected boolean setupConfigurationFromContext(final @NotNull DartCommandLineRunConfiguration configuration,
                                                  final @NotNull ConfigurationContext context,
                                                  final @NotNull Ref<PsiElement> sourceElement) {
    final VirtualFile dartFile = getRunnableDartFileFromContext(context, true);
    if (dartFile == null || DartTestRunConfigurationProducer.isFileInTestDirAndTestPackageExists(context.getProject(), dartFile)) {
      return false;
    }

    configuration.getRunnerParameters().setFilePath(dartFile.getPath());
    configuration.getRunnerParameters()
      .setWorkingDirectory(DartCommandLineRunnerParameters.suggestDartWorkingDir(context.getProject(), dartFile));

    configuration.setGeneratedName();
    sourceElement.set(sourceElement.isNull() ? null : sourceElement.get().getContainingFile());

    return true;
  }

  @Override
  public boolean isConfigurationFromContext(final @NotNull DartCommandLineRunConfiguration configuration,
                                            final @NotNull ConfigurationContext context) {
    final VirtualFile dartFile = getDartFileFromContext(context);
    return dartFile != null && dartFile.getPath().equals(configuration.getRunnerParameters().getFilePath());
  }

  public static @Nullable VirtualFile getRunnableDartFileFromContext(final @NotNull ConfigurationContext context,
                                                                     final boolean checkBrowserSpecificImports) {
    final PsiElement psiLocation = context.getPsiLocation();
    final PsiFile psiFile = psiLocation == null ? null : psiLocation.getContainingFile();
    final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(psiFile);

    if (psiFile instanceof DartFile &&
        virtualFile != null &&
        ProjectFileIndex.getInstance(context.getProject()).isInContent(virtualFile) &&
        DartResolveUtil.getMainFunction(psiFile) != null &&
        (!checkBrowserSpecificImports || !hasImport((DartFile)psiFile,
                                                    "dart:html", "dart:html_common", "dart:indexed_db", "dart:js",
                                                    "dart:svg", "dart:web_audio", "dart:web_gl", "dart:web_sql"))) {
      return virtualFile;
    }

    return null;
  }

  private static @Nullable VirtualFile getDartFileFromContext(final @NotNull ConfigurationContext context) {
    final PsiElement psiLocation = context.getPsiLocation();
    final PsiFile psiFile = psiLocation == null ? null : psiLocation.getContainingFile();
    final VirtualFile virtualFile = DartResolveUtil.getRealVirtualFile(psiFile);
    return psiFile instanceof DartFile && virtualFile != null ? virtualFile : null;
  }

  private static boolean hasImport(final @NotNull DartFile psiFile, final String @NotNull ... importTexts) {
    final DartImportStatement[] importStatements = PsiTreeUtil.getChildrenOfType(psiFile, DartImportStatement.class);
    if (importStatements == null) return false;

    for (DartImportStatement importStatement : importStatements) {
      if (ArrayUtil.contains(importStatement.getUriString(), importTexts)) return true;
    }

    return false;
  }
}
