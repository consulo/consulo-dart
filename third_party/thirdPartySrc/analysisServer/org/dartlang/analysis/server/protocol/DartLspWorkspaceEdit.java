package org.dartlang.analysis.server.protocol;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public final class DartLspWorkspaceEdit {
  private final @Nullable Map<String, List<DartLspTextEdit>> changes;
  private final @Nullable List<DartLspDocumentChange> documentChanges;

  public DartLspWorkspaceEdit(@Nullable Map<String, List<DartLspTextEdit>> changes, @Nullable List<DartLspDocumentChange> documentChanges) {
    this.changes = changes;
    this.documentChanges = documentChanges;
  }

  public @Nullable Map<String, List<DartLspTextEdit>> getChanges() {
    return changes;
  }

  public @Nullable List<DartLspDocumentChange> getDocumentChanges() {
    return documentChanges;
  }
}
