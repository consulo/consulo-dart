package com.jetbrains.lang.dart.analyzer;

import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.application.util.SystemInfo;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.io.OSAgnosticPathUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Top-level functions of upstream {@code DartFileInfo.kt}.
 */
public final class DartFileInfoKt {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartFileInfo.class);

  public static final Key<String> DART_NOT_LOCAL_FILE_URI_KEY = Key.create("DART_NOT_LOCAL_FILE_URI");

  private DartFileInfoKt() {
  }

  public static @NotNull DartFileInfo getDartFileInfo(@NotNull Project project, @NotNull String filePathOrUri) {
    if (!filePathOrUri.contains("://")) {
      return new DartLocalFileInfo(FileUtil.toSystemIndependentName(filePathOrUri));
    }
    if (!filePathOrUri.startsWith("file://")) {
      return new DartNotLocalFileInfo(project, filePathOrUri);
    }
    try {
      String path = new URI(filePathOrUri).getPath();
      if (SystemInfo.isWindows && path.length() >= 3 && path.charAt(0) == '/' && OSAgnosticPathUtil.startsWithWindowsDrive(path.substring(1))) {
        path = path.substring(1);
      }
      return new DartLocalFileInfo(path);
    }
    catch (URISyntaxException e) {
      LOG.warn("Malformed URI: " + filePathOrUri);
      return new DartNotLocalFileInfo(project, filePathOrUri);
    }
  }

  public static @NotNull DartFileInfo getDartFileInfo(@NotNull Project project, @NotNull VirtualFile virtualFile) {
    String uri = virtualFile.getUserData(DART_NOT_LOCAL_FILE_URI_KEY);
    return uri != null ? new DartNotLocalFileInfo(project, uri) : new DartLocalFileInfo(virtualFile.getPath());
  }
}
