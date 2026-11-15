// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.index;

import consulo.util.collection.Lists;
import consulo.annotation.component.ExtensionImpl;
import consulo.project.Project;
import consulo.dart.compat.FileBasedIndexCompat;
import consulo.virtualFileSystem.VirtualFile;
import consulo.util.collection.ContainerUtil;
import consulo.language.psi.stub.DefaultFileTypeSpecificInputFilter;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.language.psi.stub.FileContent;
import consulo.index.io.ID;
import consulo.language.psi.stub.SingleEntryFileBasedIndexExtension;
import consulo.language.psi.stub.SingleEntryIndexer;
import consulo.index.io.data.DataExternalizer;
import consulo.dart.compat.StringCollectionExternalizer;
import com.jetbrains.lang.dart.DartFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ExtensionImpl
public final class DartPartUriIndex extends SingleEntryFileBasedIndexExtension<List<String>> {
  public static final ID<Integer, List<String>> DART_PATH_INDEX = ID.create("DartPathIndex");

  @Override
  public @NotNull ID<Integer, List<String>> getName() {
    return DART_PATH_INDEX;
  }

  @Override
  public int getVersion() {
    return DartIndexUtil.INDEX_VERSION + 1;
  }

  @Override
  public @NotNull SingleEntryIndexer<List<String>> getIndexer() {
    return new SingleEntryIndexer<>(false) {
      @Override
      protected @Nullable List<String> computeValue(@NotNull FileContent inputData) {
        return DartIndexUtil.indexFile(inputData).getPartUris();
      }
    };
  }

  @Override
  public @NotNull DataExternalizer<List<String>> getValueExternalizer() {
    return StringCollectionExternalizer.STRING_LIST_EXTERNALIZER;
  }

  @Override
  public @NotNull FileBasedIndex.InputFilter getInputFilter() {
    return new DefaultFileTypeSpecificInputFilter(DartFileType.INSTANCE);
  }

  public static @NotNull List<String> getPartUris(final @NotNull Project project, final @NotNull VirtualFile virtualFile) {
    return Lists.notNullize(FileBasedIndexCompat.getSingleEntryIndexData(DART_PATH_INDEX, virtualFile, project));
  }
}
