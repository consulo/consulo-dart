package com.jetbrains.lang.dart.analyzer;

import java.io.IOException;
import java.net.URI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.google.dart.engine.internal.context.TimestampedData;
import com.google.dart.engine.source.Source;
import com.google.dart.engine.source.UriKind;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NullableComputable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Function;

public class DartFileBasedSource implements Source
{

	private final
	@Nonnull
	Project myProject;
	private final
	@Nonnull
	VirtualFile myFile;
	private final
	@Nonnull
	UriKind myUriKind;
	private long myModificationStampWhenFileContentWasRead = -1;

	private DartFileBasedSource(final @Nonnull Project project, final @Nonnull VirtualFile file, final @Nonnull UriKind uriKind)
	{
		myProject = project;
		myFile = file;
		myUriKind = uriKind;
	}

	@Nonnull
	public VirtualFile getFile()
	{
		return myFile;
	}

	public boolean isOutOfDate()
	{
		return myModificationStampWhenFileContentWasRead == -1 || myModificationStampWhenFileContentWasRead != getModificationStamp();
	}

	@Override
	public boolean equals(final Object object)
	{
		return object != null && this.getClass() == object.getClass() && myFile.equals(((DartFileBasedSource) object).myFile);
	}

	@Override
	public boolean exists()
	{
		return myFile.exists() && !myFile.isDirectory();
	}

	@Override
	public TimestampedData<CharSequence> getContents() throws Exception
	{
		final Pair<CharSequence, Long> contentsAndTimestamp = loadFile(myFile);
		myModificationStampWhenFileContentWasRead = contentsAndTimestamp.second;
		return new TimestampedData<CharSequence>(contentsAndTimestamp.second, contentsAndTimestamp.first);
	}

	@Override
	public void getContentsToReceiver(final ContentReceiver receiver) throws Exception
	{
		final Pair<CharSequence, Long> contentsAndTimestamp = loadFile(myFile);
		myModificationStampWhenFileContentWasRead = contentsAndTimestamp.second;
		receiver.accept(contentsAndTimestamp.first, contentsAndTimestamp.second);
	}

	@Override
	public String getEncoding()
	{
		return myUriKind.getEncoding() + myFile.getUrl();
	}

	@Override
	public String getFullName()
	{
		return myFile.getPath();
	}

	@Override
	public long getModificationStamp()
	{
		final Document cachedDocument = FileDocumentManager.getInstance().getCachedDocument(myFile);
		if(cachedDocument != null)
		{
			return cachedDocument.getModificationStamp();
		}
		else
		{
			return myFile.getModificationStamp();
		}
	}

	@Override
	public String getShortName()
	{
		return myFile.getName();
	}

	@Nonnull
	@Override
	public UriKind getUriKind()
	{
		return myUriKind;
	}

	@Override
	public int hashCode()
	{
		return myFile.hashCode();
	}

	@Override
	public boolean isInSystemLibrary()
	{
		return false;
	}

	@Override
	public Source resolveRelative(final URI containedUri)
	{
		final VirtualFile file = containedUri.getScheme() == null ? VfsUtilCore.findRelativeFile(containedUri.toString(),
				myFile.getParent()) : LocalFileSystem.getInstance().findFileByPath(containedUri.getPath());

		return file == null ? null : getSource(myProject, file);
	}

	@Override
	public String toString()
	{
		return myFile.getPath();
	}

	private static Pair<CharSequence, Long> loadFile(final VirtualFile file) throws Exception
	{
		final Ref<CharSequence> contentsRef = Ref.create();
		final Ref<Long> timestampRef = Ref.create();
		final Exception exception = ApplicationManager.getApplication().runReadAction(new NullableComputable<Exception>()
		{
			@Override
			@Nullable
			public Exception compute()
			{
				final Document cachedDocument = FileDocumentManager.getInstance().getCachedDocument(file);
				if(cachedDocument != null)
				{
					contentsRef.set(cachedDocument.getCharsSequence());
					timestampRef.set(cachedDocument.getModificationStamp());
				}
				else
				{
					try
					{
						contentsRef.set(VfsUtilCore.loadText(file));
						timestampRef.set(file.getModificationStamp());
					}
					catch(IOException e)
					{
						return e;
					}
				}
				return null;
			}
		});

		if(exception != null)
		{
			throw exception;
		}

		return Pair.create(contentsRef.get(), timestampRef.get());
	}

	public static DartFileBasedSource getSource(final @Nonnull Project project, final @Nonnull VirtualFile file)
	{
		return DartAnalyzerService.getInstance(project).getOrCreateSource(file, new Function<VirtualFile, DartFileBasedSource>()
		{
			@Override
			public DartFileBasedSource fun(final VirtualFile file)
			{
				return new DartFileBasedSource(project, file, UriKind.FILE_URI);
			}
		});
	}
}
