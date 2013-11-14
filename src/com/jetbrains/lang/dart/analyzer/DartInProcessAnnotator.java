package com.jetbrains.lang.dart.analyzer;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.dart.engine.context.AnalysisContext;
import com.google.dart.engine.context.AnalysisException;
import com.google.dart.engine.error.AnalysisError;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import com.jetbrains.lang.dart.ide.module.DartModuleExtension;
import com.jetbrains.lang.dart.psi.DartEmbeddedContent;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import com.jetbrains.lang.dart.validation.fixes.DartResolverErrorCode;
import com.jetbrains.lang.dart.validation.fixes.DartTypeErrorCode;
import com.jetbrains.lang.dart.validation.fixes.FixAndIntentionAction;

public class DartInProcessAnnotator extends ExternalAnnotator<Pair<com.jetbrains.lang.dart.analyzer.DartFileBasedSource, AnalysisContext>, AnalysisContext>
{
	static final Logger LOG = Logger.getInstance("#com.jetbrains.lang.dart.analyzer.DartInProcessAnnotator");

	@Override
	@Nullable
	public Pair<com.jetbrains.lang.dart.analyzer.DartFileBasedSource, AnalysisContext> collectInformation(@NotNull final PsiFile psiFile)
	{
		final Project project = psiFile.getProject();

		final VirtualFile annotatedFile = DartResolveUtil.getRealVirtualFile(psiFile);
		if(annotatedFile == null)
		{
			return null;
		}

		final Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
		if(module == null)
		{
			return null;
		}

		final Sdk sdk = ModuleUtilCore.getSdk(module, DartModuleExtension.class);
		if(sdk == null)
		{
			return null;
		}

		final String sdkPath = sdk.getHomePath();
		if(StringUtil.isEmptyOrSpaces(sdkPath))
		{
			return null;
		}

		final File sdkDir = new File(sdkPath);
		if(!sdkDir.isDirectory())
		{
			return null;
		}

		if(psiFile instanceof XmlFile && !containsDartEmbeddedContent((XmlFile) psiFile))
		{
			return null;
		}

		if(FileUtil.isAncestor(sdkDir.getPath(), annotatedFile.getPath(), true))
		{
			return null;
		}

		final VirtualFile packagesFolder = DartResolveUtil.getDartPackagesFolder(project, annotatedFile);

		if(packagesFolder != null && VfsUtilCore.isAncestor(packagesFolder, annotatedFile, true))
		{
			return null;
		}

		final List<VirtualFile> libraries = DartResolveUtil.findLibrary(psiFile, GlobalSearchScope.projectScope(project));
		final VirtualFile fileToAnalyze = libraries.isEmpty() || libraries.contains(annotatedFile) ? annotatedFile : libraries.get(0);

		return Pair.create(com.jetbrains.lang.dart.analyzer.DartFileBasedSource.getSource(project, fileToAnalyze), DartAnalyzerService.getInstance(project).getAnalysisContext(annotatedFile, sdkPath, packagesFolder));
	}

	private static boolean containsDartEmbeddedContent(final XmlFile file)
	{
		final String text = file.getText();
		int i = -1;
		while((i = text.indexOf("application/dart", i + 1)) != -1)
		{
			final PsiElement element = file.findElementAt(i);
			final XmlTag tag = element == null ? null : PsiTreeUtil.getParentOfType(element, XmlTag.class);
			if(tag != null && "script".equalsIgnoreCase(tag.getName()) && PsiTreeUtil.getChildOfType(tag, DartEmbeddedContent.class) != null)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	@Nullable
	public AnalysisContext doAnnotate(final Pair<com.jetbrains.lang.dart.analyzer.DartFileBasedSource, AnalysisContext> sourceAndContext)
	{
		try
		{
			sourceAndContext.second.computeErrors(sourceAndContext.first);
			return sourceAndContext.second;
		}
		catch(AnalysisException e)
		{
			LOG.info(e);
		}
		return null;
	}

	@Override
	public void apply(@NotNull PsiFile psiFile, @Nullable AnalysisContext analysisContext, @NotNull AnnotationHolder holder)
	{
		if(analysisContext == null)
		{
			return;
		}

		final VirtualFile annotatedFile = DartResolveUtil.getRealVirtualFile(psiFile);
		final com.jetbrains.lang.dart.analyzer.DartFileBasedSource source = annotatedFile == null ? null : com.jetbrains.lang.dart.analyzer.DartFileBasedSource.getSource(psiFile.getProject(), annotatedFile);
		if(source == null)
		{
			return;
		}

		// analysisContext.getErrors() doesn't perform analysis and returns already calculated errors
		final AnalysisError[] messages = analysisContext.getErrors(source).getErrors();
		if(messages == null || !psiFile.isValid())
		{
			return;
		}

		for(AnalysisError message : messages)
		{
			if(source != message.getSource())
			{
				LOG.warn("Unexpected Source: " + message.getSource() + ",\nfile: " + annotatedFile.getPath());
				continue;
			}

			final Annotation annotation = annotate(holder, message);
			if(annotation != null)
			{
				registerFixes(psiFile, annotation, message);
			}
		}
	}

	private static void registerFixes(final PsiFile psiFile, final Annotation annotation, final AnalysisError message)
	{
		List<? extends IntentionAction> fixes = Collections.emptyList();

		//noinspection EnumSwitchStatementWhichMissesCases
		switch(message.getErrorCode().getType())
		{
			case STATIC_WARNING:
				final DartResolverErrorCode resolverErrorCode = DartResolverErrorCode.findError(message.getErrorCode().toString());
				if(resolverErrorCode != null)
				{
					fixes = resolverErrorCode.getFixes(psiFile, message.getOffset(), message.getMessage());
				}
				break;
			case STATIC_TYPE_WARNING:
			case COMPILE_TIME_ERROR:
				final DartTypeErrorCode typeErrorCode = DartTypeErrorCode.findError(message.getErrorCode().toString());
				if(typeErrorCode != null)
				{
					fixes = typeErrorCode.getFixes(psiFile, message.getOffset(), message.getMessage());
				}
				break;
		}


		if(!fixes.isEmpty())
		{
			PsiElement element = psiFile.findElementAt(message.getOffset() + message.getLength() / 2);
			while(element != null && ((annotation.getStartOffset() < element.getTextOffset()) || annotation.getEndOffset() > element.getTextRange().getEndOffset()))
			{
				element = element.getParent();
			}

			if(element != null && (annotation.getStartOffset() != element.getTextRange().getStartOffset() || annotation.getEndOffset() != element.getTextRange().getEndOffset()))
			{
				element = null;
			}

			for(IntentionAction intentionAction : fixes)
			{
				if(intentionAction instanceof FixAndIntentionAction)
				{
					((FixAndIntentionAction) intentionAction).setElement(element);
				}
				annotation.registerFix(intentionAction);
			}
		}
	}

	@Nullable
	private static Annotation annotate(final AnnotationHolder holder, final AnalysisError message)
	{
		final TextRange textRange = new TextRange(message.getOffset(), message.getOffset() + message.getLength());

		switch(message.getErrorCode().getErrorSeverity())
		{
			case NONE:
				return null;
			case INFO:
				return holder.createInfoAnnotation(textRange, message.getMessage());
			case WARNING:
				return holder.createWarningAnnotation(textRange, message.getMessage());
			case ERROR:
				return holder.createErrorAnnotation(textRange, message.getMessage());
		}
		return null;
	}
}
