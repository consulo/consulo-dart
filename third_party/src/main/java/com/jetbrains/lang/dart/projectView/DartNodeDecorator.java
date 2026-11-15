// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.projectView;

import consulo.annotation.component.ExtensionImpl;
import consulo.ui.ex.tree.PresentationData;
import consulo.project.ui.view.tree.ProjectViewNode;
import consulo.project.ui.view.tree.ProjectViewNodeDecorator;
import consulo.project.ui.view.tree.PsiDirectoryNode;
import consulo.project.ui.view.tree.PsiFileNode;
import consulo.codeEditor.CodeInsightColors;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartNodeDecorator implements ProjectViewNodeDecorator {
  @Override
  public void decorate(final @NotNull ProjectViewNode node, final @NotNull PresentationData presentation) {
    if (node instanceof PsiFileNode || node instanceof PsiDirectoryNode) {
      final VirtualFile nodeFile = node.getVirtualFile();
      final Project project = node.getProject();
      if (nodeFile != null && project != null && DartAnalysisServerService.getInstance(project).isFileWithErrors(nodeFile)) {
        presentation.setAttributesKey(CodeInsightColors.ERRORS_ATTRIBUTES);
      }
    }
  }
}
