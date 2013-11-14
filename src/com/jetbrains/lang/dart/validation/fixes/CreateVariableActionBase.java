package com.jetbrains.lang.dart.validation.fixes;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.TemplateManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.util.CommonRefactoringUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartReference;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;

abstract public class CreateVariableActionBase extends BaseCreateFix
{
	private static final Logger LOG = Logger.getInstance(CreateVariableActionBase.class);
	protected final String myName;
	protected final boolean myStatic;

	public CreateVariableActionBase(String name, boolean isStatic)
	{
		myName = name;
		myStatic = isStatic;
	}

	@NotNull
	public String getFamilyName()
	{
		return DartBundle.message("dart.create.function.intention.family");
	}

	@Override
	protected boolean isAvailable(Project project, PsiElement element, Editor editor, PsiFile file)
	{
		if(PsiTreeUtil.getParentOfType(myElement, DartReference.class) == null)
		{
			return false;
		}
		final PsiElement anchor = findAnchor(element);
		return anchor != null && !isInDartSdkOrDartPackagesFolder(anchor.getContainingFile());
	}

	@Override
	protected void applyFix(Project project, @NotNull PsiElement psiElement, Editor editor)
	{
		final TemplateManager templateManager = TemplateManager.getInstance(project);
		Template template = templateManager.createTemplate("", "");
		template.setToReformat(true);

		buildTemplate(template, psiElement);

		PsiElement anchor = findAnchor(psiElement);

		if(anchor == null)
		{
			CommonRefactoringUtil.showErrorHint(project, editor, DartBundle.message("dart.create.function.intention.family"), DartBundle.message("dart.cannot.find.place.to.create"), null);
			return;
		}

		final Editor openedEditor = navigate(project, anchor.getTextOffset(), anchor.getContainingFile().getVirtualFile());
		if(openedEditor != null)
		{
			templateManager.startTemplate(openedEditor, template);
		}
	}

	protected void buildTemplate(Template template, PsiElement element)
	{
		if(myStatic)
		{
			template.addTextSegment("static ");
		}
		DartClass dartClass = DartResolveUtil.suggestType(element);
		if(dartClass == null)
		{
			template.addVariable(DartPresentableUtil.getExpression("var"), true);
		}
		else
		{
			template.addTextSegment(StringUtil.notNullize(dartClass.getName()));
		}
		template.addTextSegment(" ");
		template.addTextSegment(myName);
		template.addTextSegment(";\n");
	}
}
