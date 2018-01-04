package com.jetbrains.lang.dart.ide.completion;

import static com.intellij.patterns.PlatformPatterns.psiElement;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import consulo.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.util.Function;
import com.intellij.util.ProcessingContext;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.lang.dart.ide.index.DartLibraryIndex;
import com.jetbrains.lang.dart.psi.DartId;
import com.jetbrains.lang.dart.psi.DartLibraryId;
import com.jetbrains.lang.dart.psi.DartPathOrLibraryReference;
import com.jetbrains.lang.dart.psi.DartStringLiteralExpression;
import icons.DartIcons;

public class DartLibraryNameCompletionContributor extends CompletionContributor
{
	public DartLibraryNameCompletionContributor()
	{
		extend(CompletionType.BASIC, psiElement().withSuperParent(2, DartPathOrLibraryReference.class).withParent(DartStringLiteralExpression.class)
				, new CompletionProvider()
		{
			@Override
			public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
			{
				final Project project = parameters.getPosition().getProject();
				final Set<String> names = DartLibraryIndex.getAllLibraryNames(project);
				names.addAll(ContainerUtil.map(DartLibraryIndex.getAllStandardLibrariesFromSdk(parameters.getPosition()), new Function<String, String>()
				{
					@Override
					public String fun(String coreLib)
					{
						return "dart:" + coreLib;
					}
				}));
				names.add("package:");
				for(String libraryName : names)
				{
					if(libraryName.endsWith(".dart"))
					{
						continue;
					}
					result.addElement(new QuotedStringLookupElement(libraryName));
				}
			}
		});
		extend(CompletionType.BASIC, psiElement().withSuperParent(1, DartId.class).withSuperParent(2, DartLibraryId.class),
				new CompletionProvider()
		{
			@Override
			public void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result)
			{
				for(String libraryName : DartLibraryIndex.getAllLibraryNames(parameters.getPosition().getProject()))
				{
					result.addElement(LookupElementBuilder.create(libraryName));
				}
			}
		});
	}

	public static class QuotedStringLookupElement extends LookupElement
	{
		private final String myName;

		public QuotedStringLookupElement(String name)
		{
			myName = name;
		}

		@NotNull
		@Override
		public String getLookupString()
		{
			return myName;
		}

		@Override
		public void renderElement(LookupElementPresentation presentation)
		{
			super.renderElement(presentation);
			presentation.setIcon(DartIcons.Dart);
		}

		@Override
		public void handleInsert(InsertionContext context)
		{
			Document document = context.getDocument();
			int start = context.getStartOffset();
			int end = context.getTailOffset();
			if(start < 1 || end > document.getTextLength() - 1)
			{
				return;
			}
			CharSequence sequence = document.getCharsSequence();
			boolean left = sequence.charAt(start - 1) == sequence.charAt(start);
			boolean right = sequence.charAt(end - 1) == sequence.charAt(end);
			if(left || right)
			{
				document.replaceString(start, end, sequence.subSequence(left ? start + 1 : start, right ? end - 1 : end));
				if(right)
				{
					context.getEditor().getCaretModel().moveCaretRelatively(1, 0, false, false, true);
				}
			}
		}
	}
}