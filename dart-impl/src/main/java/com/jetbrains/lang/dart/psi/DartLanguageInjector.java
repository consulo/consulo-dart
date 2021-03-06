package com.jetbrains.lang.dart.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.jetbrains.lang.dart.DartLanguage;
import javax.annotation.Nonnull;

public class DartLanguageInjector implements LanguageInjector {
  @Override
  public void getLanguagesToInject(@Nonnull PsiLanguageInjectionHost host, @Nonnull InjectedLanguagePlaces injectionPlacesRegistrar) {
    if (host instanceof DartEmbeddedContent) {
      injectionPlacesRegistrar.addPlace(
        DartLanguage.INSTANCE,
        TextRange.create(0, host.getTextLength()),
        null,
        null
      );
    }
  }
}
