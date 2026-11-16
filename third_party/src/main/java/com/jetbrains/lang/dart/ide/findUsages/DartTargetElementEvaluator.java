/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.ide.findUsages;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.TargetElementUtilExtender;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Evaluates target elements for Dart references.
 *
 * When LSP references are enabled, this evaluator suppresses PSI target candidates
 * so that IntelliJ's find usages action does not produce duplicate/ambiguous search targets
 * (e.g. an LSP SearchTarget alongside a legacy PsiTargetVariant).
 *
 * NOTE: This is a transitional fix during the LSP migration. When Find Usages is invoked,
 * IntelliJ's SearchTargetVariantsDataRule queries both modern SEARCH_TARGETS (LSP) and
 * falls back to TargetElementUtil if USAGE_TARGETS_KEY is null. Since Dart files maintain
 * a PSI reference tree, TargetElementUtil resolves the reference through PSI, leading to
 * competing targets. This evaluator (or its legacy fallback) can be removed or simplified
 * once the plugin is fully migrated to LSP and legacy DAS / PSI reference resolution is
 * deprecated, or if the platform avoids the TargetElementUtil fallback when LSP targets exist.
 */
// Consulo: there is no TargetElementEvaluatorEx2 (a per-language targetElementEvaluator extension);
// TargetElementUtilExtender is the application-wide equivalent, so the language="Dart" scoping of the upstream
// registration is done by hand below. Note that Consulo's TargetElementUtil.getTargetCandidates consults the extenders
// only after reference.resolve() returned null, while IntelliJ asks the evaluator first.
@ExtensionImpl
public class DartTargetElementEvaluator implements TargetElementUtilExtender {
  @Override
  public @Nullable Collection<PsiElement> getTargetCandidates(@NotNull PsiReference reference) {
    final PsiElement element = reference.getElement();
    if (!element.getLanguage().isKindOf(DartLanguage.INSTANCE)) {
      return null;
    }
    final Project project = element.getProject();
    if (DartAnalysisServerService.isLspReferencesEnabled(project)) {
      // Suppress PSI candidates when LSP handles references to prevent IntelliJ from showing
      // an ambiguous "Find Usages Of" chooser popup.
      return Collections.emptyList();
    }
    // Fall back to standard PSI / DAS reference resolution when LSP is disabled.
    return null;
  }
}
