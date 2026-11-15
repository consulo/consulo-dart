// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.surroundWith.statement;

import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;

public class DartWithDoWhileSurrounder extends DartLiteralAndBlockStatementSurrounderBase {
  @Override
  public @NotNull LocalizeValue getTemplateDescription() {
    String description = "do / while";
    return LocalizeValue.of(description);
  }

  @Override
  protected String getTemplateText() {
    return "do{\n} while(true);";
  }
}
