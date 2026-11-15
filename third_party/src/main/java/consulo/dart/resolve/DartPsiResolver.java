// Copyright 2000-2017 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package consulo.dart.resolve;

import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.ide.index.DartImportAndExportIndex;
import com.jetbrains.lang.dart.ide.index.DartImportOrExportInfo;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.resolve.DartPsiScopeProcessor;
import com.jetbrains.lang.dart.resolve.DartResolveProcessor;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.ResolveState;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.project.Project;
import consulo.util.collection.ContainerUtil;
import consulo.util.collection.SmartList;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The PSI based resolve of upstream {@code DartResolver}, removed upstream as dead code in 2017 (commits 504145b15, 1b154850a)
 * when resolution became analysis server driven. In Consulo the analysis server is optional, so references resolve through PSI
 * when the server gives no navigation region.
 */
public final class DartPsiResolver {
  private DartPsiResolver() {
  }

  public static @Nullable List<? extends PsiElement> resolve(@NotNull DartReference reference) {
    if (reference instanceof DartThisExpression) {
      return toResult(PsiTreeUtil.getParentOfType(reference, DartClass.class));
    }
    if (reference instanceof DartParameterNameReferenceExpression) {
      PsiElement target;
      {
        final DartCallExpression callExpression = PsiTreeUtil.getParentOfType(reference, DartCallExpression.class);
        final DartExpression expression = callExpression != null ? callExpression.getExpression() : null;
        target = expression instanceof DartReference ? ((DartReference)expression).resolve() : null;
        target = target != null ? target.getParent() : null;
      }
      if (target == null) {
        final DartNewExpression newExpression = PsiTreeUtil.getParentOfType(reference, DartNewExpression.class);
        if (newExpression != null) {
          target = DartResolveUtil.findConstructorDeclaration(newExpression);
        }
      }
      final DartFormalParameterList parameters = PsiTreeUtil.getChildOfType(target, DartFormalParameterList.class);
      return toResult(findParameterByName(parameters, reference.getText()));
    }
    if (aloneOrFirstInChain(reference)) {
      return resolveSimpleReference(reference);
    }
    final DartReference leftReference = DartResolveUtil.getLeftReference(reference);
    // reference [node, node]
    final DartReference[] references = PsiTreeUtil.getChildrenOfType(reference, DartReference.class);
    if (references != null && references.length == 2) {
      // import prefix
      final List<DartComponentName> result = new SmartList<>();
      final String importPrefix = references[0].getCanonicalText();
      final String componentName = references[1].getCanonicalText();
      processDeclarationsInImportedFileByImportPrefix(reference, importPrefix, new DartResolveProcessor(result, componentName), componentName);
      if (!result.isEmpty()) {
        return result;
      }

      return toResult(references[1].resolve());
    }
    else if (leftReference != null) {
      final DartClassResolveResult classResolveResult = leftReference.resolveDartClass();
      final DartClass dartClass = classResolveResult.getDartClass();
      if (dartClass != null) {
        final String name = reference.getCanonicalText();
        final DartComponent subComponent = leftReference instanceof DartType
                                           ? dartClass.findNamedConstructor(name)
                                           : filterAccess(reference, dartClass.findMembersByName(name));
        return toResult(subComponent == null ? null : subComponent.getComponentName());
      }

      // import prefix
      final List<DartComponentName> result = new SmartList<>();
      final String importPrefix = leftReference.getCanonicalText();
      final String componentName = reference.getCanonicalText();
      processDeclarationsInImportedFileByImportPrefix(reference, importPrefix, new DartResolveProcessor(result, componentName), componentName);
      if (!result.isEmpty()) {
        return result;
      }
    }

    return null;
  }

  private static @NotNull List<PsiElement> toResult(@Nullable PsiElement element) {
    if (element == null) {
      return Collections.emptyList();
    }
    return new SmartList<>(element);
  }

  private static @Nullable DartComponent filterAccess(PsiElement element, List<DartComponent> components) {
    final boolean lValue = DartResolveUtil.isLValue(element);
    return ContainerUtil.find(components, component -> {
      if (lValue && component.isSetter()) {
        return true;
      }
      else if (!lValue && component.isGetter()) {
        return true;
      }
      boolean isGetterOrSetter = component.isSetter() || component.isGetter();
      return !isGetterOrSetter;
    });
  }

