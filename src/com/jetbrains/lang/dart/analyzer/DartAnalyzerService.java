package com.jetbrains.lang.dart.analyzer;

import gnu.trove.THashMap;
import gnu.trove.THashSet;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.dart.engine.AnalysisEngine;
import com.google.dart.engine.context.AnalysisContext;
import com.google.dart.engine.context.ChangeSet;
import com.google.dart.engine.sdk.DirectoryBasedDartSdk;
import com.google.dart.engine.source.DartUriResolver;
import com.google.dart.engine.source.PackageUriResolver;
import com.google.dart.engine.source.Source;
import com.google.dart.engine.source.SourceFactory;
import com.google.dart.engine.source.UriResolver;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileCopyEvent;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import com.intellij.openapi.vfs.VirtualFilePropertyEvent;
import com.intellij.util.Function;
import com.jetbrains.lang.dart.DartFileType;

public class DartAnalyzerService
{

	private final Project myProject;

	private
	@Nullable
	String mySdkPath;
	private
	@Nullable
	VirtualFile myDartPackagesFolder;
	private
	@Nullable
	WeakReference<AnalysisContext> myAnalysisContextRef;

	private final Collection<VirtualFile> myCreatedFiles = Collections.synchronizedSet(new THashSet<VirtualFile>());

	private final Map<VirtualFile, DartFileBasedSource> myFileToSourceMap = Collections.synchronizedMap(new THashMap<VirtualFile, DartFileBasedSource>());

	public DartAnalyzerService(final Project project)
	{
		myProject = project;

		final VirtualFileAdapter listener = new VirtualFileAdapter()
		{
			@Override
			public void beforePropertyChange(final VirtualFilePropertyEvent event)
			{
				if(VirtualFile.PROP_NAME.equals(event.getPropertyName()))
				{
					fileDeleted(event);
				}
			}

			@Override
			public void beforeFileMovement(final VirtualFileMoveEvent event)
			{
				fileDeleted(event);
			}

			@Override
			public void fileDeleted(final VirtualFileEvent event)
			{
				if(FileUtilRt.extensionEquals(event.getFileName(), DartFileType.DEFAULT_EXTENSION))
				{
					myFileToSourceMap.remove(event.getFile());
				}
			}

			@Override
			public void propertyChanged(final VirtualFilePropertyEvent event)
			{
				if(VirtualFile.PROP_NAME.equals(event.getPropertyName()))
				{
					fileCreated(event);
				}
			}

			@Override
			public void fileMoved(final VirtualFileMoveEvent event)
			{
				fileCreated(event);
			}

			@Override
			public void fileCopied(final VirtualFileCopyEvent event)
			{
				fileCreated(event);
			}

			@Override
			public void fileCreated(final VirtualFileEvent event)
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
			@Override
			public void dispose()
			{
				LocalFileSystem.getInstance().removeVirtualFileListener(listener);
			}
		});
	}

	@NotNull
	public static DartAnalyzerService getInstance(final @NotNull Project project)
	{
		return ServiceManager.getService(project, DartAnalyzerService.class);
	}

	@NotNull
	public AnalysisContext getAnalysisContext(final @NotNull VirtualFile annotatedFile, final @NotNull String sdkPath, final @Nullable VirtualFile packagesFolder)
	{
		AnalysisContext analysisContext = myAnalysisContextRef == null ? null : myAnalysisContextRef.get();

		if(analysisContext != null && Comparing.equal(sdkPath, mySdkPath) && Comparing.equal(packagesFolder, myDartPackagesFolder))
		{
			applyChangeSet(analysisContext, annotatedFile);
			myCreatedFiles.clear();
		}
		else
		{
			final DartUriResolver dartUriResolver = new DartUriResolver(new DirectoryBasedDartSdk(new File(sdkPath)));
			final UriResolver fileResolver = new DartFileResolver(myProject);
			final SourceFactory sourceFactory = packagesFolder == null ? new SourceFactory(dartUriResolver, fileResolver) : new SourceFactory(dartUriResolver, fileResolver, new PackageUriResolver(new File(packagesFolder.getPath())));

			analysisContext = AnalysisEngine.getInstance().createAnalysisContext();
			analysisContext.setSourceFactory(sourceFactory);

			mySdkPath = sdkPath;
			myDartPackagesFolder = packagesFolder;
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
				changeSet.added(DartFileBasedSource.getSource(myProject, file));
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
					changeSet.removed(source);
					continue;
				}

				if(((DartFileBasedSource) source).isOutOfDate())
				{
					changeSet.changed(source);
				}
			}
		}
	}

	/**
	 * Do not use this method directly, use {@link com.jetbrains.lang.dart.analyzer.DartFileBasedSource#getSource(com.intellij.openapi.project.Project, com.intellij.openapi.vfs.VirtualFile)}
	 */
	@NotNull
	DartFileBasedSource getOrCreateSource(final @NotNull VirtualFile file, final @NotNull Function<VirtualFile, DartFileBasedSource> creator)
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
