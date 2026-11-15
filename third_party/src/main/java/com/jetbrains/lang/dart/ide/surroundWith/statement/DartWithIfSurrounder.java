// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.surroundWith.statement;



public class DartWithIfSurrounder extends DartLiteralAndBlockStatementSurrounderBase {
  @Override
  public @NotNull LocalizeValue getTemplateDescription() {
    String description = "if";
    return LocalizeValue.of(description);
  }

  @Override
  protected String getTemplateText() {
    return "if(true) {\n}";
  }
}
