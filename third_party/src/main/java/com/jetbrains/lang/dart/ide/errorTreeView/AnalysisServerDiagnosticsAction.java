// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.ui.ex.action.AnActionWithSyncUpdate;
import com.google.dart.server.GetServerPortConsumer;
import consulo.webBrowser.BrowserUtil;
import consulo.project.ui.notification.Notification;
import consulo.project.ui.notification.NotificationType;
import consulo.dart.notification.DartNotificationGroups;
import consulo.localize.LocalizeValue;
import consulo.project.ui.notification.NotificationService;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.project.Project;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkUpdateChecker;

import org.dartlang.analysis.server.protocol.RequestError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletionException;

public class AnalysisServerDiagnosticsAction extends DumbAwareAction implements AnActionWithSyncUpdate {
  private static final String MIN_LSP_DIAGNOSTIC_SERVER_SDK_VERSION = "3.13.0-106.0.dev";

  public AnalysisServerDiagnosticsAction() {
    super(DartBundle.messagePointer("analysis.server.show.diagnostics.text"));
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    final Project project = e.getData(Project.KEY);
    if (project == null || project.isDisposed()) {
      e.getPresentation().setEnabledAndVisible(false);
      return;
    }
    // Consulo: no LSP server
    final boolean isRunning = isLegacyServerRunning(project);
    e.getPresentation().setEnabledAndVisible(isRunning);
  }


  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Project project = e.getData(Project.KEY);
    if (project == null) return;

    run(project, e);
  }

  void run(final @NotNull Project project, @Nullable AnActionEvent event) {
    DartSdk sdk = DartSdk.getDartSdk(project);
    String sdkVersion = sdk != null ? sdk.getVersion() : "";
    // Consulo: no LSP server, its diagnostic port is not available
    fallbackToLegacy(project);

    if (event != null) {
    } else {
      ActionManager actionManager = ActionManager.getInstance();
      if (actionManager != null) {
      }
    }
  }

  private void fallbackToLegacy(@NotNull Project project) {
    DartAnalysisServerService server = project.getInstanceIfCreated(DartAnalysisServerService.class);
    if (server == null || !server.isServerProcessActive()) {
      showErrorNotification(project, DartBundle.message("analysis.server.not.running"));
      return;
    }
    server.diagnostic_getServerPort(new GetServerPortConsumer() {
      @Override
      public void computedServerPort(int port) {
        BrowserUtil.browse("http://localhost:" + port + "/status");
      }

      @Override
      public void onError(RequestError requestError) {
        if (project.isDisposed()) return;
        showErrorNotification(project, requestError.getMessage());
      }
    });
  }

  private static boolean isLegacyServerRunning(@NotNull Project project) {
    DartAnalysisServerService server = project.getInstanceIfCreated(DartAnalysisServerService.class);
    return server != null && server.isServerProcessActive();
  }

  private static void showErrorNotification(@NotNull Project project, @NotNull String message) {
    String title = DartBundle.message("analysis.server.show.diagnostics.error");
    NotificationService.getInstance()
      .newError(DartNotificationGroups.DART_ANALYSIS_SERVER)
      .title(LocalizeValue.of(title))
      .content(LocalizeValue.of(message))
      .notify(project);
  }
}
