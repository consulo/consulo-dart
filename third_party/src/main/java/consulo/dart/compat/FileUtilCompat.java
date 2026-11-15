// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * {@code FileUtil} methods of IntelliJ which Consulo does not have.
 */
public final class FileUtilCompat {
  private FileUtilCompat() {
  }

  public static @Nullable String loadFileOrNull(@NotNull File file) {
    try {
      return FileUtil.loadFile(file);
    }
    catch (IOException e) {
      return null;
    }
  }
}
