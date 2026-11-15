// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.google.gson.JsonObject;
import consulo.application.ApplicationManager;
import consulo.application.ReadAction;
import consulo.execution.debug.XDebuggerUtil;
import consulo.execution.debug.XSourcePosition;
import consulo.project.Project;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

final class DtdEditorService {
  private final @NotNull Project project;
  private final @NotNull DartToolingDaemonService dtdService;

  DtdEditorService(@NotNull Project project, @NotNull DartToolingDaemonService dtdService) {
    this.project = project;
    this.dtdService = dtdService;
  }

  void setUpService() {
    dtdService.registerServiceMethod("Editor", "getActiveLocation", new JsonObject(), request -> {
      JsonObject result = ReadAction.nonBlocking(() -> DartToolingDaemonUtilsKt.getActiveLocation(project, dtdService)).executeSynchronously();
      result.addProperty("type", "ActiveLocation");
      return new DartToolingDaemonResponse(result, null);
    });

    dtdService.registerServiceMethod("Editor", "navigateToCode", new JsonObject(), request -> {
      String fileUri = request.get("uri").getAsString();
      if (fileUri == null) {
        JsonObject params = new JsonObject();
        params.addProperty("message", "No uri provided");
        return new DartToolingDaemonResponse(null, params);
      }

      String path = null;
      try {
        path = new URI(fileUri).toURL().getFile();
      }
      catch (MalformedURLException | URISyntaxException ignored) {
        // A null path will cause an early return.
      }

      if (path == null) {
        JsonObject params = new JsonObject();
        params.addProperty("message", "Path could not be found from fileUri: " + fileUri);
        return new DartToolingDaemonResponse(null, params);
      }

      VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
      int line = request.get("line").getAsInt();
      int column = request.get("column").getAsInt();

      ApplicationManager.getApplication().invokeLater(() -> {
        if (file != null && line >= 0 && column >= 0) {
          XSourcePosition position = XDebuggerUtil.getInstance().createPosition(file, line - 1, column - 1);
          if (position != null) {
            position.createNavigatable(project).navigate(false);
          }
        }
      });

      JsonObject params = new JsonObject();
      params.addProperty("success", true);
      return new DartToolingDaemonResponse(params, null);
    });
  }
}
