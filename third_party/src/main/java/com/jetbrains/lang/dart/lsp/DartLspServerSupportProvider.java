/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lang.lsWidget.LanguageServiceWidgetItem;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import consulo.annotation.component.ExtensionImpl;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Entry point registered in plugin.xml to declare the JetBrains native LSP server provider for Dart.
 * <p>
 * This class is required by the JetBrains LSP framework to associate {@code LspServer} instances with a specific provider key.
 * <p>
 * Note: While standard providers automatically manage server startup via {@link #fileOpened}, this provider ignores automatic
 * startup triggers because the Dart LSP Bridge lifecycle is managed manually by {@code DartAnalysisServerService} and
 * {@link DartBridgeLspServerManager}. It also suppresses the default LSP status bar widget by returning an empty list in
 * {@link #createLspWidgetItems}.
 */
@ExtensionImpl
public class DartLspServerSupportProvider implements LspServerSupportProvider {
  @Override
  public void fileOpened(@NotNull Project project, @NotNull VirtualFile file, @NotNull LspServerStarter serverStarter) {
    // Ignored. The LSP server lifecycle is managed by DartAnalysisServerService.
  }

  @Override
  public @NotNull List<LanguageServiceWidgetItem> createLspWidgetItems(@NotNull Project project, @Nullable VirtualFile currentFile) {
    // Hide the status bar widget for the Dart (Bridge) server.
    return Collections.emptyList();
  }
}
