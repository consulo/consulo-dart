// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.formatter.settings;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.codeStyle.ui.setting.CodeStyleAbstractConfigurable;
import consulo.language.codeStyle.ui.setting.CodeStyleAbstractPanel;
import consulo.language.Language;
import consulo.configurable.Configurable;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.setting.CodeStyleSettingsCustomizable;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import consulo.language.codeStyle.setting.LanguageCodeStyleSettingsProvider;
import consulo.language.codeStyle.setting.LanguageCodeStyleSettingsProvider.SettingsType;
import com.jetbrains.lang.dart.DartLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {

  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Override
  public @Nullable String getCodeSample(@NotNull SettingsType settingsType) {
    return null;
  }

  // Consulo: no customizeDefaults(), called from getDefaultCommonSettings()
  @Override
  public @Nullable CommonCodeStyleSettings getDefaultCommonSettings() {
    CommonCodeStyleSettings commonSettings = new CommonCodeStyleSettings(getLanguage());
    customizeDefaults(commonSettings, commonSettings.initIndentOptions());
    return commonSettings;
  }

  private static void customizeDefaults(@NotNull CommonCodeStyleSettings commonSettings,
                                   @NotNull CommonCodeStyleSettings.IndentOptions indentOptions) {
    commonSettings.RIGHT_MARGIN = 80;

    indentOptions.INDENT_SIZE = 2;
    indentOptions.CONTINUATION_INDENT_SIZE = 4;
    indentOptions.TAB_SIZE = 2;

    commonSettings.LINE_COMMENT_AT_FIRST_COLUMN = false;
    commonSettings.LINE_COMMENT_ADD_SPACE = true;
  }

  @Override
  public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
    if (settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS) {
      // This line is needed for SupportedFieldCollector. Otherwise the option is wiped out from the xml file that stores settings.
      consumer.showStandardOptions("RIGHT_MARGIN");
    }
  }

  @Override
  public @NotNull Configurable createSettingsPage(@NotNull CodeStyleSettings baseSettings, @NotNull CodeStyleSettings modelSettings) {
    return new CodeStyleAbstractConfigurable(baseSettings, modelSettings, getLanguageName()) {
      @Override
      protected @NotNull CodeStyleAbstractPanel createPanel(@NotNull CodeStyleSettings settings) {
        return new DartCodeStylePanel(getCurrentSettings(), settings);
      }
    };
  }
}
