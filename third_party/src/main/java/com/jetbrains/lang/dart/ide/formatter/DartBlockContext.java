package com.jetbrains.lang.dart.ide.formatter;

import consulo.language.codeStyle.FormattingMode;
import consulo.language.codeStyle.CodeStyleSettings;
import consulo.language.codeStyle.CommonCodeStyleSettings;
import com.jetbrains.lang.dart.DartLanguage;

class DartBlockContext {
  private final CodeStyleSettings mySettings;
  private final FormattingMode myMode;
  private final CommonCodeStyleSettings myDartSettings;

  DartBlockContext(CodeStyleSettings settings, FormattingMode mode) {
    mySettings = settings;
    myMode = mode;
    myDartSettings = settings.getCommonSettings(DartLanguage.INSTANCE);
  }

  public CodeStyleSettings getSettings() {
    return mySettings;
  }

  public CommonCodeStyleSettings getDartSettings() {
    return myDartSettings;
  }

  public FormattingMode getMode() {
    return myMode;
  }
}
