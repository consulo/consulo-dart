// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.actions;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartStartupActivityKt;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.flutter.FlutterUtil;
import com.jetbrains.lang.dart.ide.runner.DartConsoleFilter;
import com.jetbrains.lang.dart.ide.runner.DartRelativePathsConsoleFilter;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.sdk.DartSdkUtil;
import com.jetbrains.lang.dart.util.ProcessAdapter;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import consulo.application.AllIcons;
import consulo.application.ApplicationManager;
import consulo.application.CommonBundle;
import consulo.application.ReadAction;
import consulo.application.dumb.DumbAware;
import consulo.dart.compat.StopProcessAction;
import consulo.dart.notification.DartNotificationGroups;
import consulo.disposer.Disposer;
import consulo.document.FileDocumentManager;
import consulo.execution.ui.console.ConsoleView;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.execution.ui.console.TextConsoleBuilderFactory;
import consulo.execution.ui.console.UrlFilter;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.module.Module;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.event.ProcessEvent;
import consulo.process.localize.ProcessLocalize;
import consulo.project.Project;
import consulo.project.ui.notification.NotificationService;
import consulo.project.ui.view.MessageView;
import consulo.project.ui.wm.ToolWindow;
import consulo.project.ui.wm.ToolWindowId;
import consulo.project.ui.wm.ToolWindowManager;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.*;
import consulo.ui.ex.awt.Messages;
import consulo.ui.ex.awt.SimpleToolWindowPanel;
import consulo.ui.ex.content.Content;
import consulo.ui.ex.content.ContentFactory;
import consulo.ui.ex.content.ContentManager;
import consulo.ui.ex.localize.UILocalize;
import consulo.util.dataholder.Key;
import consulo.util.io.FileUtil;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DartPubActionBase extends AnAction implements DumbAware, AnActionWithSyncUpdate {
  public static final String PUB_ENV_VAR_NAME = "PUB_ENVIRONMENT";

  private static final Key<PubToolWindowContentInfo> PUB_TOOL_WINDOW_CONTENT_INFO_KEY = Key.create("PUB_TOOL_WINDOW_CONTENT_INFO_KEY");

  private static final AtomicBoolean ourInProgress = new AtomicBoolean(false);

  @Override
  public void update(@NotNull AnActionEvent e) {
    final Pair<Module, VirtualFile> moduleAndPubspec = getModuleAndPubspecYamlFile(e);
    // Defer to the Flutter plugin if appropriate.
    final boolean visible = moduleAndPubspec != null &&
                            !(FlutterUtil.isFlutterPluginInstalled() && FlutterUtil.isPubspecDeclaringFlutter(moduleAndPubspec.second));
    e.getPresentation().setVisible(visible);
    e.getPresentation().setEnabled(visible && !isInProgress());
  }

  protected abstract @NotNull String getTitle(@NotNull VirtualFile pubspecYamlFile);

  protected abstract String @Nullable [] calculatePubParameters(@NotNull Project project, @NotNull VirtualFile pubspecYamlFile);

  @RequiredUIAccess
  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    final Pair<Module, VirtualFile> moduleAndPubspec = getModuleAndPubspecYamlFile(e);
    if (moduleAndPubspec == null) return;

    performPubAction(moduleAndPubspec.first, moduleAndPubspec.second, true);
  }

  public void performPubAction(@NotNull Module module, @NotNull VirtualFile pubspecYamlFile, boolean allowModalDialogs) {
    DartSdk sdk = DartSdk.getDartSdk(module.getProject());

    if (sdk == null && allowModalDialogs) {
      final int answer = Messages.showDialog(module.getProject(),
                                             DartBundle.message("dart.sdk.is.not.configured"),
                                             getTitle(pubspecYamlFile),
                                             new String[]{DartBundle.message("setup.dart.sdk"), CommonBundle.getCancelButtonText()},
                                             Messages.OK,
                                             Messages.getErrorIcon());
      if (answer != Messages.OK) return;

      DartConfigurable.openDartSettings(module.getProject());
      sdk = DartSdk.getDartSdk(module.getProject());
    }

    if (sdk == null) return;

    final File exeFile = new File(DartSdkUtil.getDartExePath(sdk));

    if (!exeFile.isFile()) {
      if (allowModalDialogs) {
        final int answer = Messages.showDialog(module.getProject(),
                                               DartBundle.message("dart.sdk.bad.dartpub.path", exeFile.getPath()),
                                               getTitle(pubspecYamlFile),
                                               new String[]{DartBundle.message("setup.dart.sdk"), CommonBundle.getCancelButtonText()},
                                               Messages.OK,
                                               Messages.getErrorIcon());
        if (answer == Messages.OK) DartConfigurable.openDartSettings(module.getProject());
      }

      return;
    }

    final String[] pubParameters = calculatePubParameters(module.getProject(), pubspecYamlFile);

    if (pubParameters != null) {
      final GeneralCommandLine command = new GeneralCommandLine().withWorkDirectory(Path.of(pubspecYamlFile.getParent().getPath()).toString());

      if (FlutterUtil.isFlutterModule(module)) {
        final String flutterRoot = FlutterUtil.getFlutterRoot(sdk.getHomePath());
        if (flutterRoot != null) {
          command.getEnvironment().put("FLUTTER_ROOT", FileUtil.toSystemDependentName(flutterRoot));
        }
      }

      setupPubExePath(command, sdk);
      command.addParameters(pubParameters);

      doPerformPubAction(module, pubspecYamlFile, command, getTitle(pubspecYamlFile));
    }
  }

  private static final class PubToolWindowContentInfo {
    private final @NotNull Module module;
    private final @NotNull VirtualFile pubspecYamlFile;
    private final @NotNull GeneralCommandLine command;
    private final @NotNull String actionTitle;
    private final @NotNull ConsoleView console;
    private @Nullable RerunPubCommandAction rerunPubCommandAction;
    private @Nullable StopProcessAction stopProcessAction;

    private PubToolWindowContentInfo(@NotNull Module module,
                                     @NotNull VirtualFile pubspecYamlFile,
                                     @NotNull GeneralCommandLine command,
                                     @NotNull String actionTitle,
                                     @NotNull ConsoleView console) {
      this.module = module;
      this.pubspecYamlFile = pubspecYamlFile;
      this.command = command;
      this.actionTitle = actionTitle;
      this.console = console;
    }

    @Override
    public boolean equals(Object other) {
      return other instanceof PubToolWindowContentInfo info && command == info.command;
    }

    @Override
    public int hashCode() {
      return command.hashCode();
    }
  }

  private static final class RerunPubCommandAction extends DumbAwareAction implements AnActionWithSyncUpdate {
    private final @NotNull PubToolWindowContentInfo myInfo;
    private @Nullable ProcessHandler myProcessHandler;

    private RerunPubCommandAction(@NotNull PubToolWindowContentInfo info) {
      super(DartBundle.messageValue("rerun.pub.command.action.name"),
            DartBundle.messageValue("rerun.pub.command.action.description"),
            AllIcons.Actions.Execute);
      myInfo = info;
      registerCustomShortcutSet(CommonShortcuts.getRerun(), info.console.getComponent());
    }

    private void setProcessHandler(@NotNull ProcessHandler processHandler) {
      myProcessHandler = processHandler;
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
      e.getPresentation().setEnabled(!isInProgress() && myProcessHandler != null && myProcessHandler.isProcessTerminated());
    }

    @RequiredUIAccess
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      doPerformPubAction(myInfo.module, myInfo.pubspecYamlFile, myInfo.command, myInfo.actionTitle);
    }
  }

  public static void setupPubExePath(@NotNull GeneralCommandLine commandLine, @NotNull DartSdk dartSdk) {
    commandLine.withExePath(FileUtil.toSystemDependentName(DartSdkUtil.getDartExePath(dartSdk)));
    commandLine.addParameter("pub");
  }

  public static @NotNull String getPubEnvValue() {
    final String clientId = DartAnalysisServerService.getClientId();
    final String existing = System.getenv(PUB_ENV_VAR_NAME);
    return existing != null ? existing + ":" + clientId : clientId;
  }

  public static boolean isInProgress() {
    return ourInProgress.get();
  }

  @SuppressWarnings("unused") // external plugins compatibility
  public static void setIsInProgress(boolean inProgress) {
    ourInProgress.set(inProgress);
  }

  private static @Nullable Pair<Module, VirtualFile> getModuleAndPubspecYamlFile(@NotNull AnActionEvent e) {
    final Module module = e.getData(Module.KEY);
    final PsiFile psiFile = e.getData(PsiFile.KEY);

    if (module != null && psiFile != null && psiFile.getName().equalsIgnoreCase(PubspecYamlUtil.PUBSPEC_YAML)) {
      final VirtualFile file = psiFile.getOriginalFile().getVirtualFile();
      return file != null ? Pair.create(module, file) : null;
    }
    return null;
  }

  private static void doPerformPubAction(@NotNull Module module,
                                         @NotNull VirtualFile pubspecYamlFile,
                                         @NotNull GeneralCommandLine command,
                                         @NotNull String actionTitle) {
    FileDocumentManager.getInstance().saveAllDocuments();

    try {
      if (ourInProgress.compareAndSet(false, true)) {
        command.withEnvironment(PUB_ENV_VAR_NAME, getPubEnvValue());

        final ProcessHandler processHandler = ProcessHandlerBuilder.create(command).build();

        processHandler.addProcessListener(new ProcessAdapter() {
          @Override
          public void processTerminated(@NotNull ProcessEvent event) {
            ourInProgress.set(false);

            ApplicationManager.getApplication().invokeLater(() -> {
              if (!module.isDisposed()) {
                DartStartupActivityKt.excludeBuildAndToolCacheFolders(module, pubspecYamlFile);

                // refresh later than exclude, otherwise IDE may start indexing excluded folders
                VirtualFileUtil.markDirtyAndRefresh(true, true, true, pubspecYamlFile.getParent());

                if (DartSdkLibUtil.isDartSdkEnabled(module)) {
                  DartAnalysisServerService.getInstance(module.getProject()).serverReadyForRequest();
                }
              }
            });
          }
        });

        // Consulo: no coroutine scope, the message view is initialized on the UI thread
        MessageView.getInstance(module.getProject())
          .runWhenInitialized(() -> showPubOutputConsole(module, command, processHandler, pubspecYamlFile, actionTitle));
      }
    }
    catch (ExecutionException e) {
      ourInProgress.set(false);

      // maybe better show it in Messages tool window console?
      NotificationService.getInstance()
        .newError(DartNotificationGroups.DART_PUB_TOOL)
        .title(LocalizeValue.of(actionTitle))
        .content(DartBundle.messageValue("dart.pub.exception", e.getMessage()))
        .notify(null);
    }
    catch (Throwable t) {
      ourInProgress.set(false);
      throw t;
    }
  }

  private static void showPubOutputConsole(@NotNull Module module,
                                           @NotNull GeneralCommandLine command,
                                           @NotNull ProcessHandler processHandler,
                                           @NotNull VirtualFile pubspecYamlFile,
                                           @NotNull String actionTitle) {
    final Project project = module.getProject();
    final MessageView messageView = MessageView.getInstance(project);

    final ConsoleView console;
    PubToolWindowContentInfo info = findExistingInfoForCommand(messageView, command);

    if (info != null) {
      // rerunning the same pub command in the same tool window tab (corresponding tool window action invoked)
      console = info.console;
      console.clear();
    }
    else {
      console = ReadAction.compute(() -> createConsole(project, pubspecYamlFile));
      info = new PubToolWindowContentInfo(module, pubspecYamlFile, command, actionTitle, console);

      final ActionToolbar actionToolbar = createToolWindowActionsBar(info);

      final SimpleToolWindowPanel toolWindowPanel = new SimpleToolWindowPanel(false, true);
      toolWindowPanel.setContent(console.getComponent());
      toolWindowPanel.setToolbar(actionToolbar.getComponent());

      final Content content = ContentFactory.getInstance().createContent(toolWindowPanel.getComponent(), actionTitle, true);
      content.putUserData(PUB_TOOL_WINDOW_CONTENT_INFO_KEY, info);
      Disposer.register(content, console);

      final ContentManager contentManager = messageView.getContentManager();
      removeOldTabs(contentManager);
      contentManager.addContent(content);
      contentManager.setSelectedContent(content);

      final ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow(ToolWindowId.MESSAGES_WINDOW);
      if (toolWindow != null) {
        toolWindow.activate(null, true);
      }
    }

    info.rerunPubCommandAction.setProcessHandler(processHandler);
    info.stopProcessAction.setProcessHandler(processHandler);

    processHandler.addProcessListener(new ProcessAdapter() {
      @Override
      public void processTerminated(@NotNull ProcessEvent event) {
        console.print(ProcessLocalize.finishedWithExitCodeTextMessage(event.getExitCode()).get(), ConsoleViewContentType.SYSTEM_OUTPUT);
      }
    });

    console.print(DartBundle.message("working.dir.0", FileUtil.toSystemDependentName(pubspecYamlFile.getParent().getPath())) + "\n",
                  ConsoleViewContentType.SYSTEM_OUTPUT);
    console.attachToProcess(processHandler);
    processHandler.startNotify();
  }

  private static @Nullable PubToolWindowContentInfo findExistingInfoForCommand(@NotNull MessageView messageView,
                                                                               @NotNull GeneralCommandLine command) {
    for (Content content : messageView.getContentManager().getContents()) {
      final PubToolWindowContentInfo info = content.getUserData(PUB_TOOL_WINDOW_CONTENT_INFO_KEY);
      if (info != null && info.command == command) return info;
    }
    return null;
  }

  private static @NotNull ConsoleView createConsole(@NotNull Project project, @NotNull VirtualFile pubspecYamlFile) {
    final TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
    consoleBuilder.setViewer(true);
    consoleBuilder.addFilter(new DartConsoleFilter(project, pubspecYamlFile));
    consoleBuilder.addFilter(new DartRelativePathsConsoleFilter(project, pubspecYamlFile.getParent().getPath()));
    consoleBuilder.addFilter(new UrlFilter());
    return consoleBuilder.getConsole();
  }

  private static @NotNull ActionToolbar createToolWindowActionsBar(@NotNull PubToolWindowContentInfo info) {
    final ActionManager actionManager = ActionManager.getInstance();
    final DefaultActionGroup actionGroup = new DefaultActionGroup();

    final RerunPubCommandAction rerunPubCommandAction = new RerunPubCommandAction(info);
    info.rerunPubCommandAction = rerunPubCommandAction;
    actionGroup.addAction(rerunPubCommandAction);

    final StopProcessAction stopProcessAction =
      new StopProcessAction(DartBundle.messageValue("stop.pub.process.action"), DartBundle.messageValue("stop.pub.process.action"), null);
    info.stopProcessAction = stopProcessAction;
    actionGroup.addAction(stopProcessAction);

    actionGroup.add(actionManager.getAction(IdeActions.ACTION_PIN_ACTIVE_TAB));

    // Consulo: CloseActiveTabAction is internal, the registered action is wrapped to change its presentation
    final AnAction closeContentAction = EmptyAction.wrap(actionManager.getAction(IdeActions.ACTION_CLOSE_ACTIVE_TAB));
    closeContentAction.getTemplatePresentation().setIcon(AllIcons.Actions.Cancel);
    closeContentAction.getTemplatePresentation().setTextValue(UILocalize.tabbedPaneCloseTabActionName());
    actionGroup.add(closeContentAction);

    final ActionToolbar toolbar = actionManager.createActionToolbar("DartPubAction", actionGroup, false);
    toolbar.setTargetComponent(info.console.getComponent());
    return toolbar;
  }

  private static void removeOldTabs(@NotNull ContentManager contentManager) {
    for (Content content : contentManager.getContents()) {
      if (!content.isPinned() && content.isCloseable() && content.getUserData(PUB_TOOL_WINDOW_CONTENT_INFO_KEY) != null) {
        contentManager.removeContent(content, false);
      }
    }
  }
}
