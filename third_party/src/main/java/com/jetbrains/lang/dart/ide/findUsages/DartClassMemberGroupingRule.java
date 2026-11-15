// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.findUsages;

import consulo.application.dumb.DumbAware;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.usage.Usage;
import consulo.usage.UsageGroup;
import consulo.usage.UsageInfo2UsageAdapter;
import consulo.usage.UsageTarget;
import consulo.usage.rule.PsiElementUsage;
import consulo.usage.rule.SingleParentUsageGroupingRule;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.psi.DartClassMembers;
import com.jetbrains.lang.dart.psi.DartComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DartClassMemberGroupingRule extends SingleParentUsageGroupingRule implements DumbAware {
  @Override
  protected @Nullable UsageGroup getParentGroupFor(@NotNull Usage usage, UsageTarget @NotNull [] targets) {
    PsiElement psiElement = usage instanceof PsiElementUsage ? ((PsiElementUsage)usage).getElement() : null;
    if (psiElement == null || psiElement.getLanguage() != DartLanguage.INSTANCE) return null;
    if (psiElement instanceof PsiFile && usage instanceof UsageInfo2UsageAdapter adapter) {
      psiElement = psiElement.findElementAt(adapter.getUsageInfo().getNavigationOffset());
    }

    // todo Docs are not parsed perfectly and doc comment may be not a child of the corresponding function. Related to comment for DartDocUtil.getDocumentationText

    while (psiElement != null) {
      if (psiElement instanceof DartComponent componentElement && psiElement.getParent() instanceof DartClassMembers) {
        return new DartComponentUsageGroup(componentElement);
      }
      psiElement = psiElement.getParent();
    }

    return null;
  }
}
