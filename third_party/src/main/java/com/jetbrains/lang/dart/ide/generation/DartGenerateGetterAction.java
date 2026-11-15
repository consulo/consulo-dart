// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.generation;

import consulo.annotation.component.ActionRefAnchor;
import consulo.annotation.component.ActionRef;
import consulo.annotation.component.ActionParentRef;
import consulo.annotation.component.ActionImpl;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;

@ActionImpl(id = "Generate.GetAccessor.Dart", parents = @ActionParentRef(value = @ActionRef(id = "GenerateGroup"), anchor = ActionRefAnchor.AFTER, relatedToAction = @ActionRef(id = "Generate.Named.Constructor.Dart")))
public class DartGenerateGetterAction extends BaseDartGenerateAction {
  public DartGenerateGetterAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Generate.GetAccessor.Dart.text"));
  }

  @Override
  protected @NotNull BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateAccessorHandler(CreateGetterSetterFix.Strategy.GETTER) {
      @Override
      protected @NotNull String getTitle() {
        return DartBundle.message("fields.to.generate.getters");
      }
    };
  }
}
