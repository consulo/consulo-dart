// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.language.psi.PsiElement;
import consulo.language.psi.resolve.PsiElementProcessor;
import consulo.language.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * {@code PsiTreeUtil} methods of IntelliJ which Consulo does not have.
 */
public final class PsiTreeUtilCompat {
  private PsiTreeUtilCompat() {
  }

  @SafeVarargs
  public static @NotNull <T extends PsiElement> List<T> getChildrenOfAnyType(@Nullable PsiElement element, @NotNull Class<? extends T> @NotNull ... classes) {
    List<T> result = null;
    if (element != null) {
      for (PsiElement child = element.getFirstChild(); child != null; child = child.getNextSibling()) {
        if (PsiTreeUtil.instanceOf(child, classes)) {
          if (result == null) result = new ArrayList<>();
          //noinspection unchecked
          result.add((T)child);
        }
      }
    }
    return result != null ? result : Collections.emptyList();
  }

  public static @Nullable <T extends PsiElement> T findChildOfType(@Nullable PsiElement element,
                                                                   @NotNull Class<T> aClass,
                                                                   boolean strict,
                                                                   @Nullable Class<? extends PsiElement> stopAt) {
    if (element == null) return null;

    PsiElementProcessor.FindElement<PsiElement> processor = new PsiElementProcessor.FindElement<PsiElement>() {
      @Override
      public boolean execute(@NotNull PsiElement each) {
        if (strict && each == element) return true;
        if (aClass.isInstance(each)) return setFound(each);
        return stopAt == null || !stopAt.isInstance(each);
      }
    };
    PsiTreeUtil.processElements(element, processor);
    return aClass.cast(processor.getFoundElement());
  }
}
