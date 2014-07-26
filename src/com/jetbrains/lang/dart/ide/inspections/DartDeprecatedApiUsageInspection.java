package com.jetbrains.lang.dart.ide.inspections;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import com.jetbrains.lang.dart.psi.DartFactoryConstructorDeclaration;
import com.jetbrains.lang.dart.psi.DartMethodDeclaration;
import com.jetbrains.lang.dart.psi.DartNamedConstructorDeclaration;
import com.jetbrains.lang.dart.psi.DartReferenceExpression;
import com.jetbrains.lang.dart.psi.DartVisitor;

public class DartDeprecatedApiUsageInspection extends LocalInspectionTool
{

	private static final String DEPRECATED_METADATA = "deprecated";

	@NotNull
	public String getGroupDisplayName()
	{
		return DartBundle.message("inspections.group.name");
	}

	@Nls
	@NotNull
	public String getDisplayName()
	{
		return DartBundle.message("dart.deprecated.api.usage");
	}

	@NotNull
	public HighlightDisplayLevel getDefaultLevel()
	{
		return HighlightDisplayLevel.WEAK_WARNING;
	}

	@NotNull
	public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, final boolean isOnTheFly)
	{
		return new DartVisitor()
		{
			public void visitReferenceExpression(@NotNull final DartReferenceExpression referenceExpression)
			{
				if(PsiTreeUtil.getChildOfType(referenceExpression, DartReferenceExpression.class) != null)
				{
					return;
				}

				final PsiElement referenceParent = referenceExpression.getParent();
				if(referenceParent instanceof DartFactoryConstructorDeclaration || referenceParent instanceof DartNamedConstructorDeclaration)
				{
					return; // no need to highlight constructor declaration
				}

				final PsiElement resolve = referenceExpression.resolve();
				final PsiElement parent = resolve == null ? null : resolve.getParent();
				if(resolve instanceof DartComponentName && (parent instanceof DartComponent))
				{
					if(((DartComponent) parent).getMetadataByName(DEPRECATED_METADATA) != null)
					{
						holder.registerProblem(referenceExpression, DartBundle.message("ref.is.deprecated"), ProblemHighlightType.LIKE_DEPRECATED,
								LocalQuickFix.EMPTY_ARRAY);
					}
					else if(parent instanceof DartMethodDeclaration && ((DartComponent) parent).isConstructor())
					{
						final DartClass dartClass = PsiTreeUtil.getParentOfType(parent, DartClass.class);
						if(dartClass != null && dartClass.getMetadataByName(DEPRECATED_METADATA) != null)
						{
							holder.registerProblem(referenceExpression, DartBundle.message("ref.is.deprecated"),
									ProblemHighlightType.LIKE_DEPRECATED, LocalQuickFix.EMPTY_ARRAY);
						}
					}
				}
			}
		};
	}
}
