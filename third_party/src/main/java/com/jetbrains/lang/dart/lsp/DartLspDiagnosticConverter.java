/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.util.Lsp4jUtilKt;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.analyzer.DartFileInfo;
import com.jetbrains.lang.dart.analyzer.DartFileInfoKt;
import com.jetbrains.lang.dart.analyzer.DartLocalFileInfo;
import consulo.application.ReadAction;
import consulo.document.Document;
import consulo.document.FileDocumentManager;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.dartlang.analysis.server.protocol.AnalysisError;
import org.dartlang.analysis.server.protocol.DiagnosticMessage;
import org.dartlang.analysis.server.protocol.Location;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DiagnosticRelatedInformation;
import org.eclipse.lsp4j.DiagnosticSeverity;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DartLspDiagnosticConverter {
  public static final DartLspDiagnosticConverter INSTANCE = new DartLspDiagnosticConverter();

  private DartLspDiagnosticConverter() {
  }

  /**
   * Converts an LSP {@link Diagnostic} object received from {@code textDocument/publishDiagnostics}
   * into a legacy DAS {@link AnalysisError} protocol object so it can be displayed in the
   * Dart Analysis tool window ({@code DartProblemsView}) and used by Project View decorators.
   */
  public static @NotNull AnalysisError convertDiagnosticToAnalysisError(@NotNull Project project,
                                                                        @NotNull String uri,
                                                                        @NotNull Diagnostic diagnostic) {
    DiagnosticSeverity diagnosticSeverity = diagnostic.getSeverity();
    String severity;
    if (diagnosticSeverity == DiagnosticSeverity.Error) {
      severity = "ERROR";
    }
    else if (diagnosticSeverity == DiagnosticSeverity.Warning) {
      severity = "WARNING";
    }
    else if (diagnosticSeverity == DiagnosticSeverity.Information || diagnosticSeverity == DiagnosticSeverity.Hint) {
      severity = "INFO";
    }
    else {
      severity = "ERROR";
    }
    Either<String, Integer> code = diagnostic.getCode();
    String codeStr = code == null ? null : (code.isLeft() ? code.getLeft() : code.getRight().toString());
    String type;
    if (codeStr != null && codeStr.equalsIgnoreCase("todo")) {
      type = "TODO";
    }
    else if (severity.equals("ERROR")) {
      type = "COMPILE_TIME_ERROR";
    }
    else if (severity.equals("WARNING")) {
      type = "STATIC_WARNING";
    }
    else {
      type = "HINT";
    }
    Location location = resolveLocation(project, uri, diagnostic.getRange());
    List<DiagnosticMessage> contextMessages = null;
    List<DiagnosticRelatedInformation> relatedInformation = diagnostic.getRelatedInformation();
    if (relatedInformation != null) {
      contextMessages = new ArrayList<>();
      for (DiagnosticRelatedInformation info : relatedInformation) {
        Location infoLocation = resolveLocation(project, info.getLocation().getUri(), info.getLocation().getRange());
        contextMessages.add(new DiagnosticMessage(info.getMessage(), infoLocation));
      }
    }
    String url = diagnostic.getCodeDescription() != null ? diagnostic.getCodeDescription().getHref() : null;

    return new AnalysisError(
      severity,
      type,
      location,
      Lsp4jUtilKt.getMessageIfStringOrEmpty(diagnostic),
      null,
      codeStr,
      url,
      contextMessages,
      false
    );
  }

  /**
   * Converts a collection of LSP {@link Diagnostic} objects into DAS {@link AnalysisError} protocol objects.
   */
  public static @NotNull List<AnalysisError> convertDiagnostics(@NotNull Project project,
                                                                @NotNull String uri,
                                                                @NotNull Collection<? extends Diagnostic> diagnostics) {
    List<AnalysisError> result = new ArrayList<>(diagnostics.size());
    for (Diagnostic diagnostic : diagnostics) {
      result.add(convertDiagnosticToAnalysisError(project, uri, diagnostic));
    }
    return result;
  }

  /**
   * @deprecated Use {@link #convertDiagnosticToAnalysisError(Project, String, Diagnostic)} instead
   */
  @Deprecated
  public static @NotNull AnalysisError convertDiagnosticToAnalysisError(@NotNull Project project,
                                                                        @SuppressWarnings("unused") @Nullable DartAnalysisServerService das,
                                                                        @NotNull String uri,
                                                                        @NotNull Diagnostic diagnostic) {
    return convertDiagnosticToAnalysisError(project, uri, diagnostic);
  }

  private static @NotNull Location resolveLocation(@NotNull Project project,
                                                   @NotNull String uri,
                                                   @NotNull Range range) {
    DartFileInfo fileInfo = DartFileInfoKt.getDartFileInfo(project, uri);
    String filePath = fileInfo instanceof DartLocalFileInfo localFileInfo ? localFileInfo.getFilePath() : uri;
    ResolvedOffsets resolved = ReadAction.compute(() -> {
      VirtualFile f = fileInfo.findFile();
      Document doc = f != null ? FileDocumentManager.getInstance().getDocument(f) : null;
      if (doc != null) {
        Integer start = Lsp4jUtilKt.getOffsetInDocument(doc, range.getStart());
        Integer end = Lsp4jUtilKt.getOffsetInDocument(doc, range.getEnd());
        return new ResolvedOffsets(f, start != null ? start : 0, end != null ? end : 0);
      }
      else {
        return new ResolvedOffsets(f, 0, 0);
      }
    });
    DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
    int offset = das.getOriginalOffset(resolved.vFile, resolved.docOffset);
    int length = Math.max(das.getOriginalOffset(resolved.vFile, resolved.docEnd) - offset, 0);
    return new Location(
      filePath,
      offset,
      length,
      Math.max(range.getStart().getLine() + 1, 1),
      Math.max(range.getStart().getCharacter() + 1, 1),
      Math.max(range.getEnd().getLine() + 1, 1),
      Math.max(range.getEnd().getCharacter() + 1, 1)
    );
  }

  private record ResolvedOffsets(@Nullable VirtualFile vFile, int docOffset, int docEnd) {
  }
}
