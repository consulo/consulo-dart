package com.jetbrains.lang.dart.ide.inspections.analyzer;

import com.google.dart.engine.context.AnalysisContext;
import com.google.dart.engine.error.AnalysisError;
import com.intellij.analysis.AnalysisScope;
import com.intellij.codeInspection.GlobalInspectionContext;
import com.intellij.codeInspection.ex.InspectionToolWrapper;
import com.intellij.codeInspection.ex.Tools;
import com.intellij.codeInspection.lang.GlobalInspectionContextExtension;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.util.ProgressWrapper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NullableComputable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.analyzer.DartFileBasedSource;
import com.jetbrains.lang.dart.analyzer.DartInProcessAnnotator;
import consulo.util.dataholder.Key;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DartGlobalInspectionContext implements GlobalInspectionContextExtension<DartGlobalInspectionContext>
{
	static final Key<DartGlobalInspectionContext> KEY = Key.create("DartGlobalInspectionContext");

	private final Map<VirtualFile, AnalysisError[]> libraryRoot2Errors = new HashMap<VirtualFile, AnalysisError[]>();

	public Map<VirtualFile, AnalysisError[]> getLibraryRoot2Errors()
	{
		return libraryRoot2Errors;
	}

	@Nonnull
	@Override
	public Key<DartGlobalInspectionContext> getID()
	{
		return KEY;
	}

	@Override
	public void performPreRunActivities(@Nonnull List<Tools> globalTools, @Nonnull List<Tools> localTools, @Nonnull GlobalInspectionContext context)
	{
		final AnalysisScope analysisScope = context.getRefManager().getScope();
		if(analysisScope == null)
		{
			return;
		}

		final GlobalSearchScope scope = GlobalSearchScope.EMPTY_SCOPE.union(analysisScope.toSearchScope());
		setIndicatorText("Looking for Dart files...");
		final Collection<VirtualFile> dartFiles = FileTypeIndex.getFiles(DartFileType.INSTANCE, scope);

		for(VirtualFile dartFile : dartFiles)
		{
			analyzeFile(dartFile, context.getProject());
		}
	}

	private void analyzeFile(@Nonnull final VirtualFile virtualFile, @Nonnull final Project project)
	{
		final DartInProcessAnnotator annotator = new DartInProcessAnnotator();

		final Pair<DartFileBasedSource, AnalysisContext> sourceAndContext = ApplicationManager.getApplication().runReadAction(new
																																	  NullableComputable<Pair<DartFileBasedSource, AnalysisContext>>()
		{
			@Nullable
			public Pair<DartFileBasedSource, AnalysisContext> compute()
			{
				final PsiFile psiFile = PsiManager.getInstance(project).findFile(virtualFile);
				if(psiFile == null)
				{
					return null;
				}
				return annotator.collectInformation(psiFile);
			}
		});

		if(sourceAndContext == null)
		{
			return;
		}

		setIndicatorText("Analyzing " + virtualFile.getName() + "...");

		final AnalysisContext analysisContext = annotator.doAnnotate(sourceAndContext);
		if(analysisContext == null)
		{
			return;
		}


		libraryRoot2Errors.put(virtualFile, analysisContext.getErrors(DartFileBasedSource.getSource(project, virtualFile)).getErrors());
	}

	private static void setIndicatorText(String text)
	{
		final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
		if(indicator != null)
		{
			ProgressWrapper.unwrap(indicator).setText(text);
		}
	}

	@Override
	public void performPostRunActivities(@Nonnull List<InspectionToolWrapper> inspections, @Nonnull GlobalInspectionContext context)
	{
	}

	@Override
	public void cleanup()
	{
		libraryRoot2Errors.clear();
	}
}
