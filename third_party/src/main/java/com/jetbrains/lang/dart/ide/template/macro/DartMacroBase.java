package com.jetbrains.lang.dart.ide.template.macro;

import consulo.language.editor.template.macro.Macro;
import consulo.language.editor.template.context.TemplateContextType;
import com.jetbrains.lang.dart.ide.template.DartTemplateContextType;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;

public abstract class DartMacroBase extends Macro {
  // Consulo: Macro.getPresentableName() is abstract, IntelliJ default is getName() + "()"
  @Override
  public @NotNull LocalizeValue getPresentableName() {
    return LocalizeValue.of(getName() + "()");
  }

  @Override
  public final boolean isAcceptableInContext(final TemplateContextType context) {
    return context instanceof DartTemplateContextType;
  }
}
