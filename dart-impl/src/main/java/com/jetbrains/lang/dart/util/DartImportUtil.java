package com.jetbrains.lang.dart.util;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.Nls;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.DartTokenTypesSets;
import com.jetbrains.lang.dart.psi.DartHideCombinator;
import com.jetbrains.lang.dart.psi.DartImportStatement;
import com.jetbrains.lang.dart.psi.DartLibraryComponentReferenceExpression;
import com.jetbrains.lang.dart.psi.DartLibraryStatement;
import com.jetbrains.lang.dart.psi.DartShowCombinator;
import com.jetbrains.lang.dart.resolve.DartResolver;

public class DartImportUtil
{
	private DartImportUtil()
	{
	}

	public static void insertImport(@Nonnull PsiFile context, @Nls String componentName, @Nonnull String libraryId)
	{
		final PsiManager psiManager = context.getManager();
		libraryRootLoop:
		for(VirtualFile libraryRoot : DartResolveUtil.findLibrary(context))
		{
			final PsiFile file = psiManager.findFile(libraryRoot);
			if(file == null)
			{
				continue;
			}
			final DartImportStatement[] importStatements = PsiTreeUtil.getChildrenOfType(file, DartImportStatement.class);
			if(importStatements != null)
			{
				for(DartImportStatement importStatement : importStatements)
				{
					final PsiElement importTarget = importStatement.getLibraryExpression().resolve();
					if(importTarget != null && DartResolver.resolveSimpleReference(importTarget, componentName) != null)
					{
						addShowOrRemoveHide(importStatement, componentName);
						continue libraryRootLoop;
					}
				}
			}

			final PsiElement toAdd = DartElementGenerator.createTopLevelStatementFromText(file.getProject(), "import '" + libraryId + "';");
			if(toAdd != null)
			{
				final PsiElement anchor = findAnchorForImportStatement(file, importStatements);
				if(anchor == null)
				{
					final PsiElement child = file.getFirstChild();
					file.addBefore(toAdd, child);
					if(!(child instanceof PsiWhiteSpace))
					{
						file.getNode().addLeaf(DartTokenTypesSets.WHITE_SPACE, "\n", child.getNode());
					}
				}
				else
				{
					file.addAfter(toAdd, anchor);
				}
			}
		}
	}

	private static void addShowOrRemoveHide(@Nonnull DartImportStatement importStatement, String componentName)
	{
		// try to remove hide
		for(DartHideCombinator hideCombinator : importStatement.getHideCombinatorList())
		{
			final List<DartLibraryComponentReferenceExpression> libraryComponents = hideCombinator.getLibraryReferenceList()
					.getLibraryComponentReferenceExpressionList();
			for(DartLibraryComponentReferenceExpression libraryComponentReferenceExpression : libraryComponents)
			{
				if(componentName.equals(libraryComponentReferenceExpression.getText()))
				{
					final PsiElement toRemove = libraryComponents.size() > 1 ? libraryComponentReferenceExpression : hideCombinator;
					toRemove.delete();
					return;
				}
			}
		}

		// add show
		final List<DartShowCombinator> showCombinators = importStatement.getShowCombinatorList();
		if(showCombinators.isEmpty())
		{
			// something wrong
			return;
		}
		final DartShowCombinator combinatoroToAdd = showCombinators.iterator().next();
		final DartLibraryComponentReferenceExpression libraryComponentReference = DartElementGenerator.createLibraryComponentReference
				(importStatement.getProject(), componentName);
		if(libraryComponentReference != null)
		{
			combinatoroToAdd.getLibraryReferenceList().getNode().addLeaf(DartTokenTypes.COMMA, ",", null);
			combinatoroToAdd.getLibraryReferenceList().getNode().addLeaf(DartTokenTypesSets.WHITE_SPACE, " ", null);
			combinatoroToAdd.getLibraryReferenceList().add(libraryComponentReference);
		}
	}

	private static PsiElement findAnchorForImportStatement(@Nonnull PsiFile psiFile, @Nullable DartImportStatement[] importStatements)
	{
		if(importStatements != null && importStatements.length > 0)
		{
			return importStatements[importStatements.length - 1];
		}
		return PsiTreeUtil.getChildOfType(psiFile, DartLibraryStatement.class);
	}
}
