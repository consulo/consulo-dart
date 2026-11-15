// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.call;

import com.intellij.ide.hierarchy.CallHierarchyBrowserBase;
import consulo.language.editor.internal.hierarchy.HierarchyBrowser;
import consulo.language.editor.hierarchy.HierarchyProvider;
import consulo.language.editor.CommonDataKeys;
import consulo.dataContext.DataContext;
import consulo.project.Project;
import com.intellij.platform.dartlsp.impl.features.hierarchy.call.LspCallHierarchyBrowser;
import com.intellij.platform.dartlsp.impl.features.hierarchy.call.LspCallHierarchyProvider;
import consulo.language.psi.PsiElement;
import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Call hierarchy provider for Dart.
 *
 * <p>While LSP features are experimental, this class acts as a proxy: delegating to
 * {@link LspCallHierarchyProvider} when LSP is enabled, and falling back to legacy DAS
 * when disabled or on unsupported SDK versions.
 *
 * <p>TODO(lsp): When LSP is enabled by default and legacy DAS hierarchy is retired,
 * remove the &lt;callHierarchyProvider language="Dart"&gt; registration from plugin.xml
 * and delete this class so the platform's LspCallHierarchyProvider handles Dart directly.
 */

public final class DartCallHierarchyProvider implements HierarchyProvider {
  private final HierarchyProvider myLspProvider = new LspCallHierarchyProvider();

  @Override
  public @Nullable PsiElement getTarget(@NotNull DataContext dataContext) {
    final Project project = CommonDataKeys.PROJECT.getData(dataContext);
    if (project != null && DartConfigurable.isExperimentalLspFeaturesEnabled(project)) {
      return myLspProvider.getTarget(dataContext);
    }
    return DartHierarchyUtil.getResolvedElementAtCursor(dataContext);
  }

  @Override
  public @NotNull HierarchyBrowser createHierarchyBrowser(@NotNull PsiElement target) {
    if (DartConfigurable.isExperimentalLspFeaturesEnabled(target.getProject())) {
      return myLspProvider.createHierarchyBrowser(target);
    }
    return new DartCallHierarchyBrowser(target.getProject(), target);
  }

  @Override
  public void browserActivated(@NotNull HierarchyBrowser hierarchyBrowser) {
    if (hierarchyBrowser instanceof LspCallHierarchyBrowser) {
      myLspProvider.browserActivated(hierarchyBrowser);
      return;
    }
    ((DartCallHierarchyBrowser)hierarchyBrowser).changeView(CallHierarchyBrowserBase.getCallerType());
  }
}
