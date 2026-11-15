package org.dartlang.analysis.server.protocol;

import org.jetbrains.annotations.Nullable;

public final class DartLspApplyWorkspaceEditParams {
  private final DartLspWorkspaceEdit workspaceEdit;
  private final @Nullable String label;

  public DartLspApplyWorkspaceEditParams(DartLspWorkspaceEdit workspaceEdit, @Nullable String label) {
    this.workspaceEdit = workspaceEdit;
    this.label = label;
  }

  public DartLspWorkspaceEdit getWorkspaceEdit() {
    return workspaceEdit;
  }

  public @Nullable String getLabel() {
    return label;
  }
}
