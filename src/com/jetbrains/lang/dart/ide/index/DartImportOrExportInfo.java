package com.jetbrains.lang.dart.ide.index;

import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DartImportOrExportInfo implements DartShowHideInfo
{
	public enum Kind
	{
		Import, Export
	}

	private final
	@NotNull
	Kind myKind;
	private final
	@NotNull
	String myUri;
	private final
	@Nullable
	String myImportPrefix;
	private final
	@NotNull
	Set<String> myShowComponents;
	private final
	@NotNull
	Set<String> myHideComponents;

	public DartImportOrExportInfo(final @NotNull Kind kind, final @NotNull String uri, final @Nullable String importPrefix,
			final @NotNull Set<String> showComponents, final @NotNull Set<String> hideComponents)
	{
		myKind = kind;
		myUri = uri;
		myImportPrefix = kind == Kind.Export ? null : importPrefix;
		myShowComponents = showComponents;
		myHideComponents = hideComponents;
	}

	@NotNull
	public String getUri()
	{
		return myUri;
	}

	@NotNull
	public Kind getKind()
	{
		return myKind;
	}

	@Nullable
	public String getImportPrefix()
	{
		return myImportPrefix;
	}

	@NotNull
	public Set<String> getShowComponents()
	{
		return myShowComponents;
	}

	@NotNull
	public Set<String> getHideComponents()
	{
		return myHideComponents;
	}
}
