// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.call;

import consulo.content.scope.SearchScope;
import consulo.language.editor.hierarchy.HierarchyScope;
import consulo.find.FindUsagesHandler;
import consulo.language.editor.hierarchy.HierarchyNodeDescriptor;
import consulo.language.editor.hierarchy.HierarchyTreeStructure;
import consulo.project.Project;
import consulo.util.lang.function.Condition;
import consulo.language.psi.PsiElement;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ArrayUtil;
import com.jetbrains.lang.dart.ide.findUsages.DartServerFindUsagesHandler;
import com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DartCallHierarchyTreeStructure extends HierarchyTreeStructure {
  private final @Nullable HierarchyScope myScopeType;

  public DartCallHierarchyTreeStructure(Project project, PsiElement element, @Nullable HierarchyScope currentScopeType) {
    super(project, new DartCallHierarchyNodeDescriptor(null, element, true));
    myScopeType = currentScopeType;
  }

  protected static @NotNull FindUsagesHandler createFindUsageHandler(final @NotNull PsiElement element) {
    return new DartServerFindUsagesHandler(element);
  }

  public static void collectDeclarations(final @Nullable PsiElement element, final @NotNull List<? super PsiElement> results) {
    if (element != null) {
      Condition<PsiElement> isExecutable = object -> {
        if (object == null) return false;
        return DartHierarchyUtil.isExecutable(object);
      };
      PsiElement ref = PsiTreeUtil.findFirstParent(element, isExecutable);
      if (ref != null) {
        results.add(ref);
      }
    }
  }

  protected abstract @NotNull List<PsiElement> getChildren(@NotNull PsiElement element);

  @Override
  protected Object @NotNull [] buildChildren(@NotNull HierarchyNodeDescriptor descriptor) {
    final List<DartCallHierarchyNodeDescriptor> descriptors = new ArrayList<>();
    if (descriptor instanceof DartCallHierarchyNodeDescriptor dartDescriptor) {
      PsiElement element = dartDescriptor.getPsiElement();
      if (element == null) {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
      }
      boolean isCallable = DartHierarchyUtil.isExecutable(element);
      HierarchyNodeDescriptor nodeDescriptor = getBaseDescriptor();
      if (!(element instanceof DartComponent) || !isCallable || nodeDescriptor == null) {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
      }
      DartComponentName name = ((DartComponent)element).getComponentName();
      if (name == null) {
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
      }

      final List<PsiElement> children = getChildren(name);
      final HashMap<PsiElement, DartCallHierarchyNodeDescriptor> callerToDescriptorMap = new HashMap<>();
      PsiElement baseClass = element instanceof DartMethodDeclaration ? PsiTreeUtil.getParentOfType(name, DartClass.class) : null;

      for (PsiElement caller : children) {
        // Consulo: scope is a HierarchyScope, null means all
        if (myScopeType == null || isInScope(baseClass, caller, myScopeType)) {
          DartCallHierarchyNodeDescriptor callerDescriptor = callerToDescriptorMap.get(caller);
          if (callerDescriptor == null) {
            callerDescriptor = new DartCallHierarchyNodeDescriptor(descriptor, caller, false);
            callerToDescriptorMap.put(caller, callerDescriptor);
            descriptors.add(callerDescriptor);
          }
        }
      }
    }
    return ArrayUtil.toObjectArray(descriptors);
  }

  protected @NotNull GlobalSearchScope getScope() {
    // Consulo: the scope is a HierarchyScope instead of the HierarchyBrowserBaseEx.SCOPE_* name
    if (myScopeType == null) {
      return GlobalSearchScope.projectScope(myProject);
    }
    Object root = getRootElement();
    PsiElement element = root instanceof DartCallHierarchyNodeDescriptor rootElement ? rootElement.getPsiElement() : null;
    SearchScope searchScope = myScopeType.getSearchScope(element);
    return searchScope instanceof GlobalSearchScope globalSearchScope ? globalSearchScope : GlobalSearchScope.projectScope(myProject);
  }
}
