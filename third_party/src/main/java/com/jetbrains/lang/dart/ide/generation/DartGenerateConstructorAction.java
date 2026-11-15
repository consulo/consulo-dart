// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.generation;

import consulo.annotation.component.ActionRefAnchor;
import consulo.annotation.component.ActionRef;
import consulo.annotation.component.ActionParentRef;
import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ActionImpl;
import org.jetbrains.annotations.NotNull;

@ActionImpl(id = "Generate.Constructor.Dart", parents = @ActionParentRef(value = @ActionRef(id = "GenerateGroup"), anchor = ActionRefAnchor.FIRST))
public class DartGenerateConstructorAction extends BaseDartGenerateAction {
  public DartGenerateConstructorAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Generate.Constructor.Dart.text"));
  }

  @Override
  protected @NotNull BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateConstructorHandler();
  }
}
