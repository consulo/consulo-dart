// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import consulo.annotation.access.RequiredReadAction;
import consulo.codeEditor.Caret;
import consulo.codeEditor.Editor;
import consulo.document.Document;
import consulo.fileEditor.FileEditorManager;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Top-level functions of upstream {@code DartToolingDaemonUtils.kt}.
 */
public final class DartToolingDaemonUtilsKt {
  private DartToolingDaemonUtilsKt() {
  }

  @RequiredReadAction
  public static @NotNull JsonObject getActiveLocation(@NotNull Project project, @NotNull DartToolingDaemonService dtdService) {
    JsonObject activeLocationData = new JsonObject();
    JsonArray selectionsArray = new JsonArray();
    activeLocationData.add("selections", selectionsArray);

    Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
    if (editor == null) return activeLocationData;
    Document document = editor.getDocument();
    VirtualFile virtualFile = editor.getVirtualFile();
    if (virtualFile == null || !"dart".equals(virtualFile.getExtension())) return activeLocationData;
    String uri = dtdService.getFileUri(virtualFile);

    JsonObject textDocumentObject = new JsonObject();
    textDocumentObject.addProperty("uri", uri);
    activeLocationData.add("textDocument", textDocumentObject);

    for (Caret caret : editor.getCaretModel().getAllCarets()) {
      int selectionStart = caret.getSelectionStart();
      int selectionEnd = caret.getSelectionEnd();
      int anchorOffset = caret.getOffset() == selectionStart ? selectionEnd : selectionStart;
      int activeOffset = caret.getOffset();

      int[] anchor = getLineAndColumn(document, anchorOffset);
      int[] active = getLineAndColumn(document, activeOffset);

      JsonObject selectionObject = new JsonObject();
      JsonObject anchorObject = new JsonObject();
      anchorObject.addProperty("line", anchor[0]);
      anchorObject.addProperty("character", anchor[1]);
      selectionObject.add("anchor", anchorObject);
      JsonObject activeObject = new JsonObject();
      activeObject.addProperty("line", active[0]);
      activeObject.addProperty("character", active[1]);
      selectionObject.add("active", activeObject);
      selectionsArray.add(selectionObject);
    }

    return activeLocationData;
  }

  private static int[] getLineAndColumn(@NotNull Document document, int offset) {
    int lineNumber = document.getLineNumber(offset);
    return new int[]{lineNumber, offset - document.getLineStartOffset(lineNumber)};
  }
}
