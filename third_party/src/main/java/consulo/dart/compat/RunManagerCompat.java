// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.execution.RunManager;
import consulo.execution.RunnerAndConfigurationSettings;
import consulo.execution.configuration.ConfigurationType;
import consulo.execution.configuration.ConfigurationTypeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * {@code RunManager.createConfiguration(String, Class)} of IntelliJ.
 */
public final class RunManagerCompat {
  private RunManagerCompat() {
  }

  public static @NotNull RunnerAndConfigurationSettings createConfiguration(@NotNull RunManager runManager,
                                                                            @NotNull String name,
                                                                            @NotNull Class<? extends ConfigurationType> typeClass) {
    return runManager.createRunConfiguration(name, ConfigurationTypeUtil.findConfigurationType(typeClass).getConfigurationFactories()[0]);
  }
}
