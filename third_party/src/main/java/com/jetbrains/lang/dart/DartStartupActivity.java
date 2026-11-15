// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart;

import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonService;
import com.jetbrains.lang.dart.projectWizard.DartModuleBuilder;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.application.ReadAction;
import consulo.application.util.registry.Registry;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.project.Project;
import consulo.project.startup.PostStartupActivity;
import consulo.ui.UIAccess;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

/**
 * {@link DartStartupActivity} configures "Dart Packages" library (based on Dart-specific pubspec.yaml and .packages files) on a project open.
 * Afterward the "Dart Packages" library is kept up-to-dated thanks to {@link DartFileListener} and {@link DartWorkspaceModelChangeListener}.
 *
 * @see DartFileListener
 * @see DartWorkspaceModelChangeListener
 */
@ExtensionImpl
public final class DartStartupActivity implements PostStartupActivity {
  @Override
  public void runActivity(@NotNull Project project, @NotNull UIAccess uiAccess) {
    // Consulo: no coroutine service scope, background work goes to the application pool
    Application.get().executeOnPooledThread(() -> {
      // Group all required exclusions by module and content root in the background to avoid EDT lockup
      // See: https://github.com/flutter/dart-intellij-third-party/issues/149
      final Map<Module, Map<String, Set<String>>> exclusionsByModule =
        ReadAction.compute(() -> DartStartupActivityKt.collectExclusionsByModule(project));

      // Apply all exclusions in a single update
      // See: https://github.com/flutter/dart-intellij-third-party/issues/728
      if (!exclusionsByModule.isEmpty()) {
        uiAccess.giveAsync(() -> Application.get().runWriteAction(() -> DartStartupActivityKt.applyExclusions(exclusionsByModule)))
          .whenComplete((r, e) -> DartFileListener.scheduleDartPackageRootsUpdate(project));
      }
    });

    Application.get().executeOnPooledThread(() -> startAnalysisServerIfNeeded(project));
  }

  private static void startAnalysisServerIfNeeded(@NotNull Project project) {
    if (DartModuleBuilder.isPubGetScheduledForNewlyCreatedProject(project)) {
      // We want to start Analysis Server after the initial 'pub get' is finished, this will be done in DartPubActionBase
      return;
    }

    if (DartSdk.getDartSdk(project) == null) return;
    boolean hasDartModule = false;
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      if (DartSdkLibUtil.isDartSdkEnabled(module)) {
        hasDartModule = true;
        break;
      }
    }
    if (!hasDartModule) return;

    ReadAction.run(() -> DartAnalysisServerService.getInstance(project).serverReadyForRequest());

    if (Registry.is("dart.launch.dtd.and.devtools", false)) {
      DartToolingDaemonService.getInstance(project).startService();
    }
  }
}
