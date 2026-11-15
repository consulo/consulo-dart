// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide;

import consulo.annotation.component.ExtensionImpl;
import consulo.ide.navigation.ChooseByNameContributorEx;
import consulo.navigation.NavigationItem;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.content.scope.SearchScope;
import java.util.function.Function;
import consulo.application.util.function.Processor;
import consulo.util.collection.JBIterable;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.search.FindSymbolParameters;
import consulo.index.io.ID;
import consulo.language.psi.stub.IdFilter;
import com.jetbrains.lang.dart.ide.index.DartClassIndex;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Objects;

@ExtensionImpl
public final class DartClassContributor implements ChooseByNameContributorEx {
  @Override
  public void processNames(@NotNull Processor<String> processor, @NotNull SearchScope scope, @Nullable IdFilter filter) {
    FileBasedIndex.getInstance().processAllKeys(DartClassIndex.DART_CLASS_INDEX, processor, scope, filter);
  }

  @Override
  public void processElementsWithName(@NotNull String name,
                                      @NotNull Processor<NavigationItem> processor,
                                      @NotNull FindSymbolParameters parameters) {
    doProcessElements(DartClassIndex.DART_CLASS_INDEX, DartResolveUtil::getClassDeclarations, name, processor, parameters);
  }

  static void doProcessElements(@NotNull ID<String, Void> index,
                                @NotNull Function<PsiElement, Iterable<? extends DartComponent>> componentGetter,
                                @NotNull String name,
                                @NotNull Processor<NavigationItem> processor,
                                @NotNull FindSymbolParameters parameters) {
    PsiManager psiManager = PsiManager.getInstance(parameters.getProject());
    FileBasedIndex.getInstance().getFilesWithKey(
      index, Collections.singleton(name), file ->
        JBIterable.of(psiManager.findFile(file))
          .filter(psiFile -> psiFile instanceof DartFile)
          .flatMap(componentGetter)
          .filter(o -> Objects.equals(name, o.getName()))
          .filterMap(DartComponent::getComponentName)
          .processEach(processor), parameters.getSearchScope());
  }
}
