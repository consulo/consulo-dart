package com.jetbrains.lang.dart.analyzer;

import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.application.CachesInvalidator;
import consulo.localize.LocalizeValue;
import consulo.platform.Platform;
import consulo.project.Project;
import consulo.project.ProjectManager;
import consulo.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * A CachesInvalidator for the Dart IntelliJ plugin, on the execution of this invalidator,
 * the `~./dartServer/` directory (or equivalent on Windows machines), is deleted.
 */
@ExtensionImpl
public final class DASSCacheInvalidator extends CachesInvalidator {
  // Consulo: no getComment(), the description is shown instead
  @Override
  public @NotNull LocalizeValue getDescription() {
    return DartBundle.messageValue("analysis.server.cache.invalidate.description");
  }

  @Override
  public boolean isEnabledByDefault() {
    return false;
  }

  @Override
  public void invalidateCaches() {
    Project[] projects = ProjectManager.getInstance().getOpenProjects();
    for (Project project : projects) {
      DartAnalysisServerService serverService = DartAnalysisServerService.getInstance(project);
      serverService.stopServer();
    }
    File dartServerCacheDirectory = getDartServerCacheDirectory();
    if (dartServerCacheDirectory != null && dartServerCacheDirectory.exists()) {
      // Consulo: no FileUtil.asyncDelete
      Application.get().executeOnPooledThread(() -> FileUtil.delete(dartServerCacheDirectory));
    }
  }

  private static @Nullable File getDartServerCacheDirectory() {
    if (Platform.current().os().isWindows()) {
      String appData = System.getenv("LOCALAPPDATA");
      return appData != null ? new File(appData, ".dartServer") : null;
    }
    else {
      String userHome = System.getProperty("user.home");
      return userHome != null ? new File(userHome, ".dartServer") : null;
    }
  }
}
