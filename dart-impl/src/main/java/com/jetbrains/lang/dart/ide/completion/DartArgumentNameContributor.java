package com.jetbrains.lang.dart.ide.completion;

import static com.intellij.patterns.PlatformPatterns.psiElement;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import consulo.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.jetbrains.lang.dart.psi.*;

public class DartArgumentNameContributor extends CompletionContributor
{
	public DartArgumentNameContributor()
	{
		final PsiElementPattern.Capture<PsiElement> idInExpression = psiElement().withSuperParent(1, DartId.class).withSuperParent(2,
				DartReference.class);
		extend(CompletionType.BASIC, idInExpression.withSuperParent(3, DartArgumentList.class), new CompletionProvider()
		{
			@Override
			public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
			{
				DartExpression reference = findExpressionFromCallOrNew(parameters);
				PsiElement target = reference instanceof DartReference ? ((DartReference) reference).resolve() : null;
				PsiElement targetComponent = target != null ? target.getParent() : null;
				DartFormalParameterList parameterList = PsiTreeUtil.getChildOfType(targetComponent, DartFormalParameterList.class);
				if(parameterList != null)
				{
					for(DartNormalFormalParameter parameter : parameterList.getNormalFormalParameterList())
					{
						final DartComponentName componentName = parameter.findComponentName();
						if(componentName != null)
						{
							addParameterName(result, componentName.getName());
						}
					}
					DartNamedFormalParameters namedFormalParameters = parameterList.getNamedFormalParameters();
					List<DartDefaultFormalNamedParameter> namedParameterList = namedFormalParameters != null ? namedFormalParameters
							.getDefaultFormalNamedParameterList() : Collections.<DartDefaultFormalNamedParameter>emptyList();
					for(DartDefaultFormalNamedParameter parameterDescription : namedParameterList)
					{
						final DartComponentName componentName = parameterDescription.getNormalFormalParameter().findComponentName();
						if(componentName != null)
						{
							addParameterName(result, componentName.getName());
						}
					}
				}
			}

			private void addParameterName(CompletionResultSet result, @Nullable String parameterName)
			{
				if(parameterName != null)
				{
					result.addElement(LookupElementBuilder.create(parameterName));
				}
			}
		});
	}

	@Nullable
	private static DartExpression findExpressionFromCallOrNew(CompletionParameters parameters)
	{
		DartCallExpression callExpression = PsiTreeUtil.getParentOfType(parameters.getPosition(), DartCallExpression.class);
		if(callExpression != null)
		{
			return callExpression.getExpression();
		}
		DartNewExpression newExpression = PsiTreeUtil.getParentOfType(parameters.getPosition(), DartNewExpression.class);
		if(newExpression != null)
		{
			final DartExpression expression = newExpression.getReferenceExpression();
			if(expression != null)
			{
				return expression;
			}
			final DartType type = newExpression.getType();
			return type != null ? type.getReferenceExpression() : null;
		}
		return null;
	}
}
