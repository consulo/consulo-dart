/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.lang.dart.ide.hierarchy.call;

import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.annotation.access.RequiredReadAction;
import consulo.language.editor.hierarchy.*;
import consulo.language.psi.PsiElement;
import consulo.logging.Logger;
import consulo.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Consulo replacement of upstream {@code DartCallHierarchyBrowser}: Consulo builds the hierarchy view from a model.
 */
public class DartCallHierarchyModel implements HierarchyModel<PsiElement> {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartCallHierarchyModel.class);

  private final Project myProject;
  private final PsiElement myTarget;

  public DartCallHierarchyModel(Project project, PsiElement target) {
    myProject = project;
    myTarget = target;
  }

  @Override
  public HierarchyKind getKind() {
    return StandardHierarchyKinds.CALL;
  }

  @Override
  public PsiElement getTarget() {
    return myTarget;
  }

  @Override
  public List<HierarchyViewType> getViewTypes() {
    return List.of(StandardHierarchyViewTypes.CALLER, StandardHierarchyViewTypes.CALLEE);
  }

  @RequiredReadAction
  @Override
  public HierarchyViewType getDefaultViewType() {
    return StandardHierarchyViewTypes.CALLER;
  }

  @Override
  public boolean isScopeSupported() {
    return true;
  }

  @RequiredReadAction
  @Override
  public @Nullable HierarchyTreeStructure createTreeStructure(HierarchyRequest<PsiElement> request) {
    PsiElement psiElement = request.getTarget();
    HierarchyViewType viewType = request.getViewType();
    if (StandardHierarchyViewTypes.CALLER.equals(viewType)) {
      return new DartCallerTreeStructure(myProject, psiElement, request.getScope());
    }
    else if (StandardHierarchyViewTypes.CALLEE.equals(viewType)) {
      return new DartCalleeTreeStructure(myProject, psiElement, request.getScope());
    }
    else {
      LOG.error("unexpected type: " + viewType.getId());
      return null;
    }
  }

  @RequiredReadAction
  @Override
  public boolean isApplicableElement(PsiElement element) {
    return DartHierarchyUtil.isExecutable(element);
  }
}
