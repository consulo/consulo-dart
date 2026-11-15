// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.inspections;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.LocalInspectionTool;
import consulo.language.editor.inspection.ProblemHighlightType;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.module.Module;
import consulo.language.util.ModuleUtilCore;
import consulo.application.progress.ProgressIndicatorProvider;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiElementVisitor;
import consulo.language.psi.PsiFileSystemItem;
import consulo.language.psi.PsiReference;
import consulo.language.psi.path.FileReference;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.PubspecYamlReferenceContributor;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import consulo.language.Language;
import org.jetbrains.yaml.YAMLLanguage;
import consulo.localize.LocalizeValue;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;

@ExtensionImpl
public final class DartPathPackageReferenceInspection extends LocalInspectionTool {
  // Consulo: registration attributes from upstream dart-yaml.xml <localInspection>
  @Override
  public @Nullable Language getLanguage() {
    return YAMLLanguage.INSTANCE;
  }

  @Override
  public @NotNull LocalizeValue getDisplayName() {
    return DartBundle.messageValue("path.package.reference.inspection.name");
  }

  @Override
  public @NotNull LocalizeValue getGroupDisplayName() {
    return DartBundle.messageValue("inspections.group.name");
  }

  @Override
  public @NotNull HighlightDisplayLevel getDefaultLevel() {
    return HighlightDisplayLevel.ERROR;
  }

  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, final boolean isOnTheFly) {
    if (!PubspecYamlUtil.PUBSPEC_YAML.equals(holder.getFile().getName())) return super.buildVisitor(holder, isOnTheFly);

    final Module module = ModuleUtilCore.findModuleForPsiElement(holder.getFile());
    final DartSdk sdk = DartSdk.getDartSdk(holder.getProject());
    if (module == null || sdk == null || !DartSdkLibUtil.isDartSdkEnabled(module)) {
      return super.buildVisitor(holder, isOnTheFly);
    }

    return new PsiElementVisitor() {
      @Override
      public void visitElement(final @NotNull PsiElement element) {
        ProgressIndicatorProvider.checkCanceled();

        if (!(element instanceof YAMLKeyValue) ||
            !PubspecYamlReferenceContributor.isPathPackageDefinition((YAMLKeyValue)element)) {
          return;
        }

        YAMLValue yamlValue = ((YAMLKeyValue)element).getValue();
        if (yamlValue == null) {
          return;
        }

        final VirtualFile packageDir = checkReferences(holder, (YAMLKeyValue)element);
        if (packageDir == null) {
          return;
        }

        if (packageDir.findChild(PubspecYamlUtil.PUBSPEC_YAML) == null) {
          final String message = DartBundle.message("pubspec.yaml.not.found.in", FileUtil.toSystemDependentName(packageDir.getPath()));
          holder.registerProblem(yamlValue, message);
          return;
        }

        final VirtualFile file = DartResolveUtil.getRealVirtualFile(element.getContainingFile());
        if (file != null && packageDir.equals(file.getParent())) {
          holder.registerProblem(yamlValue, DartBundle.message("path.package.reference.to.itself"));
        }
      }
    };
  }

  private static @Nullable VirtualFile checkReferences(final @NotNull ProblemsHolder holder, final @NotNull YAMLKeyValue element) {
    for (PsiReference reference : element.getReferences()) {
      if (reference instanceof FileReference && !reference.isSoft()) {
        final PsiFileSystemItem resolve = ((FileReference)reference).resolve();
        if (resolve == null) {
          holder.registerProblem(reference.getElement(), ((FileReference)reference).buildUnresolvedMessage(reference.getCanonicalText()).get(),
                                 ProblemHighlightType.GENERIC_ERROR, reference.getRangeInElement());
          return null;
        }
        else if (((FileReference)reference).isLast()) {
          final VirtualFile dir = resolve.getVirtualFile();
          if (dir != null && dir.isDirectory()) {
            return dir;
          }
        }
      }
    }

    return null;
  }
}
