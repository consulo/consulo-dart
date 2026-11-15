package com.jetbrains.lang.dart.ide.editor;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.codeInsight.DartCodeInsightSettings;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.SimpleConfigurableByProperties;
import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.ui.CheckBox;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Consulo: upstream's {@code CodeCompletionOptionsCustomSection} (Kotlin UI DSL); the Dart section of Editor > Code Completion is a configurable.
 */
@ExtensionImpl
public final class DartSmartKeysConfigurable extends SimpleConfigurableByProperties implements ApplicationConfigurable {
  @Override
  public @NotNull String getId() {
    return "editor.preferences.completion.dart";
  }

  @Override
  public @Nullable String getParentId() {
    return "editor.preferences.completion";
  }

  @Override
  public @NotNull LocalizeValue getDisplayName() {
    return DartBundle.messageValue("dart.title");
  }

  @RequiredUIAccess
  @Override
  protected @NotNull Component createLayout(@NotNull PropertyBuilder propertyBuilder, @NotNull Disposable uiDisposable) {
    DartCodeInsightSettings settings = DartCodeInsightSettings.getInstance();
    VerticalLayout layout = VerticalLayout.create();
    CheckBox insertDefaultArgValues = CheckBox.create(DartBundle.messageValue("dart.smartKeys.insertDefaultArgValues.text"));
    propertyBuilder.add(insertDefaultArgValues, () -> settings.INSERT_DEFAULT_ARG_VALUES, value -> settings.INSERT_DEFAULT_ARG_VALUES = value);
    layout.add(insertDefaultArgValues);
    return layout;
  }
}
