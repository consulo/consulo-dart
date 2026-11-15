// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.generation;

import consulo.language.editor.template.Template;
import consulo.language.editor.template.TemplateManager;
import consulo.language.editor.template.TextExpression;
import consulo.codeEditor.Editor;
import consulo.project.Project;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Set;

public class CreateNamedConstructorFix extends BaseCreateMethodsFix<DartComponent> {
  public CreateNamedConstructorFix(final @NotNull DartClass dartClass) {
    super(dartClass);
  }

  @Override
  protected void processElements(final @NotNull Project project,
                                 final @NotNull Editor editor,
                                 final @NotNull Set<DartComponent> elementsToProcess) {
    final TemplateManager templateManager = TemplateManager.getInstance(project);
    anchor = doAddMethodsForOne(editor, templateManager, buildFunctionsText(templateManager, elementsToProcess), anchor);
  }

  @Override
  protected @NotNull String getCommandName() {
    return DartBundle.message("dart.generate.named.constructor");
  }

  @Override
  protected @NotNull String getNothingFoundMessage() {
    return ""; // can't be called actually because processElements() is overridden
  }

  protected Template buildFunctionsText(TemplateManager templateManager, Set<DartComponent> elementsToProcess) {
    final Template template = templateManager.createTemplate(getClass().getName(), DART_TEMPLATE_GROUP);
    template.setToReformat(true);

    //noinspection ConstantConditions
    template.addTextSegment(myDartClass.getName());
    template.addTextSegment(".");
    template.addVariable(new TextExpression("name"), true);
    template.addTextSegment("(");
    for (Iterator<DartComponent> iterator = elementsToProcess.iterator(); iterator.hasNext(); ) {
      DartComponent component = iterator.next();
      template.addTextSegment("this.");
      //noinspection ConstantConditions
      template.addTextSegment(component.getName());
      if (iterator.hasNext()) {
        template.addTextSegment(",");
      }
    }
    template.addTextSegment(");");
    template.addEndVariable();
    template.addTextSegment(" "); // trailing space is removed when auto-reformatting, but it helps to enter line break if needed
    return template;
  }

  @Override
  protected Template buildFunctionsText(TemplateManager templateManager, DartComponent e) {
    // ignore
    return null;
  }
}
