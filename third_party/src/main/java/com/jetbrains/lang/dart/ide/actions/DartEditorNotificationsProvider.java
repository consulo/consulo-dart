// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.actions;

import consulo.dart.notification.DartNotificationGroups;
import consulo.annotation.component.ExtensionImpl;
import consulo.webBrowser.BrowserUtil;
import consulo.application.ApplicationManager;
import consulo.fileEditor.FileEditor;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import consulo.module.Module;
import consulo.language.util.ModuleUtilCore;
import consulo.project.Project;

import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.annotation.access.RequiredReadAction;
import consulo.fileEditor.EditorNotificationBuilder;
import consulo.fileEditor.EditorNotificationProvider;
import consulo.localize.LocalizeValue;
import consulo.project.ui.notification.NotificationService;
import jakarta.inject.Inject;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.flutter.FlutterUtil;
import com.jetbrains.lang.dart.sdk.*;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;


@ExtensionImpl
public final class DartEditorNotificationsProvider implements EditorNotificationProvider {
  private final @NotNull Project project;

  @Inject
  public DartEditorNotificationsProvider(@NotNull Project project) {
    this.project = project;
  }

  @Override
  public @NotNull String getId() {
    return "dart.editor.notifications";
  }

  // Consulo: EditorNotificationProvider fills an EditorNotificationBuilder instead of returning a panel factory
  @RequiredReadAction
  @Override
  public @Nullable EditorNotificationBuilder buildNotification(@NotNull VirtualFile file,
                                                               @NotNull FileEditor fileEditor,
                                                               @NotNull Supplier<EditorNotificationBuilder> builderFactory) {
    if (!file.isInLocalFileSystem()) {
      return null;
    }

    boolean isPubspecFile = PubspecYamlUtil.isPubspecFile(file);

    if (isPubspecFile) {
      final Module module = ModuleUtilCore.findModuleForFile(file, project);
      if (module == null) return null;

      // Defer to the Flutter plugin for package management and SDK configuration if appropriate.
      if (FlutterUtil.isFlutterPluginInstalled() && FlutterUtil.isPubspecDeclaringFlutter(file)) return null;

      final DartSdk sdk = DartSdk.getDartSdk(project);
      if (sdk != null && DartSdkLibUtil.isDartSdkEnabled(module)) {
        return createPubActionsPanel(builderFactory.get());
      }
    }

    if (isPubspecFile || FileTypeRegistry.getInstance().isFileOfType(file, DartFileType.INSTANCE)) {
      final DartSdk sdk = DartSdk.getDartSdk(project);

      final PsiFile psiFile = PsiManager.getInstance(project).findFile(file);
      if (psiFile == null) return null;

      final Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
      if (module == null) return null;

      // no SDK
      if (sdk == null) {
        final String sdkPath = DartSdkUtil.getFirstKnownDartSdkPath();
        if (DartSdkUtil.isDartSdkHome(sdkPath)) {
          return createNotificationToEnableDartSupport(builderFactory.get(), module);
        }
        else {
          return createNoDartSdkPanel(builderFactory.get(), project, DartBundle.message("dart.sdk.is.not.configured"));
        }
      }

      // SDK not enabled for this module
      if (!DartSdkLibUtil.isDartSdkEnabled(module)) {
        return createNotificationToEnableDartSupport(builderFactory.get(), module);
      }

      if (!DartAnalysisServerService.isDartSdkVersionSufficient(sdk)) {
        String message = DartBundle.message("old.dart.sdk.configured", DartAnalysisServerService.MIN_SDK_VERSION, sdk.getVersion());
        return createNoDartSdkPanel(builderFactory.get(), project, message);
      }
    }

    return null;
  }

  public @NotNull EditorNotificationBuilder createNoDartSdkPanel(@NotNull EditorNotificationBuilder panel,
                                                                 @NotNull Project project,
                                                                 String message) {
    final String downloadUrl = DartSdkUpdateChecker.SDK_STABLE_DOWNLOAD_URL;

    panel.asWarning().withIcon(DartIcons.Dart_16).withText(LocalizeValue.of(message));
    panel.withAction(DartBundle.messageValue("download.dart.sdk"), e -> new OpenWebPageRunnable(downloadUrl).run());
    panel.withAction(DartBundle.messageValue("open.dart.settings"), e -> new OpenDartSettingsRunnable(project).run());
    return panel;
  }