  private static List<? extends PsiElement> resolveSimpleReference(@NotNull DartReference reference) {
    final List<? extends PsiElement> result = resolveSimpleReference(reference, reference.getCanonicalText());
    final PsiElement parent = reference.getParent();
    final PsiElement superParent = parent.getParent();
    final boolean isSimpleConstructor = parent instanceof DartType &&
                                        superParent instanceof DartNewExpression &&
                                        ((DartNewExpression)superParent).getReferenceExpression() == null;
    if (!isSimpleConstructor || result.isEmpty()) {
      return result;
    }
    final List<PsiElement> filteredResult = new ArrayList<>(result.size());
    for (PsiElement element : result) {
      final PsiElement elementParent = element.getParent();
      if (element instanceof DartComponentName && elementParent instanceof DartClass) {
        final DartComponent component = ((DartClass)elementParent).findNamedConstructor(reference.getCanonicalText());
        if (component != null && DartComponentType.typeOf(component) == DartComponentType.CONSTRUCTOR) {
          filteredResult.add(component.getComponentName());
          continue;
        }
      }
      filteredResult.add(element);
    }
    return filteredResult;
  }

  public static @NotNull List<? extends PsiElement> resolveSimpleReference(final @NotNull PsiElement scopeElement, final @NotNull String name) {
    final List<DartComponentName> result = new ArrayList<>();
    // local
    final DartResolveProcessor dartResolveProcessor = new DartResolveProcessor(result, name, DartResolveUtil.isLValue(scopeElement));
    PsiTreeUtil.treeWalkUp(dartResolveProcessor, scopeElement, null, ResolveState.initial());

    // supers
    final DartClass dartClass = PsiTreeUtil.getParentOfType(scopeElement, DartClass.class);
    final boolean inClass = PsiTreeUtil.getParentOfType(scopeElement, DartClassBody.class, false) != null;
    if (result.isEmpty() && dartClass != null && inClass) {
      final DartComponent field = filterAccess(scopeElement, dartClass.findMembersByName(name));
      if (field != null) {
        return toResult(field.getComponentName());
      }
    }

    // global
    if (result.isEmpty()) {
      final List<VirtualFile> libraryFiles = DartResolveUtil.findLibrary(scopeElement.getContainingFile());
      DartResolveUtil.processTopLevelDeclarations(scopeElement, dartResolveProcessor, libraryFiles, name);
    }

    return result;
  }

  // removed upstream from DartResolveUtil together with the PSI resolve

  public static boolean aloneOrFirstInChain(DartReference reference) {
    return PsiTreeUtil.getChildrenOfType(reference, DartReference.class) == null &&
           DartResolveUtil.getLeftReference(reference) == null &&
           DartResolveUtil.getLeftReference(reference.getParent()) == null;
  }

  public static @Nullable DartComponentName findParameterByName(@Nullable DartFormalParameterList parameterList, final String name) {
    if (parameterList == null) {
      return null;
    }

    for (DartNormalFormalParameter parameter : parameterList.getNormalFormalParameterList()) {
      final DartComponentName componentName = parameter.findComponentName();
      if (componentName != null && name.equals(componentName.getText())) return componentName;
    }

    final DartOptionalFormalParameters optionalFormalParameters = parameterList.getOptionalFormalParameters();
    if (optionalFormalParameters == null) return null;

    for (DartDefaultFormalNamedParameter namedParameter : optionalFormalParameters.getDefaultFormalNamedParameterList()) {
      final DartComponentName parameterName = namedParameter.getNormalFormalParameter().findComponentName();
      if (parameterName != null && name.equals(parameterName.getText())) return parameterName;
    }
    return null;
  }

  public static boolean processDeclarationsInImportedFileByImportPrefix(final @NotNull PsiElement context,
                                                                        final @NotNull String importPrefix,
                                                                        final @NotNull DartPsiScopeProcessor processor,
                                                                        final @Nullable String componentNameHint) {
    final Project project = context.getProject();

    for (VirtualFile virtualFile : DartResolveUtil.findLibrary(context.getContainingFile())) {
      for (DartImportOrExportInfo importOrExportInfo : DartImportAndExportIndex.getImportAndExportInfos(project, virtualFile)) {
        if (importOrExportInfo.getKind() == DartImportOrExportInfo.Kind.Import && importPrefix.equals(importOrExportInfo.getImportPrefix())) {
          final VirtualFile importedFile = DartResolveUtil.getImportedFile(project, virtualFile, importOrExportInfo.getUri());
          if (importedFile != null) {
            processor.importedFileProcessingStarted(importedFile, importOrExportInfo);
            final boolean continueProcessing = DartResolveUtil.processTopLevelDeclarations(context, processor, importedFile, componentNameHint);
            processor.importedFileProcessingFinished(importedFile);

            if (!continueProcessing) {
              return false;
            }
          }
        }
      }
    }

    return true;
  }
}
