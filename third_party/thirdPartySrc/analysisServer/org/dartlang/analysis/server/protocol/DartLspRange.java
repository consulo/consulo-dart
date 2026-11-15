package org.dartlang.analysis.server.protocol;

public final class DartLspRange {
  private final DartLspPosition start;
  private final DartLspPosition end;

  public DartLspRange(DartLspPosition start, DartLspPosition end) {
    this.start = start;
    this.end = end;
  }

  public DartLspPosition getStart() {
    return start;
  }

  public DartLspPosition getEnd() {
    return end;
  }
}
