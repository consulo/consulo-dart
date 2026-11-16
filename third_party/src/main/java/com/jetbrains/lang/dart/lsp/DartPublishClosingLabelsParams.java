/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class DartPublishClosingLabelsParams {
  private final @Nullable String uri;
  private final @Nullable List<DartLspClosingLabel> labels;

  public DartPublishClosingLabelsParams(@Nullable String uri) {
    this(uri, Collections.emptyList());
  }

  public DartPublishClosingLabelsParams(@Nullable String uri, @Nullable List<DartLspClosingLabel> labels) {
    this.uri = uri;
    this.labels = labels;
  }

  public @Nullable String getUri() {
    return uri;
  }

  public @Nullable List<DartLspClosingLabel> getLabels() {
    return labels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DartPublishClosingLabelsParams that)) return false;
    return Objects.equals(uri, that.uri) && Objects.equals(labels, that.labels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uri, labels);
  }

  @Override
  public @NotNull String toString() {
    return "DartPublishClosingLabelsParams(uri=" + uri + ", labels=" + labels + ")";
  }
}
