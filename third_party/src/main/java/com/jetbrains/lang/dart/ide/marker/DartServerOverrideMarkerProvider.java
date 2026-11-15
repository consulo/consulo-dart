// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.marker;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.language.Language;
import consulo.annotation.component.ExtensionImpl;
import consulo.ui.image.Image;
import consulo.language.editor.DaemonBundle;
import consulo.language.editor.gutter.LineMarkerInfo;
import consulo.language.editor.gutter.LineMarkerProvider;
import consulo.language.editor.ui.navigation.PsiTargetNavigator;
import consulo.application.AllIcons;
import consulo.codeEditor.markup.GutterIconRenderer;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.analyzer.DartServerData;
import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.dartlang.analysis.server.protocol.OverriddenMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@ExtensionImpl
public final class DartServerOverrideMarkerProvider implements LineMarkerProvider {
  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Override
  public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
    if (!(element instanceof DartComponentName)) {
      return null;
    }
    final PsiElement parent = element.getParent();
    if (parent instanceof DartComponent) {
      final DartClass dartClass = PsiTreeUtil.getParentOfType(element, DartClass.class);
      return dartClass == null ? null : createOverrideMarker((DartComponentName)element);
    }
    return null;
  }

  private static void addDartComponent(List<DartComponent> components, Project project, OverriddenMember member) {
    final DartComponent component = member == null ? null : DartHierarchyUtil.findDartComponent(project, member.getElement().getLocation());
    if (component != null) {
      components.add(component);
    }
  }

  private static @Nullable LineMarkerInfo createOverrideMarker(final DartComponentName componentName) {
    final VirtualFile virtualFile = componentName.getContainingFile().getVirtualFile();
    if (virtualFile == null || !virtualFile.isInLocalFileSystem()) {
      return null;
    }

    final List<DartServerData.DartOverrideMember> overrideMembers =
      DartAnalysisServerService.getInstance(componentName.getProject()).getOverrideMembers(virtualFile);
    final Project project = componentName.getProject();
    final int nameOffset = componentName.getTextRange().getStartOffset();
    DartComponent superclassComponent = null;
    List<DartComponent> interfaceComponents = new ArrayList<>();
    for (DartServerData.DartOverrideMember overrideMember : overrideMembers) {
      if (overrideMember.getOffset() == nameOffset) {
        final OverriddenMember member = overrideMember.getSuperclassMember();
        superclassComponent = member == null ? null : DartHierarchyUtil.findDartComponent(project, member.getElement().getLocation());
        if (overrideMember.getInterfaceMembers() != null) {
          for (OverriddenMember overriddenMember : overrideMember.getInterfaceMembers()) {
            addDartComponent(interfaceComponents, project, overriddenMember);
          }
        }
      }
    }
    return tryCreateOverrideMarker(componentName, superclassComponent, interfaceComponents);
  }

    private static @Nullable LineMarkerInfo tryCreateOverrideMarker(final @NotNull DartComponentName componentName,
                                                                    final @Nullable DartComponent superclassComponent,
                                                                    final @NotNull List<DartComponent> interfaceComponents) {
        if (superclassComponent == null && interfaceComponents.isEmpty()) {
            return null;
        }
        final String name = componentName.getName();
        final boolean overrides;
        final DartComponent superComponent;
        if (superclassComponent != null) {
            overrides = true;
            superComponent = superclassComponent;
        }
        else {
            overrides = false;
            superComponent = interfaceComponents.getFirst();
        }

        // Generate the tooltip/accessible name string once
        final String accessibleName;
        final DartClass superClass = PsiTreeUtil.getParentOfType(superComponent, DartClass.class);
        if (superClass == null) {
            accessibleName = "null";
        } else if (overrides) {
            accessibleName = DartBundle.message(superclassComponent.isOperator() ? "overrides.operator.in" : "overrides.method.in", name, superClass.getName());
        } else {
            accessibleName = DartBundle.message("implements.method.in", name, superClass.getName());
        }

        final Image icon = overrides ? AllIcons.Gutter.OverridingMethod : AllIcons.Gutter.ImplementingMethod;
        PsiElement anchor = PsiTreeUtil.getDeepestFirst(componentName);

        return new LineMarkerInfo<>(anchor, anchor.getTextRange(), icon, __ -> accessibleName, (e, __) -> {
            List<DartComponent> superComponents = new ArrayList<>();
            if (superclassComponent != null) {
                superComponents.add(superclassComponent);
            }
            superComponents.addAll(interfaceComponents);
            new PsiTargetNavigator<>(() -> DartResolveUtil.getComponentNames(superComponents))
                    .tabTitle(DaemonBundle.message("navigation.findUsages.title.super.method", name))
                    .navigate(e, DaemonBundle.message("navigation.title.super.method", name), componentName.getProject());
        }, GutterIconRenderer.Alignment.LEFT, () -> accessibleName);
    }

}
