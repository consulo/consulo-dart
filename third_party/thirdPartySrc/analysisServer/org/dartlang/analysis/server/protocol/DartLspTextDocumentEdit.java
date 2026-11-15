package org.dartlang.analysis.server.protocol;

import java.util.List;

public final class DartLspTextDocumentEdit implements DartLspDocumentChange {
  private final DartLspVersionedTextDocumentIdentifier textDocument;
  private final List<DartLspTextEdit> edits;

  public DartLspTextDocumentEdit(DartLspVersionedTextDocumentIdentifier textDocument, List<DartLspTextEdit> edits) {
    this.textDocument = textDocument;
    this.edits = edits;
  }

  public DartLspVersionedTextDocumentIdentifier getTextDocument() {
    return textDocument;
  }

  public List<DartLspTextEdit> getEdits() {
    return edits;
  }
}
