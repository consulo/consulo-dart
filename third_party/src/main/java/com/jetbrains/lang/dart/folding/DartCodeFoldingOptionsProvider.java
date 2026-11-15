// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.folding;

import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ExtensionImpl;
import consulo.configurable.ApplicationConfigurable;
import consulo.configurable.SimpleConfigurableByProperties;
import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.ui.CheckBox;
import consulo.ui.Component;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.layout.VerticalLayout;
import jakarta.inject.Inject;
import jakarta.inject.Provider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Consulo: upstream's {@code CodeFoldingOptionsProvider} (a {@code BeanConfigurable}); the Dart section of Editor > Code Folding is a configurable.
 */
@ExtensionImpl
public final class DartCodeFoldingOptionsProvider extends SimpleConfigurableByProperties implements ApplicationConfigurable {
  private final Provider<DartCodeFoldingSettings> mySettingsProvider;

  @Inject
  public DartCodeFoldingOptionsProvider(@NotNull Provider<DartCodeFoldingSettings> settingsProvider) {
    mySettingsProvider = settingsProvider;
  }

  @Override
  public @NotNull String getId() {
    return "editor.preferences.folding.dart";
  }

  @Override
  public @Nullable String getParentId() {
    return "editor.preferences.folding";
  }

  @Override
  public @NotNull LocalizeValue getDisplayName() {
    return DartBundle.messageValue("dart.title");
  }

  @RequiredUIAccess
  @Override
  protected @NotNull Component createLayout(@NotNull PropertyBuilder propertyBuilder, @NotNull Disposable uiDisposable) {
    VerticalLayout layout = VerticalLayout.create();
    DartCodeFoldingSettings settings = mySettingsProvider.get();
    CheckBox collapseParts = CheckBox.create(DartBundle.messageValue("checkbox.collapse.parts"));
    propertyBuilder.add(collapseParts, settings::isCollapseParts, settings::setCollapseParts);
    layout.add(collapseParts);
    CheckBox collapseGenericParameters = CheckBox.create(DartBundle.messageValue("checkbox.collapse.generic.parameters"));
    propertyBuilder.add(collapseGenericParameters, settings::isCollapseGenericParameters, settings::setCollapseGenericParameters);
    layout.add(collapseGenericParameters);
    return layout;
  }
}
