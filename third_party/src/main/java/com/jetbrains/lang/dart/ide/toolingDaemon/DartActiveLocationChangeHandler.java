// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.google.gson.JsonObject;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.annotation.access.RequiredReadAction;
import consulo.application.ReadAction;
import consulo.codeEditor.EditorFactory;
import consulo.codeEditor.event.CaretEvent;
import consulo.codeEditor.event.CaretListener;
import consulo.codeEditor.event.EditorEventMulticaster;
import consulo.codeEditor.event.SelectionEvent;
import consulo.codeEditor.event.SelectionListener;
import consulo.document.event.DocumentEvent;
import consulo.document.event.DocumentListener;
import consulo.fileEditor.FileEditorManager;
import consulo.fileEditor.event.FileEditorManagerEvent;
import consulo.fileEditor.event.FileEditorManagerListener;
import consulo.logging.Logger;
import consulo.ui.ex.awt.util.Alarm;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Consulo: upstream debounces the events with a coroutine flow, here an {@link Alarm} is used with the same 300 ms delay.
 */
final class DartActiveLocationChangeHandler {
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DartActiveLocationChangeHandler.class);
  private static final int DEBOUNCE_MILLIS = 300;

  private final @NotNull DartToolingDaemonService dtdService;
  private final @NotNull Alarm alarm;
  private volatile boolean activeLocationNullSent = false;

  DartActiveLocationChangeHandler(@NotNull DartToolingDaemonService dtdService) {
    this.dtdService = dtdService;
    this.alarm = new Alarm(Alarm.ThreadToUse.POOLED_THREAD, dtdService);

    Listener listener = new Listener();
    EditorEventMulticaster multicaster = EditorFactory.getInstance().getEventMulticaster();
    multicaster.addDocumentListener(listener, dtdService);
    multicaster.addCaretListener(listener, dtdService);
    multicaster.addSelectionListener(listener, dtdService);
    dtdService.getProject().getMessageBus().connect(dtdService).subscribe(FileEditorManagerListener.class, listener);
  }

  private void emitActiveLocationChange() {
    alarm.cancelAllRequests();
    alarm.addRequest(this::doSendActiveLocationChangeEvent, DEBOUNCE_MILLIS);
  }

  private void doSendActiveLocationChangeEvent() {
    if (!dtdService.getWebSocketReady()) return;

    JsonObject paramsObject = ReadAction.compute(this::calcActiveLocationChangeParams);
    JsonObject eventData = paramsObject.getAsJsonObject("eventData");
    boolean activeLocationNull = eventData == null || eventData.getAsJsonObject("textDocument") == null;
    if (!activeLocationNull || !activeLocationNullSent) {
      activeLocationNullSent = activeLocationNull;
      try {
        dtdService.sendRequest("postEvent", paramsObject, false, response -> { });
      }
      catch (Exception e) {
        logger.warn("Failed to send activeLocationChanged event", e);
      }
    }
  }

  @RequiredReadAction
  private @NotNull JsonObject calcActiveLocationChangeParams() {
    JsonObject paramsObject = new JsonObject();
    paramsObject.addProperty("streamId", "Editor");
    paramsObject.addProperty("eventKind", "activeLocationChanged");
    JsonObject activeLocation = DartToolingDaemonUtilsKt.getActiveLocation(dtdService.getProject(), dtdService);
    paramsObject.add("eventData", activeLocation);
    return paramsObject;
  }

  private final class Listener implements DocumentListener, CaretListener, SelectionListener, FileEditorManagerListener {
    @Override
    public void documentChanged(@NotNull DocumentEvent event) {
      emitActiveLocationChange();
    }

    @Override
    public void caretPositionChanged(@NotNull CaretEvent event) {
      emitActiveLocationChange();
    }

    @Override
    public void caretAdded(@NotNull CaretEvent event) {
      emitActiveLocationChange();
    }

    @Override
    public void caretRemoved(@NotNull CaretEvent event) {
      emitActiveLocationChange();
    }

    @Override
    public void selectionChanged(@NotNull SelectionEvent e) {
      emitActiveLocationChange();
    }

    @Override
    public void fileOpened(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
      emitActiveLocationChange();
    }

    @Override
    public void fileClosed(@NotNull FileEditorManager source, @NotNull VirtualFile file) {
      emitActiveLocationChange();
    }

    @Override
    public void selectionChanged(@NotNull FileEditorManagerEvent event) {
      emitActiveLocationChange();
    }
  }
}
