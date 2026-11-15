package org.dartlang.analysis.server.protocol;

public final class DartLspTextEdit {
  private final DartLspRange range;
  private final String newText;

  public DartLspTextEdit(DartLspRange range, String newText) {
    this.range = range;
    this.newText = newText;
  }

  public DartLspRange getRange() {
    return range;
  }

  public String getNewText() {
    return newText;
  }
}
