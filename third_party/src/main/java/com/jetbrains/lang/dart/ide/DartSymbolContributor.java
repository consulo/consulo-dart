package com.jetbrains.lang.dart.ide;

import consulo.annotation.component.ExtensionImpl;
import consulo.ide.navigation.ChooseByNameContributorEx;
import consulo.navigation.NavigationItem;
import consulo.language.psi.PsiElement;
import consulo.language.psi.SyntaxTraverser;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.application.util.function.Processor;
import consulo.util.collection.JBIterable;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.search.FindSymbolParameters;
import consulo.language.psi.stub.IdFilter;
import com.jetbrains.lang.dart.psi.DartComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.jetbrains.lang.dart.ide.index.DartSymbolIndex.DART_SYMBOL_INDEX;

@ExtensionImpl
public final class DartSymbolContributor implements ChooseByNameContributorEx {
  @Override
  public void processNames(@NotNull Processor<? super String> processor, @NotNull GlobalSearchScope scope, @Nullable IdFilter filter) {
    FileBasedIndex.getInstance().processAllKeys(DART_SYMBOL_INDEX, processor, scope, null);
  }

  @Override
  public void processElementsWithName(@NotNull String name,
                                      @NotNull Processor<? super NavigationItem> processor,
                                      @NotNull FindSymbolParameters parameters) {
    DartClassContributor.doProcessElements(DART_SYMBOL_INDEX, DartSymbolContributor::getComponents,
                                           name, processor, parameters);
  }

  private static JBIterable<DartComponent> getComponents(PsiElement context) {
    return SyntaxTraverser.psiTraverser(context).traverse().filter(DartComponent.class);
  }
}
