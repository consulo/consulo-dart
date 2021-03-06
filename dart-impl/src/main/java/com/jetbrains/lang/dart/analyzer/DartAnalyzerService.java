package com.jetbrains.lang.dart.analyzer;

import com.google.dart.engine.AnalysisEngine;
import com.google.dart.engine.context.AnalysisContext;
import com.google.dart.engine.context.ChangeSet;
import com.google.dart.engine.sdk.DirectoryBasedDartSdk;
import com.google.dart.engine.source.DartUriResolver;
import com.google.dart.engine.source.ExplicitPackageUriResolver;
import com.google.dart.engine.source.Source;
import com.google.dart.engine.source.SourceFactory;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.*;
import com.intellij.reference.SoftReference;
import com.intellij.util.Function;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.util.DartUrlResolver;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.*;

public class DartAnalyzerService
{

	private final Project myProject;

	private
	@Nullable
	String mySdkPath;
	private long myPubspecYamlTimestamp;
	private
	@Nonnull
	VirtualFile[] myDartPackageRoots;
	private
	@Nullable
	VirtualFile myContentRoot; // checked only in case of ExplicitPackageUriResolver

	private
	@Nullable
	WeakReference<AnalysisContext> myAnalysisContextRef;

	private final Collection<VirtualFile> myCreatedFiles = Collections.synchronizedSet(new HashSet<VirtualFile>());

	private final Map<VirtualFile, DartFileBasedSource> myFileToSourceMap = Collections.synchronizedMap(new HashMap<VirtualFile,
				DartFileBasedSource>());

	public DartAnalyzerService(final Project project)
	{
		myProject = project;

		final VirtualFileListener listener = new VirtualFileListener()
		{
			public void beforePropertyChange(@Nonnull final VirtualFilePropertyEvent event)
			{
				if(VirtualFile.PROP_NAME.equals(event.getPropertyName()))
				{
					fileDeleted(event);
				}
			}

			public void beforeFileMovement(@Nonnull final VirtualFileMoveEvent event)
			{
				fileDeleted(event);
			}

			public void fileDeleted(@Nonnull final VirtualFileEvent event)
			{
				if(FileUtilRt.extensionEquals(event.getFileName(), DartFileType.DEFAULT_EXTENSION))
				{
					myFileToSourceMap.remove(event.getFile());
				}
			}

			public void propertyChanged(@Nonnull final VirtualFilePropertyEvent event)
			{
				if(VirtualFile.PROP_NAME.equals(event.getPropertyName()))
				{
					fileCreated(event);
				}
			}

			public void fileMoved(@Nonnull final VirtualFileMoveEvent event)
			{
				fileCreated(event);
			}

			public void fileCopied(@Nonnull final VirtualFileCopyEvent event)
			{
				fileCreated(event);
			}

			public void fileCreated(@Nonnull final VirtualFileEvent event)
			{
				if(FileUtilRt.extensionEquals(event.getFileName(), DartFileType.DEFAULT_EXTENSION))
				{
					myCreatedFiles.add(event.getFile());
				}
			}
		};

		LocalFileSystem.getInstance().addVirtualFileListener(listener);

		Disposer.register(project, new Disposable()
		{
			public void dispose()
			{
				LocalFileSystem.getInstance().removeVirtualFileListener(listener);
			}
		});
	}

	@Nonnull
	public static DartAnalyzerService getInstance(final @Nonnull Project project)
	{
		return ServiceManager.getService(project, DartAnalyzerService.class);
	}

