// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.spelling;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.spellchecker.editor.dictionary.BundledDictionaryProvider;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.function.BiConsumer;

@ExtensionImpl
public final class DartBundledDictionaryProvider implements BundledDictionaryProvider {
  public String[] getBundledDictionaries() {
    return new String[]{"/spelling/dart.dic"};
  }

  // Consulo: dictionaries are passed as streams
  @Override
  public void loadDictionaries(@NotNull BiConsumer<String, InputStream> consumer) {
    for (String dictionary : getBundledDictionaries()) {
      InputStream stream = getClass().getResourceAsStream(dictionary);
      if (stream != null) {
        consumer.accept(dictionary, stream);
      }
    }
  }
}
