package org.dartlang.analysis.server.protocol;

import org.jetbrains.annotations.Nullable;

public final class DartLspVersionedTextDocumentIdentifier {
  private final String uri;
  private final @Nullable Integer version;

  public DartLspVersionedTextDocumentIdentifier(String uri, @Nullable Integer version) {
    this.uri = uri;
    this.version = version;
  }

  public String getUri() {
    return uri;
  }

  public @Nullable Integer getVersion() {
    return version;
  }
}
