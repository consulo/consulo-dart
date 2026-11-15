// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.platform.CpuArchitecture;
import consulo.platform.Platform;

/**
 * Subset of {@code com.intellij.util.system.CpuArch} of IntelliJ, backed by {@link Platform}.
 */
public final class CpuArch {
  private CpuArch() {
  }

  public static boolean isArm64() {
    return Platform.current().jvm().arch() == CpuArchitecture.AARCH64;
  }
}
