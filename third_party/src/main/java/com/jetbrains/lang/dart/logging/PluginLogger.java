/*
 * Copyright 2025 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.logging;

import consulo.container.boot.ContainerPathManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public final class PluginLogger {
  public static final PluginLogger INSTANCE = new PluginLogger();

  private static final String LOG_FILE_NAME = "dash.log";
  private static final String FLUTTER_LOGGER_NAME = "io.flutter";
  private static final int LOG_FILE_SIZE_BYTES = 10 * 1024 * 1024; // 10 MB
  private static final int LOG_FILE_COUNT = 5;
  private static final String LOG_FORMAT = "%1$tF %1$tT %3$s [%4$-7s] %5$s %6$s %n";

  private final Logger rootLogger = Logger.getLogger("com.jetbrains.lang.dart");

  private PluginLogger() {
    String logPath = ContainerPathManager.get().getLogPath().getPath();
    String fullPath = logPath + File.separatorChar + LOG_FILE_NAME;

    synchronized (LogManager.getLogManager()) {
      Logger flutterLogger = Logger.getLogger(FLUTTER_LOGGER_NAME);

      // Check if either logger already has a FileHandler for dash.log
      Handler existingHandler = findFileHandler(rootLogger);
      if (existingHandler == null) {
        existingHandler = findFileHandler(flutterLogger);
      }

      if (existingHandler != null) {
        // Another plugin initialized first; reuse its handler
        if (!Arrays.asList(rootLogger.getHandlers()).contains(existingHandler)) {
          rootLogger.addHandler(existingHandler);
        }
      }
      else {
        // We are the first plugin to initialize; create the handler
        try {
          FileHandler newHandler = new FileHandler(fullPath, LOG_FILE_SIZE_BYTES, LOG_FILE_COUNT, true);
          newHandler.setFormatter(new PluginLogFormatter());

          // Attach to the dart logger so it can be found
          rootLogger.addHandler(newHandler);
        }
        catch (IOException | SecurityException e) {
          // TODO(helin24): Consider adding a backup logging method if this fails at any point.
          consulo.logging.Logger.getInstance(PluginLogger.class).error("Failed to initialize plugin log file handler", e);
        }
      }
    }
  }

  private static FileHandler findFileHandler(@NotNull Logger logger) {
    for (Handler handler : logger.getHandlers()) {
      if (handler instanceof FileHandler fileHandler) {
        return fileHandler;
      }
    }
    return null;
  }

  public @NotNull consulo.logging.Logger createLogger(@NotNull Class<?> logClass) {
    return consulo.logging.Logger.getInstance(logClass.getName());
  }

  private static class PluginLogFormatter extends java.util.logging.Formatter {
    @Override
    public String format(java.util.logging.LogRecord record) {
      return String.format(
        LOG_FORMAT,
        new java.util.Date(record.getMillis()),
        null,
        record.getLoggerName(),
        record.getLevel().getLocalizedName(),
        formatMessage(record),
        record.getThrown() != null ? "\n" + stackTraceToString(record.getThrown()) : ""
      );
    }

    private static String stackTraceToString(Throwable throwable) {
      StringWriter writer = new StringWriter();
      throwable.printStackTrace(new PrintWriter(writer));
      return writer.toString();
    }
  }
}
