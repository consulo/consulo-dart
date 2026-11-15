// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.analyzer;

import com.google.dart.server.AnalysisServerSocket;
import com.google.dart.server.DartLspWorkspaceApplyEditRequestConsumer;
import com.google.dart.server.ShowMessageRequestConsumer;
import com.google.dart.server.internal.remote.RemoteAnalysisServerImpl;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.application.Application;
import consulo.dart.notification.DartNotificationGroups;
import consulo.document.Document;
import consulo.document.FileDocumentManager;
import consulo.language.editor.WriteCommandAction;
import consulo.localize.LocalizeValue;
import consulo.logging.Logger;
import consulo.project.Project;
import consulo.project.ui.notification.NotificationService;
import consulo.project.ui.notification.NotificationType;
import consulo.virtualFileSystem.VirtualFile;
import consulo.webBrowser.BrowserUtil;
import org.dartlang.analysis.server.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class DartAnalysisServerImpl extends RemoteAnalysisServerImpl {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartAnalysisServerImpl.class);

  private final @NotNull Project project;

  DartAnalysisServerImpl(@NotNull Project project, @NotNull AnalysisServerSocket socket) {
    super(socket);
    this.project = project;
  }

  @Override
  public void server_openUrlRequest(String url) {
    BrowserUtil.browse(url);
  }

  @Override
  public void server_showMessageRequest(String messageType,
                                        String message,
                                        List<MessageAction> messageActions,
                                        ShowMessageRequestConsumer consumer) {
    NotificationType notificationType = switch (messageType) {
      case MessageType.ERROR -> NotificationType.ERROR;
      case MessageType.WARNING -> NotificationType.WARNING;
      default -> NotificationType.INFORMATION;
    };

    var builder = NotificationService.getInstance()
      .newOfType(DartNotificationGroups.get("Dart Analysis Server"), notificationType)
      .content(LocalizeValue.of(message));
    for (MessageAction messageAction : messageActions) {
      String actionLabel = messageAction.getLabel();
      builder.addClosingAction(LocalizeValue.of(actionLabel), () -> consumer.computedMessageActions(actionLabel));
    }
    builder.notify(project);
  }

  @Override
  public void lsp_workspaceApplyEdit(DartLspApplyWorkspaceEditParams params, DartLspWorkspaceApplyEditRequestConsumer consumer) {
    Application.get().invokeLater(() -> {
      String label = params.getLabel();
      String commandName = label != null ? label : DartBundle.message("code.changes.by.dart.analysis.server");

      boolean applied = false;
      try {
        applied = WriteCommandAction.writeCommandAction(project)
          .withName(LocalizeValue.of(commandName))
          .compute(() -> applyWorkspaceEdit(params.getWorkspaceEdit()));
      }
      finally {
        consumer.workspaceEditApplied(new DartLspApplyWorkspaceEditResult(applied));
      }
    });
  }

  private boolean applyWorkspaceEdit(@NotNull DartLspWorkspaceEdit workspaceEdit) {
    List<DartLspDocumentChange> documentChanges = workspaceEdit.getDocumentChanges();
    if (documentChanges == null) return false;

    for (DartLspDocumentChange change : documentChanges) {
      if (change instanceof DartLspTextDocumentEdit textDocumentEdit) {
        String uri = textDocumentEdit.getTextDocument().getUri();
        VirtualFile virtualFile = DartFileInfoKt.getDartFileInfo(project, uri).findFile();
        if (virtualFile == null) return false;
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        if (document == null) return false;
        if (!DartLspUtilKt.applyTextEdits(document, textDocumentEdit.getEdits())) return false;
      }
      else {
        LOG.warn("Unsupported document change type: " + change.getClass().getSimpleName());
        return false;
      }
    }
    return true;
  }
}
