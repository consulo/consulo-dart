/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.google.gson.JsonObject;
import com.intellij.platform.lsp.api.LspClientManager;
import com.intellij.platform.lsp.api.LspServer;
import com.intellij.platform.lsp.api.LspServerManager;
import com.intellij.platform.lsp.api.LspServerState;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceAPI;
import consulo.annotation.component.ServiceImpl;
import consulo.application.ApplicationManager;
import consulo.language.psi.PsiManager;
import consulo.project.Project;
import consulo.ui.ModalityState;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.jetbrains.annotations.NotNull;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps the {@code dart} configuration section that the Dart Analysis Server holds in sync with the Dart
 * checkboxes in Settings | Editor | Inlay Hints.
 * <p>
 * The server never reads the settings on its own, it pulls them with {@code workspace/configuration}:
 * once while it starts up, because the client advertises the {@code workspace.configuration} capability,
 * and once more for every {@code workspace/didChangeConfiguration} notification it gets. There is no
 * event to hang that notification on - {@code DeclarativeInlayHintsSettings} publishes nothing when the
 * user flips a checkbox - so the drift is noticed in
 * {@link DartLspInlayHintSupport#shouldAskServerForInlayHints}, which runs before every inlay hint
 * request cycle.
 * <p>
 * That is the only place it is noticed: while no Dart editor is open there is no such cycle, so a
 * change the user makes then reaches the server when the next Dart file is opened - which is early
 * enough, because until then there are no hints to get wrong.
 */
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartLspConfigurationSync {
  /** The LSP notification that makes the server read the configuration again. */
  private static final String DID_CHANGE_CONFIGURATION = "workspace/didChangeConfiguration";

  public static @NotNull DartLspConfigurationSync getInstance(@NotNull Project project) {
    return project.getInstance(DartLspConfigurationSync.class);
  }

  private final @NotNull Project project;

  private final DartLspConfigurationPushState state = new DartLspConfigurationPushState();

  /**
   * Has the inlay hints computed again. Replaced in tests, which have neither the EDT nor a
   * daemon to run the real one on.
   */
  @VisibleForTesting
  @NotNull Runnable refreshInlayHints = this::scheduleInlayHintRefresh;

  @Inject
  public DartLspConfigurationSync(@NotNull Project project) {
    this.project = project;
  }

  @VisibleForTesting
  @NotNull Runnable getRefreshInlayHints() {
    return refreshInlayHints;
  }

  @VisibleForTesting
  void setRefreshInlayHints(@NotNull Runnable refreshInlayHints) {
    this.refreshInlayHints = refreshInlayHints;
  }

  /**
   * Notifies the server that the settings have changed, unless it already knows them or it has
   * been notified and has not read them again yet.
   */
  public void pushConfigurationIfChanged() {
    DartBridgeLspServerManager bridgeServerManager = project.getInstanceIfCreated(DartBridgeLspServerManager.class);
    DartBridgeLspServer bridgeServer = bridgeServerManager != null ? bridgeServerManager.getBridgeServer() : null;
    if (bridgeServer != null) {
      if (!DartAnalysisServerService.getInstance(project).isLspConfigurationNotificationSupported()) return;
      if (!state.beginPush(DartLspInlayHintsConfiguration.INSTANCE.buildDartSection())) return;

      // The settings stay empty: the server ignores them and pulls workspace/configuration.
      boolean sent = bridgeServer.forwardNotification(DID_CHANGE_CONFIGURATION, new DidChangeConfigurationParams(new JsonObject()));
      if (!sent) state.pushFailed();
      return;
    }

    List<LspServer> runningServers = getRunningServers();
    if (!runningServers.isEmpty()) {
      if (!state.beginPush(DartLspInlayHintsConfiguration.INSTANCE.buildDartSection())) return;

      for (LspServer server : runningServers) {
        server.sendNotification(
          it -> it.getWorkspaceService().didChangeConfiguration(new DidChangeConfigurationParams(new JsonObject())));
      }
      return;
    }

    // There is no server while no LSP client is connected, and then there are no inlay hints to
    // get wrong either, so the next request cycle can try again.
    if (!DartAnalysisServerService.getInstance(project).isLspConfigurationNotificationSupported()) return;
    if (!state.beginPush(DartLspInlayHintsConfiguration.INSTANCE.buildDartSection())) return;
    state.pushFailed();
  }

  /**
   * Remembers the section that the server has just read, and has the inlay hints computed again
   * if it read them because the settings had changed.
   */
  public void configurationSentToServer(@NotNull JsonObject section) {
    if (state.configurationSentToServer(section)) refreshInlayHints.run();
  }

  /**
   * Throws away the hints that the server computed from the old settings and has the inlay hint
   * pass run again.
   * <p>
   * The server applies the new configuration silently - it sends no
   * {@code workspace/inlayHint/refresh} - and the LSP inlay hints provider never asks for
   * hints itself, it only filters the ones the cache holds. That cache (the LSP highlighting cache) is
   * keyed on {@code PsiModificationTracker.modificationCount} and asks the server again only when that
   * count has moved, which neither a daemon restart nor {@code forceHintsUpdateOnNextPass} does. So a
   * category that the server stopped computing would stay on screen, and one it started
   * computing would never appear, until the user happens to edit the file.
   * <p>
   * Bumping that count from here is therefore deliberate. {@link PsiManager#dropPsiCaches} is the
   * public API that does it (it fires a PSI change that {@code PsiModificationTrackerImpl} turns into
   * an increment, and it has to run on the EDT or in a write action); the lighter
   * {@code PsiModificationTrackerImpl.incCounter()} is not API, it would need a cast to an
   * implementation class. The cost is that every {@code CachedValue} keyed on the PSI modification
   * count is recomputed once, project-wide, per settings change - i.e. per rare and explicit
   * user action.
   * <p>
   * The clean fix is an invalidation hook on the cache of the vendored LSP client; that is a
   * change to JetBrains-owned code and is proposed to the maintainers along with this work.
   */
  private void scheduleInlayHintRefresh() {
    ApplicationManager.getApplication().invokeLater(() -> {
      PsiManager.getInstance(project).dropPsiCaches();
      // Consulo: LspServerManagerImpl.refreshInlayHints(project) is impl-only; ask the client of every Dart
      // LSP server to refresh its inlay hints instead, which is what a `workspace/inlayHint/refresh` from the
      // server would do.
      LspClientManager clientManager = project.getInstanceIfCreated(LspClientManager.class);
      if (clientManager == null) return;
      for (LspServer server : ((LspServerManager)clientManager).getServersForProvider(DartLspServerSupportProvider.class)) {
        if (server.getDescriptor() instanceof DartLspServerDescriptor descriptor) {
          DartLsp4jClient client = descriptor.getLsp4jClient();
          if (client != null) {
            client.refreshInlayHints();
          }
        }
      }
    }, ModalityState.nonModal(), project.getDisposed());
  }

  /** Forgets what the server knew; the next server has to read the settings again. */
  public void serverStopped() {
    state.serverStopped();
  }

  private @NotNull List<LspServer> getRunningServers() {
    // Consulo: the project service is registered against LspClientManager, LspServerManager is its deprecated view
    LspClientManager clientManager = project.getInstanceIfCreated(LspClientManager.class);
    List<LspServer> result = new ArrayList<>();
    if (clientManager == null) return result;
    for (LspServer server : ((LspServerManager)clientManager).getServersForProvider(DartLspServerSupportProvider.class)) {
      if (server.getState() == LspServerState.Running) {
        result.add(server);
      }
    }
    return result;
  }
}
