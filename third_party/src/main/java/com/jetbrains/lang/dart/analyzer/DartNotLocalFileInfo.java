package com.jetbrains.lang.dart.analyzer;

import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class DartNotLocalFileInfo extends DartFileInfo {
  private final @NotNull Project project;
  private final @NotNull String fileUri;

  public DartNotLocalFileInfo(@NotNull Project project, @NotNull String fileUri) {
    this.project = project;
    this.fileUri = fileUri;
  }

  public @NotNull String getFileUri() {
    return fileUri;
  }

  @Override
  public @Nullable VirtualFile findFile() {
    VirtualFile file = DartAnalysisServerService.getInstance(project).getNotLocalVirtualFile(fileUri);
    return file != null ? file : VirtualFileManager.getInstance().findFileByUrl(fileUri);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof DartNotLocalFileInfo other && project.equals(other.project) && fileUri.equals(other.fileUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(project, fileUri);
  }

  @Override
  public String toString() {
    return "DartNotLocalFileInfo(project=" + project + ", fileUri=" + fileUri + ")";
  }
}
