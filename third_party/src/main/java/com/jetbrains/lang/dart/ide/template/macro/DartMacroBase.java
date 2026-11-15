package com.jetbrains.lang.dart.ide.template.macro;

import consulo.language.editor.template.macro.Macro;
import consulo.language.editor.template.context.TemplateContextType;
import com.jetbrains.lang.dart.ide.template.DartTemplateContextType;

public abstract class DartMacroBase extends Macro {
  @Override
  public final boolean isAcceptableInContext(final TemplateContextType context) {
    return context instanceof DartTemplateContextType;
  }
}
