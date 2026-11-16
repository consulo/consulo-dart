/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.LspServer;
import com.intellij.platform.lsp.api.LspServerManager;
import com.intellij.platform.lsp.api.LspServerState;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.sdk.DartSdk;
import consulo.application.progress.ProgressManager;
import consulo.application.util.concurrent.AppExecutorUtil;
import consulo.project.Project;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public final class DartLspService {
  public static final DartLspService INSTANCE = new DartLspService();

  private static final Map<LspServer, String> connectedDtdUriByServer = Collections.synchronizedMap(new WeakHashMap<>());

  private DartLspService() {
  }

  private static @Nullable LspServer getFirstServer(@NotNull Project project) {
    for (LspServer server : LspServerManager.getInstance(project).getServersForProvider(DartLspServerSupportProvider.class)) {
      return server;
    }
    return null;
  }

  public static boolean isServerRunning(@NotNull Project project) {
    if (project.isDisposed()) return false;
    for (LspServer server : LspServerManager.getInstance(project).getServersForProvider(DartLspServerSupportProvider.class)) {
      if (server.getState() == LspServerState.Running) {
        return true;
      }
    }
    return false;
  }

  public static @NotNull CompletableFuture<Integer> getDiagnosticServerPort(@NotNull Project project) {
    return CompletableFuture.supplyAsync(() -> {
      if (project.isDisposed()) return null;
      LspServer server = getFirstServer(project);
      if (server == null) return null;
      Integer[] port = {null};
      ProgressManager.getInstance().executeNonCancelableSection(() -> {
        DiagnosticServerResult result = server.sendRequestSync(it -> ((DartLanguageServer)it).diagnosticServer());
        port[0] = result != null ? result.getPort() : null;
      });
      return port[0];
    }, AppExecutorUtil.getAppExecutorService());
  }

  public static @NotNull CompletableFuture<Object> connectToDtd(@NotNull Project project, @NotNull String uri) {
    return CompletableFuture.supplyAsync(() -> {
      if (project.isDisposed()) return null;
      // Connection to DTD is used for server-initiated edits (workspace/applyEdit), which require Dart 3.8+.
      DartSdk sdk = DartSdk.getDartSdk(project);
      if (sdk == null) return null;
      if (!DartAnalysisServerService.isDartSdkVersionSufficientForWorkspaceApplyEdits(sdk.getVersion())) {
        return null;
      }
      LspServer server = getFirstServer(project);
      if (server == null) return null;
      if (server.getState() != LspServerState.Running) {
        return null;
      }
      synchronized (connectedDtdUriByServer) {
        if (Objects.equals(connectedDtdUriByServer.get(server), uri)) {
          return null;
        }
        connectedDtdUriByServer.put(server, uri);
      }
      boolean[] dispatched = {false};
      try {
        Object[] result = {null};
        ProgressManager.getInstance().executeNonCancelableSection(() -> {
          boolean[] requestStarted = {false};
          result[0] = server.sendRequestSync(lspServer -> {
            CompletableFuture<Object> future = ((DartLanguageServer)lspServer).connectToDtd(new ConnectToDtdParams(uri))
              .whenComplete((ignored, error) -> {
                if (error != null) {
                  evictConnectedDtdUri(server, uri);
                }
              });
            requestStarted[0] = true;
            return future;
          });
          dispatched[0] = requestStarted[0];
        });
        return result[0];
      }
      finally {
        if (!dispatched[0]) {
          evictConnectedDtdUri(server, uri);
        }
      }
    }, AppExecutorUtil.getAppExecutorService());
  }

  private static void evictConnectedDtdUri(@NotNull LspServer server, @NotNull String uri) {
    connectedDtdUriByServer.remove(server, uri);
  }

  public static @NotNull CompletableFuture<WorkspaceEdit> willRenameFiles(@NotNull Project project, @NotNull RenameFilesParams params) {
    return CompletableFuture.supplyAsync(() -> {
      if (project.isDisposed()) return null;
      LspServer server = getFirstServer(project);
      if (server == null) return null;
      WorkspaceEdit[] edit = {null};
      ProgressManager.getInstance().executeNonCancelableSection(() -> {
        edit[0] = server.sendRequestSync(it -> it.getWorkspaceService().willRenameFiles(params));
      });
      return edit[0];
    }, AppExecutorUtil.getAppExecutorService());
  }
}