  private static @NotNull EditorNotificationBuilder createNotificationToEnableDartSupport(@NotNull EditorNotificationBuilder panel,
                                                                                          @NotNull Module module) {
    final String message = DartSdkLibUtil.isIdeWithMultipleModuleSupport()
                           ? DartBundle.message("dart.support.is.not.enabled.for.module.0", module.getName())
                           : DartBundle.message("dart.support.is.not.enabled.for.project");
    panel.asWarning().withIcon(DartIcons.Dart_16).withText(LocalizeValue.of(message));
    panel.withAction(DartBundle.messageValue("enable.dart.support"), e -> new EnableDartSupportForModule(module).run());
    panel.withAction(DartBundle.messageValue("open.dart.settings"), e -> new OpenDartSettingsRunnable(module.getProject()).run());
    return panel;
  }

  // Consulo: no separator between the pub and webdev actions
  private static @NotNull EditorNotificationBuilder createPubActionsPanel(@NotNull EditorNotificationBuilder panel) {
    panel.asInfo();
    panel.withAction(DartBundle.messageValue("pub.get"), "Dart.pub.get");
    panel.withAction(DartBundle.messageValue("pub.upgrade"), "Dart.pub.upgrade");
    panel.withAction(DartBundle.messageValue("pub.outdated"), "Dart.pub.outdated");
    panel.withAction(DartBundle.messageValue("webdev.build"), "Dart.build");
    return panel;
  }

  private static class EnableDartSupportForModule implements Runnable {
    private final Module myModule;

    EnableDartSupportForModule(@NotNull Module module) {
      this.myModule = module;
    }

    @Override
    public void run() {
      final Project project = myModule.getProject();

      ApplicationManager.getApplication().runWriteAction(() -> {
        if (DartSdk.getDartSdk(project) == null) {
          final String sdkPath = DartSdkUtil.getFirstKnownDartSdkPath();
          if (DartSdkUtil.isDartSdkHome(sdkPath)) {
            DartSdkLibUtil.ensureDartSdkConfigured(project, sdkPath);
          }
          else {
            return; // shouldn't happen, sdk path is already checked
          }
        }

        DartSdkLibUtil.enableDartSdk(myModule);
      });

      DartAnalysisServerService.getInstance(project).serverReadyForRequest();

      final DartSdk sdk = DartSdk.getDartSdk(project);
      if (sdk != null && DartSdkLibUtil.isDartSdkEnabled(myModule)) {
        final String title = DartSdkLibUtil.isIdeWithMultipleModuleSupport()
                             ? DartBundle.message("dart.support.enabled.for.module.0", myModule.getName())
                             : DartBundle.message("dart.support.enabled");
        final String message = DartBundle.message("dart.sdk.0.open.dart.settings", sdk.getVersion());

        NotificationService.getInstance()
          .newInfo(DartNotificationGroups.DART_SUPPORT)
          .title(LocalizeValue.of(title))
          .content(LocalizeValue.of(message))
          .addAction(DartBundle.messageValue("open.dart.settings"), () -> {
            DartConfigurable.openDartSettings(project);
          })
          .notify(project);
      }
    }
  }

  private static final class OpenWebPageRunnable implements Runnable {
    private final @NotNull String myUrl;

    private OpenWebPageRunnable(@NotNull String url) {
      myUrl = url;
    }

    @Override
    public void run() {
      BrowserUtil.browse(myUrl);
    }
  }

  private static final class OpenDartSettingsRunnable implements Runnable {
    private final @NotNull Project myProject;

    private OpenDartSettingsRunnable(@NotNull Project project) {
      myProject = project;
    }

    @Override
    public void run() {
      DartConfigurable.openDartSettings(myProject);
    }
  }
}
