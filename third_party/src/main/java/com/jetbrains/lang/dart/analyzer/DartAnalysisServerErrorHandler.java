// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.analyzer;

import consulo.container.plugin.PluginDescriptor;
import consulo.ide.impl.idea.ide.plugins.PluginManagerCore;
import com.intellij.notification.*;
import consulo.project.ui.notification.NotificationAction;
import consulo.project.ui.notification.NotificationGroup;
import consulo.project.ui.notification.NotificationType;
import consulo.application.internal.ApplicationInfo;
import consulo.application.ApplicationManager;
import consulo.logging.Logger;
import consulo.container.plugin.PluginId;
import consulo.fileEditor.FileEditorManager;
import consulo.project.Project;
import consulo.util.io.FileUtil;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.logging.PluginLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * A class to log Dart analysis server errors to the IntelliJ event log, and present additional details
 * to users on demand.
 */
public class DartAnalysisServerErrorHandler {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartAnalysisServerErrorHandler.class);

  private static final int MAX_REPORTS_PER_SESSION = 100;

  // NOTIFICATION_GROUP is used to add an error to Event Log tool window.
  private static final NotificationGroup NOTIFICATION_GROUP =
    NotificationGroupManager.getInstance().getNotificationGroup("Dart analysis issue");

  private final @NotNull Project myProject;
  private int issueCount = 0;

  DartAnalysisServerErrorHandler(@NotNull Project project) {
    myProject = project;
  }

  public void handleError(@NotNull String message,
                          @Nullable String stackTrace,
                          boolean isFatal,
                          @Nullable String sdkVersion,
                          @Nullable String debugLog) {
    if (issueCount > MAX_REPORTS_PER_SESSION) {
      return;
    }

    final int issueNumber = ++issueCount;

    String messageOneLine = StringUtil.splitByLines(message)[0];

    String notificationContent = DartBundle.message("notification.content.dart.analysis.issue", messageOneLine);

    // This writes to Event Log tool window but doesn't show a balloon.
    NOTIFICATION_GROUP
      .createNotification(
              DartBundle.message("notification.title.dart.analysis.issue"),
              notificationContent,
              isFatal ? NotificationType.ERROR : NotificationType.WARNING)
      .addAction(NotificationAction.createSimple(DartBundle.message("notification.view.details.action.text"), () -> {
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
          String content = calculateIssueText(messageOneLine, sdkVersion, message, stackTrace, debugLog);
          try {
            // 'issue-1.md'
           File ioFile = FileUtil.createTempFile("issue-" + issueNumber, ".md", true);
           FileUtil.createParentDirs(ioFile);
           FileUtil.writeToFile(ioFile, content);

           VirtualFile file = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(ioFile);
           if (file != null) {
             ApplicationManager.getApplication().invokeLater(() -> {
               FileEditorManager.getInstance(myProject).openFile(file, true);
               }, com.intellij.openapi.application.ModalityState.nonModal(), myProject.getDisposed());
           }
          } catch (IOException ioe) {
            LOG.warn("Error creating issue file", ioe);
          }
        });
      }))
      .notify(myProject);
  }

  private static String calculateIssueText(@NotNull String messageOneLine,
                                           @Nullable String sdkVersion,
                                           @NotNull String message,
                                           @Nullable String stackTrace,
                                           @Nullable String debugLog) {
    StringWriter writer = new StringWriter();
    //noinspection IOResourceOpenedButNotSafelyClosed
    PrintWriter text = new PrintWriter(writer);

    // message
    text.println("## Dart analysis issue");
    text.println("");
    text.println(messageOneLine);
    text.println("");

    // version info
    final ApplicationInfo platform = ApplicationInfo.getInstance();
    final IdeaPluginDescriptor plugin = PluginManagerCore.getPlugin(PluginId.getId("Dart"));

    text.println("## Version information");
    text.println("");
    if (sdkVersion != null) {
      text.println("- Dart SDK " + sdkVersion);
    }
    if (plugin != null) {
      text.println("- Dart IntelliJ " + plugin.getVersion());
    }
    text.println("- " + platform.getVersionName() + " " + platform.getFullVersion());
    text.println("- " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
    text.println("");

    // stacktrace
    text.println("## Details");
    text.println("");
    text.println(message.trim());
    if (stackTrace != null) {
      text.println("");
      text.println("```");
      text.println(stackTrace.trim());
      text.println("```");
    }
    text.println("");

    // debug log
    if (debugLog != null) {
      text.println("## Debug log");
      text.println("");
      text.println("```");
      text.println(debugLog.trim());
      text.println("```");
    }

    return writer.toString();
  }
}
