package com.jetbrains.lang.dart.ide.generation;

import java.util.Iterator;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;

/**
 * Created by fedorkorotkov.
 */
public class CreateConstructorFix extends BaseCreateMethodsFix<DartComponent> {
	public CreateConstructorFix(DartClass dartClass) {
		super(dartClass);
	}

	@Override
	protected void processElements(@NotNull Project project, @NotNull Editor editor, Set<DartComponent> elementsToProcess) {
		final TemplateManager templateManager = TemplateManager.getInstance(project);
		anchor = doAddMethodsForOne(editor, templateManager, buildFunctionsText(templateManager, elementsToProcess), anchor);
	}

	protected Template buildFunctionsText(TemplateManager templateManager, Set<DartComponent> elementsToProcess) {
		final Template template = templateManager.createTemplate(getClass().getName(), DART_TEMPLATE_GROUP);
		template.setToReformat(true);

		//noinspection ConstantConditions
		template.addTextSegment(myDartClass.getName());
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
		template.addTextSegment(")");
		template.addTextSegment("{\n");
		template.addEndVariable();
		template.addTextSegment("\n}\n");
		return template;
	}

	@Override
	protected Template buildFunctionsText(TemplateManager templateManager, DartComponent e) {
		// ignore
		return null;
	}
}
