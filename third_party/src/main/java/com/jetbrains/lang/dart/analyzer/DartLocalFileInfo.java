package com.jetbrains.lang.dart.analyzer;

import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class DartLocalFileInfo extends DartFileInfo {
  private final @NotNull String filePath;

  public DartLocalFileInfo(@NotNull String filePath) {
    this.filePath = filePath;
  }

  public @NotNull String getFilePath() {
    return filePath;
  }

  @Override
  public @Nullable VirtualFile findFile() {
    return LocalFileSystem.getInstance().findFileByPath(filePath);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof DartLocalFileInfo other && filePath.equals(other.filePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filePath);
  }

  @Override
  public String toString() {
    return "DartLocalFileInfo(filePath=" + filePath + ")";
  }
}
