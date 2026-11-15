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
package com.jetbrains.lang.dart.ide.hierarchy.method;

import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartGetterDeclaration;
import com.jetbrains.lang.dart.psi.DartMethodDeclaration;
import com.jetbrains.lang.dart.psi.DartSetterDeclaration;
import consulo.annotation.access.RequiredReadAction;
import consulo.ide.localize.IdeLocalize;
import consulo.language.editor.localize.LanguageEditorLocalize;
import consulo.language.editor.hierarchy.*;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.logging.Logger;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Consulo replacement of upstream {@code DartMethodHierarchyBrowser}: Consulo builds the hierarchy view from a model.
 */
public class DartMethodHierarchyModel implements HierarchyModel<PsiElement> {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartMethodHierarchyModel.class);

  private static final HierarchyOption HIDE_NOT_IMPLEMENTED = new HierarchyOption(
    "dart.method.hierarchy.hide.not.implemented",
    LanguageEditorLocalize.actionHideNonImplementations(),
    PlatformIconGroup.gutterImplementedmethod(),
    false
  );

  private static final List<HierarchyLegendEntry> LEGEND = List.of(
    new HierarchyLegendEntry(PlatformIconGroup.hierarchyMethoddefined(), IdeLocalize.hierarchyLegendMethodIsDefinedInClass()),
    new HierarchyLegendEntry(PlatformIconGroup.hierarchyMethodnotdefined(), IdeLocalize.hierarchyLegendMethodDefinedInSuperclass()),
    new HierarchyLegendEntry(PlatformIconGroup.hierarchyShoulddefinemethod(), IdeLocalize.hierarchyLegendMethodShouldBeDefined())
  );

  private final Project myProject;
  private final PsiElement myTarget;

  public DartMethodHierarchyModel(Project project, PsiElement target) {
    myProject = project;
    myTarget = target;
  }

  @Override
  public HierarchyKind getKind() {
    return StandardHierarchyKinds.METHOD;
  }

  @Override
  public PsiElement getTarget() {
    return myTarget;
  }

  @Override
  public List<HierarchyViewType> getViewTypes() {
    return List.of(StandardHierarchyViewTypes.METHOD);
  }

  @RequiredReadAction
  @Override
  public HierarchyViewType getDefaultViewType() {
    return StandardHierarchyViewTypes.METHOD;
  }

  @Override
  public List<HierarchyOption> getOptions() {
    return List.of(HIDE_NOT_IMPLEMENTED);
  }

  @Override
  public List<HierarchyLegendEntry> getLegend() {
    return LEGEND;
  }

  @RequiredReadAction
  @Override
  public @Nullable HierarchyTreeStructure createTreeStructure(HierarchyRequest<PsiElement> request) {
    HierarchyViewType viewType = request.getViewType();
    if (!StandardHierarchyViewTypes.METHOD.equals(viewType)) {
      LOG.error("unexpected type: " + viewType.getId());
      return null;
    }
    return new DartMethodHierarchyTreeStructure(myProject, (DartComponent)request.getTarget(), request.isEnabled(HIDE_NOT_IMPLEMENTED));
  }

  @RequiredReadAction
  @Override
  public boolean isApplicableElement(PsiElement element) {
    return (element instanceof DartMethodDeclaration ||
            element instanceof DartGetterDeclaration ||
            element instanceof DartSetterDeclaration) &&
           PsiTreeUtil.getParentOfType(element, DartClass.class) != null;
  }
}
