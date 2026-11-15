package org.dartlang.analysis.server.protocol;

public final class DartLspPosition {
  private final int line;
  private final int character;

  public DartLspPosition(int line, int character) {
    this.line = line;
    this.character = character;
  }

  public int getLine() {
    return line;
  }

  public int getCharacter() {
    return character;
  }
}
