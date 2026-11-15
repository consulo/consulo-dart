// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.generation;

import consulo.annotation.component.ActionRefAnchor;
import consulo.annotation.component.ActionRef;
import consulo.annotation.component.ActionParentRef;
import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ActionImpl;
import com.jetbrains.lang.dart.psi.DartClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ActionImpl(id = "Generate.ToString.Dart", parents = @ActionParentRef(value = @ActionRef(id = "GenerateGroup"), anchor = ActionRefAnchor.AFTER, relatedToAction = @ActionRef(id = "Generate.GetSetAccessor.Dart")))
public class DartGenerateToStringAction extends BaseDartGenerateAction {
  public DartGenerateToStringAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Generate.ToString.Dart.text"));
  }


  private static final String TO_STRING = "toString";

  @Override
  protected @NotNull BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateToStringHandler();
  }

  @Override
  protected boolean doEnable(@Nullable DartClass dartClass) {
    if (dartClass == null) {
      return false;
    }
    return !doesClassContainMethod(dartClass, TO_STRING);
  }
}
