// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.toolingDaemon;

import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkUpdateChecker;
import com.jetbrains.lang.dart.sdk.DartSdkUtil;
import consulo.process.cmd.GeneralCommandLine;
import consulo.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Top-level functions of upstream {@code DartToolingDaemonCommandLine.kt}.
 */
public final class DartToolingDaemonCommandLineKt {
  private static final List<String> DTD_COMMAND_LINE_PARAMETERS = List.of(
    "tooling-daemon",
    "--machine"
  );

  private static final String DTD_PING_INTERVAL_PARAMETER_PREFIX = "--ping-interval=";
  private static final String MIN_DTD_PING_INTERVAL_SDK_VERSION = "3.11.0";

  private static final Pattern DTD_COMMAND_LINE_PATTERN =
    Pattern.compile(".+\\btooling-daemon --machine(?: --ping-interval=\\d+)?");

  private DartToolingDaemonCommandLineKt() {
  }

  public static @NotNull GeneralCommandLine createDtdCommandLine(@NotNull DartSdk sdk) {
    return createDtdCommandLine(sdk, 15);
  }

  public static @NotNull GeneralCommandLine createDtdCommandLine(@NotNull DartSdk sdk, int pingInterval) {
    GeneralCommandLine commandLine = new GeneralCommandLine().withWorkDirectory(sdk.getHomePath());
    commandLine.setExePath(FileUtil.toSystemDependentName(DartSdkUtil.getDartExePath(sdk)));
    commandLine.setCharset(StandardCharsets.UTF_8);
    DTD_COMMAND_LINE_PARAMETERS.forEach(commandLine::addParameter);
    if (supportsDtdPingInterval(sdk.getVersion())) {
      commandLine.addParameter(DTD_PING_INTERVAL_PARAMETER_PREFIX + pingInterval);
    }
    return commandLine;
  }

  static boolean supportsDtdPingInterval(@NotNull String sdkVersion) {
    if (sdkVersion.isBlank() || !Character.isDigit(sdkVersion.charAt(0))) {
      return false;
    }
    return DartSdkUpdateChecker.compareDartSdkVersions(sdkVersion, MIN_DTD_PING_INTERVAL_SDK_VERSION) >= 0;
  }

  public static boolean isDtdCommandLine(@NotNull String text) {
    return DTD_COMMAND_LINE_PATTERN.matcher(text).matches();
  }
}
