// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.ide.impl.idea.ide.projectView.impl.ProjectViewPaneImpl;
import consulo.application.Application;
import consulo.ui.image.Image;
import consulo.dart.notification.DartNotificationGroups;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceImpl;
import consulo.annotation.component.ServiceAPI;
import com.google.common.annotations.VisibleForTesting;
import consulo.execution.ExecutionUtil;
import consulo.project.ui.view.ProjectView;
import consulo.project.ui.notification.Notification;
import consulo.project.ui.notification.NotificationService;
import consulo.localize.LocalizeValue;
import consulo.project.ui.notification.NotificationType;
import consulo.disposer.Disposable;
import consulo.application.ApplicationManager;
import consulo.ui.ModalityState;
import consulo.application.ReadAction;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.component.persist.StoragePathMacros;
import consulo.project.Project;
import consulo.module.content.ProjectFileIndex;

import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.ui.ex.toolWindow.ToolWindow;
import consulo.project.ui.wm.ToolWindowManager;
import consulo.content.scope.SearchScope;
import consulo.ui.ex.content.Content;
import consulo.ui.ex.awt.util.Alarm;
import consulo.dart.compat.ModalityUiUtil;
import consulo.util.io.PathUtil;
import consulo.application.util.concurrent.AppExecutorUtil;
import consulo.dart.compat.ThreadingAssertions;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerMessages;
import icons.DartIcons;
import org.dartlang.analysis.server.protocol.AnalysisError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@State(
  name = "DartProblemsView",
  storages = @Storage(StoragePathMacros.WORKSPACE_FILE)
)
@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartProblemsView implements PersistentStateComponent<DartProblemsViewSettings>, Disposable {
  public static final String TOOLWINDOW_ID = "Dart Analysis"; // the same as in plugin.xml, this is not a user-visible string

  private static final int TABLE_REFRESH_PERIOD = 300;

  private final Project myProject;
  private final DartProblemsPresentationHelper myPresentationHelper;

  private final Object myLock = new Object(); // use this lock to access myScheduledFilePathToErrors, myCurrentFilePathToErrors, and myAlarm
  private final Map<String, List<? extends AnalysisError>> myScheduledFilePathToErrors = new HashMap<>();
  private final Map<String, List<? extends AnalysisError>> myCurrentFilePathToErrors = new HashMap<>();
  private final Set<String> myFilePathsWithErrors = new HashSet<>();
  private final Map<String, Integer> myFolderPathsWithErrors = new HashMap<>();
  private final Alarm myAlarm;

  private @NotNull Image myCurrentIcon = DartIcons.Dart_13;
  private boolean myAnalysisIsBusy;

  private int myFilesWithErrorsHash;
  private Notification myNotification;
  private boolean myDisabledForSession;

  private final Runnable myUpdateRunnable = new Runnable() {
    @Override
    public void run() {
      if (ProjectViewPaneImpl.ID.equals(ProjectView.getInstance(myProject).getCurrentViewId())) {
        final int hash = getFilePathsWithErrorsHash();
        if (myFilesWithErrorsHash != hash) {
          // refresh red squiggles managed by com.jetbrains.lang.dart.projectView.DartNodeDecorator
          myFilesWithErrorsHash = hash;
          ProjectView.getInstance(myProject).refresh();
        }
      }

      DartProblemsViewPanel panel = getProblemsViewPanel();
      if (panel == null) {
        // Panel not yet created (tool window not opened yet).
        // Errors are preserved in myCurrentFilePathToErrors and will be populated
        // via panelCreated() when the tool window content is created.
        synchronized (myLock) {
          myScheduledFilePathToErrors.clear();
        }
        return;
      }

      final Map<String, List<? extends AnalysisError>> filePathToErrors;
      synchronized (myLock) {
        filePathToErrors = new HashMap<>(myScheduledFilePathToErrors);
        myScheduledFilePathToErrors.clear();
      }

      populatePanel(panel, filePathToErrors);
    }
  };

  private void populatePanel(final @NotNull DartProblemsViewPanel panel,
                             final @NotNull Map<String, List<? extends AnalysisError>> filePathToErrors) {
    ReadAction.nonBlocking(() -> {
        Map<String, List<DartProblem>> filePathToDartProblems = new HashMap<>();

        for (Map.Entry<String, List<? extends AnalysisError>> entry : filePathToErrors.entrySet()) {
          String filePath = entry.getKey();
          List<? extends AnalysisError> analysisErrors = entry.getValue();

          VirtualFile vFile = findVirtualFile(filePath);
          boolean fileOk = vFile != null && ProjectFileIndex.getInstance(myProject).isInContent(vFile);
          List<DartProblem> dartProblems = fileOk
                                           ? ContainerUtil.map(analysisErrors, analysisError -> new DartProblem(myProject, analysisError))
                                           : Collections.emptyList();
          filePathToDartProblems.put(filePath, dartProblems);
        }

        return filePathToDartProblems;
      })
      .expireWith(getInstance(myProject))
      .finishOnUiThread(Application::getNoneModalityState, panel::setErrors)
      .submit(AppExecutorUtil.getAppExecutorService());
  }

  private static @Nullable VirtualFile findVirtualFile(final @NotNull String filePath) {
    VirtualFile vFile = LocalFileSystem.getInstance().findFileByPath(filePath);
    if (vFile == null && ApplicationManager.getApplication().isUnitTestMode()) {
      vFile = VirtualFileManager.getInstance().findFileByUrl("temp://" + filePath);
    }
    return vFile;
  }

  @Inject
  public DartProblemsView(@NotNull Project project) {
    myProject = project;
    myPresentationHelper = new DartProblemsPresentationHelper(project);
    myAlarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD, this);

    project.getMessageBus().connect().subscribe(
      DartAnalysisServerMessages.DART_ANALYSIS_TOPIC, new DartAnalysisServerMessages.DartAnalysisNotifier() {
        @Override
        public void analysisStarted() {
          myAnalysisIsBusy = true;
          ModalityUiUtil.invokeLaterIfNeeded(ModalityState.nonModal(), myProject::isDisposed, () -> updateIcon());
        }

        @Override
        public void analysisFinished() {
          myAnalysisIsBusy = false;
          ModalityUiUtil.invokeLaterIfNeeded(ModalityState.nonModal(), myProject::isDisposed, () -> updateIcon());
        }
      }
    );
  }

  @VisibleForTesting
  public @NotNull DartProblemsPresentationHelper getPresentationHelper() {
    return myPresentationHelper;
  }

  private @Nullable ToolWindow getDartAnalysisToolWindow() {
    ToolWindow toolWindow = ToolWindowManager.getInstance(myProject).getToolWindow(TOOLWINDOW_ID);
    if (toolWindow != null && !toolWindow.isAvailable()) {
      ModalityUiUtil.invokeLaterIfNeeded(ModalityState.nonModal(), myProject::isDisposed, () -> {
        ToolWindow tw = ToolWindowManager.getInstance(myProject).getToolWindow(TOOLWINDOW_ID);
        if (tw != null && !tw.isAvailable()) {
          tw.setAvailable(true);
        }
      });
    }
    return toolWindow;
  }

  private @Nullable DartProblemsViewPanel getProblemsViewPanel() {
    ToolWindow toolWindow = getDartAnalysisToolWindow();
    Content content = toolWindow != null ? toolWindow.getContentManager().getContent(0) : null;
    return content != null ? (DartProblemsViewPanel)content.getComponent() : null;
  }

  // Inlined from `ActionCenter` (where it was removed).
  // see: https://github.com/flutter/dart-intellij-third-party/issues/80
  private static @Nullable ToolWindow getToolWindow(@Nullable Project project) {
    return project == null ? null : ToolWindowManager.getInstance(project).getToolWindow("Notifications");
  }

  void setTabTitle(@NotNull String tabTitle) {
    ToolWindow toolWindow = getDartAnalysisToolWindow();
    Content content = toolWindow != null ? toolWindow.getContentManager().getContent(0) : null;
    if (content != null) {
      content.setDisplayName(tabTitle);
    }
  }

  void setToolWindowIcon(@NotNull Image icon) {
    myCurrentIcon = icon;
    updateIcon();
  }

  private void updateIcon() {
    ToolWindow toolWindow = getDartAnalysisToolWindow();
    if (toolWindow == null) return;

    if (myAnalysisIsBusy) {
      toolWindow.setIcon(ExecutionUtil.getIconWithLiveIndicator(myCurrentIcon));
    }
    else {
      toolWindow.setIcon(myCurrentIcon);
    }
  }

  public static @NotNull DartProblemsView getInstance(final @NotNull Project project) {
    return project.getInstance(DartProblemsView.class);
  }

  public VirtualFile getCurrentFile() {
    return myPresentationHelper.getCurrentFile();
  }

  public void showErrorNotificationTerse(@NotNull String title) {
    showNotification(NotificationType.ERROR, title, null, null, true, false);
  }

  public void showErrorNotification(@NotNull String title,
                                    @Nullable String content,
                                    @Nullable Image icon,
                                    boolean addOpenAnalysisAction) {
    showNotification(NotificationType.ERROR, title, content, icon, false, addOpenAnalysisAction);
  }

  public void clearNotifications() {
    if (myNotification != null) {
      myNotification.expire();
      myNotification = null;
    }
  }

  private void showNotification(@NotNull NotificationType notificationType,
                                @NotNull String title,
                                @Nullable String content,
                                @Nullable Image icon,
                                boolean terse,
                                boolean addOpenAnalysisAction) {
    clearNotifications();

    if (myDisabledForSession) return;

    final Notification.Builder builder = NotificationService.getInstance()
      .newOfType(DartNotificationGroups.get("Dart Analysis"), notificationType)
      .title(LocalizeValue.of(title))
      .content(LocalizeValue.of(StringUtil.notNullize(content)));

    if (addOpenAnalysisAction) {
      builder.addClosingAction(DartBundle.messageValue("notification.open.dart.analysis.action.text"), () -> {
        ToolWindow toolWindow = getDartAnalysisToolWindow();
        if (toolWindow != null) {
          toolWindow.activate(null);
        }
      });
    }

    if (!terse) {
      builder.addClosingAction(DartBundle.messageValue("notification.don.t.show.for.this.session.action.text"),
                               () -> myDisabledForSession = true);

      builder.addClosingAction(DartBundle.messageValue("notification.never.show.again.action.text"), () -> {
        // Consulo: NotificationsConfiguration is internal, the group display type can not be changed to NONE;
        // the notifications are disabled for this session instead
        myDisabledForSession = true;

        NotificationService.getInstance()
          .newInfo(DartNotificationGroups.get("Dart Analysis"))
          .title(DartBundle.messageValue("notification.title.warning.disabled"))
          .content(DartBundle.messageValue("notification.content.you.can.enable.it.back.in.the.settings", "Notifications"))
          .addClosingAction(DartBundle.messageValue("notification.restore.settings.action.text", "Notifications"), () -> {
            final ToolWindow toolWindow = getToolWindow(myProject);
            if (toolWindow != null) toolWindow.activate(null);
          })
          .notify(myProject);
      });
    }

    if (icon != null) {
      builder.icon(icon);
    }

    myNotification = builder.notifyAndGet(myProject);
  }

  @Override
  public DartProblemsViewSettings getState() {
    return myPresentationHelper.getSettings();
  }

  @Override
  public void loadState(@NotNull DartProblemsViewSettings state) {
    myPresentationHelper.setSettings(state);
  }

  /**
   * Unlike {@link #setCurrentFile(VirtualFile)} this method may be called not from the EDT
   */
  public void setInitialCurrentFileBeforeServerStart(final @Nullable VirtualFile file) {
    myPresentationHelper.setCurrentFile(file);
  }

  public void setCurrentFile(final @Nullable VirtualFile file) {
    ThreadingAssertions.assertEventDispatchThread();

    // Calling getProblemsViewPanel() here also ensures that the tool window contents becomes visible when Analysis server starts
    DartProblemsViewPanel panel = getProblemsViewPanel();
    if (panel != null &&
        myPresentationHelper.setCurrentFile(file) &&
        myPresentationHelper.getFileFilterMode() != DartProblemsViewSettings.FileFilterMode.All) {
      panel.fireGroupingOrFilterChanged();
    }
  }

  public void updateErrorsForFile(final @NotNull String filePath, @NotNull List<? extends AnalysisError> errors) {
    synchronized (myLock) {
      if (errors.isEmpty()) {
        myCurrentFilePathToErrors.remove(filePath);
      }
      else {
        myCurrentFilePathToErrors.put(filePath, errors);
      }

      boolean hasSevereProblems = false;
      for (AnalysisError error : errors) {
        if ("ERROR".equals(error.getSeverity())) {
          hasSevereProblems = true;
          break;
        }
      }

      if (hasSevereProblems) {
        if (myFilePathsWithErrors.add(filePath)) {
          String parentPath = PathUtil.getParentPath(filePath);
          while (!parentPath.isEmpty()) {
            int count = myFolderPathsWithErrors.getOrDefault(parentPath, 0);
            myFolderPathsWithErrors.put(parentPath, count + 1);
            parentPath = PathUtil.getParentPath(parentPath);
          }
        }
      }
      else {
        if (myFilePathsWithErrors.remove(filePath)) {
          String parentPath = PathUtil.getParentPath(filePath);
          while (!parentPath.isEmpty()) {
            Integer count = myFolderPathsWithErrors.get(parentPath);
            if (count != null) {
              if (count > 1) {
                myFolderPathsWithErrors.put(parentPath, count - 1);
              }
              else {
                myFolderPathsWithErrors.remove(parentPath);
              }
            }
            parentPath = PathUtil.getParentPath(parentPath);
          }
        }
      }

      if (myScheduledFilePathToErrors.isEmpty()) {
        myAlarm.addRequest(myUpdateRunnable, TABLE_REFRESH_PERIOD, ModalityState.nonModal());
      }

      myScheduledFilePathToErrors.put(filePath, errors);
    }
  }

  public void panelCreated(final @NotNull DartProblemsViewPanel panel) {
    final Map<String, List<? extends AnalysisError>> snapshot;
    synchronized (myLock) {
      myAlarm.cancelAllRequests();
      myScheduledFilePathToErrors.clear();
      if (myCurrentFilePathToErrors.isEmpty()) {
        return;
      }
      snapshot = new HashMap<>(myCurrentFilePathToErrors);
    }
    populatePanel(panel, snapshot);
  }

  public @NotNull Map<String, List<? extends AnalysisError>> getCurrentFilePathToErrors() {
    synchronized (myLock) {
      return new HashMap<>(myCurrentFilePathToErrors);
    }
  }

  public boolean hasErrorsInScope(final @NotNull SearchScope scope) {
    final Set<String> filePaths;
    synchronized (myLock) {
      if (myFilePathsWithErrors.isEmpty()) {
        return false;
      }
      filePaths = new HashSet<>(myFilePathsWithErrors);
    }
    for (String filePath : filePaths) {
      VirtualFile vFile = findVirtualFile(filePath);
      if (vFile != null && scope.contains(vFile)) {
        return true;
      }
    }
    return false;
  }

  public boolean isFileWithErrors(final @NotNull VirtualFile file) {
    synchronized (myLock) {
      return file.isDirectory()
             ? myFolderPathsWithErrors.getOrDefault(file.getPath(), 0) > 0
             : myFilePathsWithErrors.contains(file.getPath());
    }
  }

  public int getFilePathsWithErrorsHash() {
    synchronized (myLock) {
      return myFilePathsWithErrors.hashCode();
    }
  }

  public void clearAll() {
    ThreadingAssertions.assertEventDispatchThread();

    ProjectView.getInstance(myProject).refresh(); // refresh red waves managed by com.jetbrains.lang.dart.projectView.DartNodeDecorator

    synchronized (myLock) {
      myAlarm.cancelAllRequests();
      myScheduledFilePathToErrors.clear();
      myCurrentFilePathToErrors.clear();
      myFilePathsWithErrors.clear();
      myFolderPathsWithErrors.clear();
    }

    DartProblemsViewPanel panel = getProblemsViewPanel();
    if (panel != null) {
      panel.clearAll();
    }
  }

  @Override
  public void dispose() {}
}
