// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.component.PropertiesComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

/**
 * {@code PropertiesComponent.getList/setList} of IntelliJ, Consulo stores the same values as arrays.
 */
public final class PropertiesComponentCompat {
  private PropertiesComponentCompat() {
  }

  public static @Nullable List<String> getList(@NotNull PropertiesComponent component, @NotNull String name) {
    String[] values = component.getValues(name);
    return values == null ? null : Arrays.asList(values);
  }

  public static void setList(@NotNull PropertiesComponent component, @NotNull String name, @Nullable List<String> values) {
    component.setValues(name, values == null ? null : values.toArray(String[]::new));
  }
}
