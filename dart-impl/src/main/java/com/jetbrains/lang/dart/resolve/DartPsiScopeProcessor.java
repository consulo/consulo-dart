package com.jetbrains.lang.dart.resolve;

import gnu.trove.THashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.jetbrains.lang.dart.ide.index.DartShowHideInfo;
import com.jetbrains.lang.dart.psi.DartComponentName;

public abstract class DartPsiScopeProcessor implements PsiScopeProcessor
{
	private static final Logger LOG = Logger.getInstance(DartResolveProcessor.class.getName());

	private final List<Pair<VirtualFile, DartShowHideInfo>> myShowHideFilters = new ArrayList<Pair<VirtualFile, DartShowHideInfo>>();
	private final Map<VirtualFile, Collection<PsiElement>> myFilteredOutElements = new THashMap<VirtualFile, Collection<PsiElement>>();

	public void importedFileProcessingStarted(final @NotNull VirtualFile importedFile, final @NotNull DartShowHideInfo showHideInfo)
	{
		myShowHideFilters.add(Pair.create(importedFile, showHideInfo));
	}

	public void importedFileProcessingFinished(final @NotNull VirtualFile importedFile)
	{
		LOG.assertTrue(myShowHideFilters.size() > 0, importedFile.getPath());
		final Pair<VirtualFile, DartShowHideInfo> removed = myShowHideFilters.remove(myShowHideFilters.size() - 1);
		LOG.assertTrue(importedFile.equals(removed.first), "expected: " + removed.first.getPath() + ", actual: " + importedFile.getPath());
	}

	public void processFilteredOutElementsForImportedFile(final @NotNull VirtualFile importedFile)
	{
		// removed now, but may be added again in execute();
		final Collection<PsiElement> elements = myFilteredOutElements.remove(importedFile);
		if(elements != null)
		{
			for(PsiElement element : elements)
			{
				execute(element, ResolveState.initial());
			}
		}
	}

	@Override
	public final boolean execute(final @NotNull PsiElement element, final @NotNull ResolveState state)
	{
		if(!(element instanceof DartComponentName))
		{
			return true;
		}

		if(isFilteredOut(((DartComponentName) element).getName()))
		{
			final VirtualFile importedFile = myShowHideFilters.get(myShowHideFilters.size() - 1).first;
			Collection<PsiElement> elements = myFilteredOutElements.get(importedFile);
			if(elements == null)
			{
				elements = new ArrayList<PsiElement>();
				myFilteredOutElements.put(importedFile, elements);
			}
			elements.add(element);

			return true;
		}

		return doExecute((DartComponentName) element);
	}

	protected abstract boolean doExecute(final @NotNull DartComponentName dartComponentName);

	@Override
	public <T> T getHint(@NotNull Key<T> hintKey)
	{
		return null;
	}

	@Override
	public void handleEvent(@NotNull Event event, @Nullable Object associated)
	{
	}

	protected boolean isFilteredOut(final String name)
	{
		for(Pair<VirtualFile, DartShowHideInfo> filter : myShowHideFilters)
		{
			if(isFilteredOut(name, filter.second))
			{
				return true;
			}
		}
		return false;
	}

	private static boolean isFilteredOut(final @Nullable String name, final @NotNull DartShowHideInfo showHideInfo)
	{
		if(showHideInfo.getHideComponents().contains(name))
		{
			return true;
		}
		if(!showHideInfo.getShowComponents().isEmpty() && !showHideInfo.getShowComponents().contains(name))
		{
			return true;
		}
		return false;
	}
}
