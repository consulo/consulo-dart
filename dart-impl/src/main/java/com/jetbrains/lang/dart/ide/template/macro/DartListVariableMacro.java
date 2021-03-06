package com.jetbrains.lang.dart.ide.template.macro;

import com.jetbrains.lang.dart.psi.DartClass;
import javax.annotation.Nonnull;

/**
 * @author: Fedor.Korotkov
 */
public class DartListVariableMacro extends DartFilterByClassMacro {
  @Override
  public String getName() {
    return "dartListVariable";
  }

  @Override
  public String getPresentableName() {
    return "dartListVariable()";
  }

  @Override
  protected boolean filter(@Nonnull DartClass dartClass) {
    return dartClass.findMemberByName("length") != null && dartClass.findOperator("[]", null) != null;
  }
}
