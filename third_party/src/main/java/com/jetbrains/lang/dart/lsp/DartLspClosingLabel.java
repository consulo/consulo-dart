/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import org.eclipse.lsp4j.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class DartLspClosingLabel {
  private final @Nullable String label;
  private final @Nullable Range range;

  public DartLspClosingLabel(@Nullable String label, @Nullable Range range) {
    this.label = label;
    this.range = range;
  }

  public @Nullable String getLabel() {
    return label;
  }

  public @Nullable Range getRange() {
    return range;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof DartLspClosingLabel that)) return false;
    return Objects.equals(label, that.label) && Objects.equals(range, that.range);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, range);
  }

  @Override
  public @NotNull String toString() {
    return "DartLspClosingLabel(label=" + label + ", range=" + range + ")";
  }
}
