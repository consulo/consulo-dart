// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.editor;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.codeInsight.DartCodeInsightSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.localize.ApplicationLocalize;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.SimpleConfigurableByProperties;
import consulo.disposer.Disposable;
import consulo.language.editor.CodeInsightSettings;
import consulo.localize.LocalizeValue;
import consulo.ui.ComboBox;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import consulo.ui.util.LabeledBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Consulo: upstream's {@code AutoImportOptionsProvider} (Kotlin UI DSL); the Dart section of Editor > Auto Import is a configurable.
 */
@ExtensionImpl
public final class DartAutoImportOptionsProvider extends SimpleConfigurableByProperties implements ApplicationConfigurable {
  @Override
  public @NotNull String getId() {
    return "editor.preferences.import.dart";
  }

  @Override
  public @Nullable String getParentId() {
    return "editor.preferences.import";
  }

  @Override
  public @NotNull LocalizeValue getDisplayName() {
    return DartBundle.messageValue("dart.title");
  }

  @RequiredUIAccess
  @Override
  protected @NotNull Component createLayout(@NotNull PropertyBuilder propertyBuilder, @NotNull Disposable uiDisposable) {
    DartCodeInsightSettings settings = DartCodeInsightSettings.getInstance();
    ComboBox.Builder<Integer> builder = ComboBox.<Integer>builder()
      .add(CodeInsightSettings.YES, ApplicationLocalize.comboboxInsertImportsAll())
      .add(CodeInsightSettings.ASK, ApplicationLocalize.comboboxInsertImportsAsk())
      .add(CodeInsightSettings.NO, ApplicationLocalize.comboboxInsertImportsNone());
    int current = settings.ADD_IMPORTS_ON_PASTE;
    if (current != CodeInsightSettings.YES && current != CodeInsightSettings.ASK && current != CodeInsightSettings.NO) {
      builder.add(current, LocalizeValue.empty());
    }
    ComboBox<Integer> comboBox = builder.build();
    propertyBuilder.add(comboBox, () -> settings.ADD_IMPORTS_ON_PASTE, value -> settings.ADD_IMPORTS_ON_PASTE = value);

    VerticalLayout layout = VerticalLayout.create();
    layout.add(LabeledBuilder.sided(DartBundle.messageValue("label.update.imports.on.paste"), comboBox));
    return layout;
  }
}
