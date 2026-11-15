// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.hierarchy.method;

import consulo.language.editor.hierarchy.HierarchyNodeDescriptor;
import consulo.language.editor.hierarchy.HierarchyTreeStructure;
import consulo.project.Project;
import consulo.language.psi.SmartPointerManager;
import consulo.language.psi.SmartPsiElementPointer;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.util.collection.ArrayUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import org.dartlang.analysis.server.protocol.TypeHierarchyItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil.findDartClass;
import static com.jetbrains.lang.dart.ide.hierarchy.DartHierarchyUtil.getTypeHierarchyItems;

public class DartMethodHierarchyTreeStructure extends HierarchyTreeStructure {
  private final SmartPsiElementPointer<DartComponent> myMethod;
  // Consulo: HierarchyBrowserManager state is internal, the option comes from DartMethodHierarchyModel
  private final boolean myHideClassesWhereMethodNotImplemented;

  public DartMethodHierarchyTreeStructure(Project project, DartComponent element) {
    this(project, element, false);
  }

  public DartMethodHierarchyTreeStructure(Project project, DartComponent element, boolean hideClassesWhereMethodNotImplemented) {
    super(project, null);
    myHideClassesWhereMethodNotImplemented = hideClassesWhereMethodNotImplemented;
    DartClass baseClass = PsiTreeUtil.getParentOfType(element, DartClass.class);
    myBaseDescriptor = new DartMethodHierarchyNodeDescriptor(project, null, baseClass, true, this);
    setBaseElement(myBaseDescriptor);
    ((DartMethodHierarchyNodeDescriptor)myBaseDescriptor).setTreeStructure(this);
    myMethod = SmartPointerManager.getInstance(myProject).createSmartPsiElementPointer(element);
  }

  @Override
  protected Object @NotNull [] buildChildren(@NotNull HierarchyNodeDescriptor descript) {
    final DartMethodHierarchyNodeDescriptor descriptor = (DartMethodHierarchyNodeDescriptor)descript;
    final DartClass dartClass = descriptor.getType();
    if (dartClass == null) return ArrayUtil.EMPTY_OBJECT_ARRAY;

    final List<TypeHierarchyItem> items = getTypeHierarchyItems(dartClass);
    if (items.isEmpty()) return ArrayUtil.EMPTY_OBJECT_ARRAY;

    addAllVisibleSubclasses(new HashSet<>(), myProject, items, items.getFirst(), descriptor);
    return descriptor.getCachedChildren();
  }

  private void addAllVisibleSubclasses(final @NotNull Set<TypeHierarchyItem> stackItems,
                                       final @NotNull Project project,
                                       final @NotNull List<TypeHierarchyItem> items,
                                       final @NotNull TypeHierarchyItem item,
                                       final @NotNull DartMethodHierarchyNodeDescriptor descriptor) {
    if (!stackItems.add(item)) {
      descriptor.setCachedChildren(ArrayUtil.EMPTY_OBJECT_ARRAY);
      return;
    }

    List<DartMethodHierarchyNodeDescriptor> subDescriptors = new ArrayList<>();
    try {
      for (int index : item.getSubclasses()) {
        final TypeHierarchyItem subItem = items.get(index);
        final DartClass subclass = findDartClass(project, subItem);
        if (subclass != null) {
          final DartMethodHierarchyNodeDescriptor subDescriptor =
            new DartMethodHierarchyNodeDescriptor(project, descriptor, subclass, false, this);
          subDescriptors.add(subDescriptor);
          addAllVisibleSubclasses(stackItems, project, items, subItem, subDescriptor);
        }
      }
      DartClass dartClass = findDartClass(project, item);
      assert dartClass != null;
      String methodName = getBaseMethod().getName();
      if (methodName != null) {
        DartComponent method = dartClass.findMethodByName(methodName);
        if (method != null) {
          DartClass definingClass = PsiTreeUtil.getParentOfType(method, DartClass.class);
          if (definingClass == dartClass) {
            descriptor.myIsImplementor = true;
          }
          else {
            descriptor.myShouldImplement = method.isAbstract() && !dartClass.isAbstract();
          }
        }
      }
      for (DartMethodHierarchyNodeDescriptor subDescriptor : subDescriptors) {
        if (subDescriptor.myIsSuperclassOfImplementor || subDescriptor.myIsImplementor) {
          descriptor.myIsSuperclassOfImplementor = true;
          break;
        }
      }
      if (myHideClassesWhereMethodNotImplemented) {
        List<DartMethodHierarchyNodeDescriptor> toRemove = new ArrayList<>();
        for (DartMethodHierarchyNodeDescriptor subDescriptor : subDescriptors) {
          if (!(subDescriptor.myIsSuperclassOfImplementor || subDescriptor.myIsImplementor)) {
            toRemove.add(subDescriptor);
          }
        }
        subDescriptors.removeAll(toRemove);
      }
    }
    finally {
      stackItems.remove(item);
    }
    descriptor.setCachedChildren(subDescriptors.toArray(HierarchyNodeDescriptor.EMPTY_ARRAY));
  }

  private DartComponent getBaseMethod() {
    return myMethod.getElement();
  }
}
