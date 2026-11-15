package com.jetbrains.lang.dart.analyzer;

import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.Nullable;

public sealed abstract class DartFileInfo permits DartLocalFileInfo, DartNotLocalFileInfo {
  public abstract @Nullable VirtualFile findFile();
}
