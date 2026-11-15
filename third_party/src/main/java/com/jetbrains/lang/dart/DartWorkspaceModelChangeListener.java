package com.jetbrains.lang.dart;

import jakarta.inject.Inject;
import consulo.module.content.layer.event.ModuleRootListener;
import consulo.module.content.layer.event.ModuleRootEvent;
import consulo.annotation.component.TopicImpl;
import consulo.annotation.component.ComponentScope;
import consulo.project.Project;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonService;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Consulo: there is no workspace model, Dart roots are updated on {@link ModuleRootListener#rootsChanged}
 * (content roots and the Dart module extension are module root changes) when Dart is enabled in any module.
 */
@TopicImpl(ComponentScope.PROJECT)
public class DartWorkspaceModelChangeListener implements ModuleRootListener {

  private final @NotNull Project myProject;

  @Inject
  public DartWorkspaceModelChangeListener(@NotNull Project project) {
    myProject = project;
  }

  @Override
  public void rootsChanged(@NotNull ModuleRootEvent event) {
    if (DartSdkLibUtil.getModulesWithDartSdkEnabled(myProject).isEmpty()) return;
    onDartRootsChanged(myProject);
  }

  private static void onDartRootsChanged(@NotNull Project project) {
    if (!project.isInitialized() || DartSdk.getDartSdk(project) == null) return;

    DartFileListener.scheduleDartPackageRootsUpdate(project);
    DartAnalysisServerService.getInstance(project).ensureAnalysisRootsUpToDate();
    DartToolingDaemonService.getInstance(project).ensureRootsUpToDate();
  }
}
