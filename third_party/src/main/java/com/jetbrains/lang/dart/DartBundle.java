// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart;

import consulo.dart.localize.DartLocalize;
import consulo.localize.LocalizeKey;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;


/**
 * Upstream message keys are served by the generated {@link DartLocalize}
 * (see tool/consulo/properties2localize.py), deprecated messages are merged into it.
 */
public final class DartBundle {
  private DartBundle() {}

  public static @NotNull String message(@NotNull String key, Object @NotNull ... params) {
    return messageValue(key, params).get();
  }

  /**
   * Consulo: {@link LocalizeValue} is a {@code Supplier<String>}, so it is accepted where upstream passes the pointer
   * and also where Consulo APIs want a {@link LocalizeValue}.
   */
  public static @NotNull LocalizeValue messagePointer(@NotNull String key, Object @NotNull ... params) {
    return messageValue(key, params);
  }

  public static @NotNull LocalizeValue messageValue(@NotNull String key, Object @NotNull ... params) {
    LocalizeKey localizeKey = LocalizeKey.of(DartLocalize.ID, key, params.length);
    return params.length == 0 ? localizeKey.getValue() : localizeKey.getValue(params);
  }
}
