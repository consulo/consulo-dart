// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.index.io.ID;
import consulo.language.psi.stub.FileBasedIndex;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * {@code FileBasedIndex} methods of IntelliJ which Consulo does not have.
 */
public final class FileBasedIndexCompat {
  private FileBasedIndexCompat() {
  }

  /**
   * {@code FileBasedIndexEx.getSingleEntryIndexData}
   */
  public static <V> @Nullable V getSingleEntryIndexData(@NotNull ID<Integer, V> id,
                                                        @NotNull VirtualFile virtualFile,
                                                        @NotNull Project project) {
    Map<Integer, V> data = FileBasedIndex.getInstance().getFileData(id, virtualFile, project);
    if (data.isEmpty()) return null;
    if (data.size() == 1) return data.values().iterator().next();
    throw new IllegalStateException("Invalid single entry index data '" + id + "'");
  }
}
