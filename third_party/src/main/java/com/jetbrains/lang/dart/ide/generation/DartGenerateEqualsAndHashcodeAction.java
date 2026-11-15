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

@ActionImpl(id = "Generate.EqualsAndHashcode.Dart", parents = @ActionParentRef(value = @ActionRef(id = "GenerateGroup"), anchor = ActionRefAnchor.AFTER, relatedToAction = @ActionRef(id = "Generate.ToString.Dart")))
public class DartGenerateEqualsAndHashcodeAction extends BaseDartGenerateAction {
  public DartGenerateEqualsAndHashcodeAction() {
    // Consulo: text, description and icon of upstream plugin.xml <action>
    getTemplatePresentation().setText(DartBundle.messageValue("action.Generate.EqualsAndHashcode.Dart.text"));
  }


  private static final String EQUALS_OP = "==";
  private static final String HASHCODE = "hashCode";

  @Override
  protected @NotNull BaseDartGenerateHandler getGenerateHandler() {
    return new DartGenerateEqualsAndHashcodeHandler();
  }

  @Override
  protected boolean doEnable(final @Nullable DartClass dartClass) {
    if (dartClass == null) {
      return false;
    }
    return !doesClassContainEqualsAndHashCode(dartClass);
  }

  public static boolean doesClassContainEqualsAndHashCode(final @NotNull DartClass dartClass) {
    return doesClassContainMethod(dartClass, EQUALS_OP) && doesClassContainGetter(dartClass, HASHCODE);
  }
}
