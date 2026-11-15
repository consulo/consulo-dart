// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.generation;

import consulo.annotation.component.ActionRefAnchor;
import consulo.annotation.component.ActionRef;
import consulo.annotation.component.ActionParentRef;
import consulo.annotation.component.ActionImpl;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;

@ActionImpl(id = "Generate.SetAccessor.Dart", parents = @ActionParentRef(value = @ActionRef(id = "GenerateGroup"), anchor = ActionRefAnchor.AFTER, relatedToAction = @ActionRef(id = "Generate.GetAccessor.Dart")))
public class DartGenerateSetterAction extends BaseDartGenerateAction {
  public DartGenerateSetterAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Generate.SetAccessor.Dart.text"));
  }

  @Override
  protected @NotNull BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateAccessorHandler(CreateGetterSetterFix.Strategy.SETTER) {
      @Override
      protected @NotNull String getTitle() {
        return DartBundle.message("fields.to.generate.setters");
      }
    };
  }
}
