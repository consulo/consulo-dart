package com.jetbrains.lang.dart.ide.index;

import com.jetbrains.lang.dart.DartComponentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author: Fedor.Korotkov
 */
public class DartComponentInfo {
  @NotNull private final String value;
  @Nullable private final DartComponentType type;

  public DartComponentInfo(@NotNull String value, DartComponentType type) {
    this.value = value;
    this.type = type;
  }

  @NotNull
  public String getValue() {
    return value;
  }

  @Nullable
  public DartComponentType getType() {
    return type;
  }
}