	@Nonnull
	public AnalysisContext getAnalysisContext(final @Nonnull VirtualFile annotatedFile, final @Nonnull String sdkPath)
	{
		AnalysisContext analysisContext = SoftReference.dereference(myAnalysisContextRef);

		final DartUrlResolver dartUrlResolver = DartUrlResolver.getInstance(myProject, annotatedFile);
		final VirtualFile yamlFile = dartUrlResolver.getPubspecYamlFile();
		final Document cachedDocument = yamlFile == null ? null : FileDocumentManager.getInstance().getCachedDocument(yamlFile);
		final long pubspecYamlTimestamp = yamlFile == null ? -1 : cachedDocument == null ? yamlFile.getModificationCount() : cachedDocument
				.getModificationStamp();

		final VirtualFile[] packageRoots = dartUrlResolver.getPackageRoots();

		final VirtualFile contentRoot = ProjectRootManager.getInstance(myProject).getFileIndex().getContentRootForFile(annotatedFile);
		final Module module = ModuleUtilCore.findModuleForFile(annotatedFile, myProject);

		final boolean useExplicitPackageUriResolver = !ApplicationManager.getApplication().isUnitTestMode() &&
				contentRoot != null &&
				module != null &&
				!DartConfigurable.isCustomPackageRootSet(module) &&
				yamlFile == null;

		final boolean sameContext = analysisContext != null &&
				Comparing.equal(sdkPath, mySdkPath) &&
				pubspecYamlTimestamp == myPubspecYamlTimestamp &&
				Comparing.haveEqualElements(packageRoots, myDartPackageRoots) &&
				(!useExplicitPackageUriResolver || Comparing.equal(contentRoot, myContentRoot));

		if(sameContext)
		{
			applyChangeSet(analysisContext, annotatedFile);
			myCreatedFiles.clear();
		}
		else
		{
			final DirectoryBasedDartSdk dirBasedSdk = new DirectoryBasedDartSdk(new File(sdkPath));
			final DartUriResolver dartUriResolver = new DartUriResolver(dirBasedSdk);
			final DartFileAndPackageUriResolver fileAndPackageUriResolver = new DartFileAndPackageUriResolver(myProject, dartUrlResolver);

			final SourceFactory sourceFactory = useExplicitPackageUriResolver ? new SourceFactory(dartUriResolver, fileAndPackageUriResolver,
					new ExplicitPackageUriResolver(dirBasedSdk, new File(contentRoot.getPath()))) : new SourceFactory(dartUriResolver,
					fileAndPackageUriResolver);

			analysisContext = AnalysisEngine.getInstance().createAnalysisContext();
			analysisContext.setSourceFactory(sourceFactory);

			mySdkPath = sdkPath;
			myPubspecYamlTimestamp = pubspecYamlTimestamp;
			myDartPackageRoots = packageRoots;
			myContentRoot = contentRoot;
			myAnalysisContextRef = new WeakReference<AnalysisContext>(analysisContext);
		}

		return analysisContext;
	}

	private void applyChangeSet(final AnalysisContext context, final VirtualFile annotatedFile)
	{
		final ChangeSet changeSet = new ChangeSet();

		final DartFileBasedSource source = myFileToSourceMap.get(annotatedFile);
		if(source != null)
		{
			handleDeletedAndOutOfDateSources(changeSet, source);
		}

		handleDeletedAndOutOfDateSources(changeSet, context.getLibrarySources());
		handleDeletedAndOutOfDateSources(changeSet, context.getHtmlSources());

		synchronized(myCreatedFiles)
		{
			for(VirtualFile file : myCreatedFiles)
			{
				changeSet.addedSource(DartFileBasedSource.getSource(myProject, file));
			}
		}

		context.applyChanges(changeSet);
	}

	private void handleDeletedAndOutOfDateSources(final ChangeSet changeSet, final Source... sources)
	{
		for(final Source source : sources)
		{
			if(source instanceof DartFileBasedSource)
			{
				if(!source.exists() || !myFileToSourceMap.containsKey(((DartFileBasedSource) source).getFile()))
				{
					changeSet.removedSource(source);
					continue;
				}

				if(((DartFileBasedSource) source).isOutOfDate())
				{
					changeSet.changedSource(source);
				}
			}
		}
	}

	/**
	 * Do not use this method directly, use {@link com.jetbrains.lang.dart.analyzer.DartFileBasedSource#getSource(com.intellij.openapi.project
	 * .Project, com.intellij.openapi.vfs.VirtualFile)}
	 */
	@Nonnull
	DartFileBasedSource getOrCreateSource(final @Nonnull VirtualFile file, final @Nonnull Function<VirtualFile, DartFileBasedSource> creator)
	{
		DartFileBasedSource source = myFileToSourceMap.get(file);
		if(source == null)
		{
			source = creator.fun(file);
			myFileToSourceMap.put(file, source);
		}
		return source;
	}
}
