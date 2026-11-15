package org.dartlang.analysis.server.protocol;

public final class DartLspApplyWorkspaceEditResult {
  private final boolean applied;

  public DartLspApplyWorkspaceEditResult(boolean applied) {
    this.applied = applied;
  }

  public boolean getApplied() {
    return applied;
  }
}
