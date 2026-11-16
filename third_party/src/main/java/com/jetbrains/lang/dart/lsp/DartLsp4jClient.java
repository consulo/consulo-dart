/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.Lsp4jClient;
import com.intellij.platform.lsp.api.LspServerNotificationsHandler;
import com.jetbrains.lang.dart.analyzer.DartFileInfo;
import com.jetbrains.lang.dart.analyzer.DartFileInfoKt;
import com.jetbrains.lang.dart.analyzer.DartLocalFileInfo;
import com.jetbrains.lang.dart.ide.errorTreeView.DartProblemsView;
import consulo.project.Project;
import org.dartlang.analysis.server.protocol.AnalysisError;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom {@link Lsp4jClient} that intercepts standard {@code textDocument/publishDiagnostics} notifications
 * to update the Dart Analysis tool window ({@code DartProblemsView}) identically in both Bridge mode and Full LSP
 * (while delegating to {@code super.publishDiagnostics} for editor highlightings and squiggles), and handles
 * custom {@code dart/textDocument/publishClosingLabels} notifications via {@link DartLanguageClient}.
 * <p>
 * Consulo: the inherited public {@link Lsp4jClient#refreshInlayHints()} is used by other code
 * (see {@link DartLspServerDescriptor#getLsp4jClient()}) to refresh inlay hints.
 */
public class DartLsp4jClient extends Lsp4jClient implements DartLanguageClient {
  private final @NotNull Project project;

  public DartLsp4jClient(@NotNull Project project, @NotNull LspServerNotificationsHandler handler) {
    super(handler);
    this.project = project;
  }

  @Override
  public void publishDiagnostics(@NotNull PublishDiagnosticsParams params) {
    if (project.isDisposed()) return;
    super.publishDiagnostics(params);
    List<Diagnostic> diagnostics = params.getDiagnostics() != null ? params.getDiagnostics() : Collections.emptyList();
    List<AnalysisError> errors = new ArrayList<>();
    for (AnalysisError error : DartLspDiagnosticConverter.convertDiagnostics(project, params.getUri(), diagnostics)) {
      if (!"TODO".equals(error.getType())) {
        errors.add(error);
      }
    }
    DartFileInfo fileInfo = DartFileInfoKt.getDartFileInfo(project, params.getUri());
    String filePath = fileInfo instanceof DartLocalFileInfo localFileInfo ? localFileInfo.getFilePath() : params.getUri();
    DartProblemsView.getInstance(project).updateErrorsForFile(filePath, errors);
  }

  @Override
  public void publishClosingLabels(@NotNull DartPublishClosingLabelsParams params) {
    if (params.getUri() != null) {
      DartLspClosingLabelsService.getInstance(project).updateClosingLabels(params.getUri(), params.getLabels());
    }
  }
}
