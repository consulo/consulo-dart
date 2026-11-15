// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.search.SearchableOptionContributor;
import consulo.configurable.search.SearchableOptionProcessor;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
// Consulo: configurable ids of Consulo pages, configurable names are not known here
public final class DartSearchableOptionContributor extends SearchableOptionContributor {
  @Override
  public void processOptions(@NotNull SearchableOptionProcessor processor) {
    processor.addOption(LocalizeValue.of("dart " + DartBundle.message("dialog.paste.on.import.title")),
                        LocalizeValue.of("Dart: " + DartBundle.message("dialog.paste.on.import.title")), "editor.preferences.import.dart", LocalizeValue.empty());
    processor.addOption(LocalizeValue.of(DartBundle.message("checkbox.collapse.parts")),
                        LocalizeValue.of("Code Folding: collapse Dart parts by default"), "editor.preferences.folding.dart", LocalizeValue.empty());
    processor.addOption(LocalizeValue.of(DartBundle.message("checkbox.collapse.generic.parameters")),
                        LocalizeValue.of("Code Folding: collapse Dart generic parameters by default"), "editor.preferences.folding.dart", LocalizeValue.empty());
    processor.addOption(LocalizeValue.of("dart " + DartBundle.message("dart.smartKeys.insertDefaultArgValues.text")),
                        LocalizeValue.of("Dart: " + DartBundle.message("dart.smartKeys.insertDefaultArgValues.text")), "editor.preferences.completion.dart", LocalizeValue.empty());

    processor.addOption(LocalizeValue.of("Dart SDK path"),
                        LocalizeValue.of("Dart SDK path"), "dart.settings", LocalizeValue.empty());
    processor.addOption(LocalizeValue.of("Dart pub webdev server port"),
                        LocalizeValue.of("Dart webdev server port"), "dart.settings", LocalizeValue.empty());
    processor.addOption(LocalizeValue.of(DartBundle.message("code.style.settings.label.line.length")),
                        LocalizeValue.of(DartBundle.message("code.style.settings.label.line.length")), "preferences.sourceCode.Dart", LocalizeValue.empty());
  }
}
