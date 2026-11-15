// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.lang.dart.projectWizard;

import com.jetbrains.lang.dart.ide.actions.DartPubGetAction;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.sdk.DartSdkUtil;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import consulo.application.ApplicationManager;
import consulo.application.WriteAction;
import consulo.content.bundle.Sdk;
import consulo.dart.module.extension.DartMutableModuleExtension;
import consulo.fileEditor.FileEditorManager;
import consulo.module.Module;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.project.DumbService;
import consulo.project.Project;
import consulo.ui.ModalityState;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.AnAction;
import consulo.util.dataholder.Key;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Collection;

/**
 * Consulo: the wizard UI is {@link consulo.dart.newProjectOrModule.DartNewModuleBuilder}, upstream module builder logic is kept here.
 */
public final class DartModuleBuilder {
  private static final Key<Boolean> PUB_GET_SCHEDULED_KEY = Key.create("PUB_GET_SCHEDULED_KEY");

  private DartModuleBuilder() {
  }

  public static void setupProject(final @NotNull ModifiableRootModel modifiableRootModel,
                           final @NotNull VirtualFile baseDir,
                           final @NotNull DartProjectWizardData wizardData) {
    setupSdk(modifiableRootModel, wizardData);

    if (wizardData.myTemplate != null) {
      ApplicationManager.getApplication().executeOnPooledThread(() -> {
        try {
          final Module module = modifiableRootModel.getModule();
          final String dartSdkPath = wizardData.dartSdkPath;
          final Collection<VirtualFile> generatedFiles = wizardData.myTemplate.generateProject(
                  dartSdkPath,
                  module,
                  baseDir
          );

          WriteAction.runAndWait(() -> {
            for (VirtualFile file : generatedFiles) {
              file.refresh(false, true);
            }
          });

          if (!generatedFiles.isEmpty()) {
            scheduleFilesOpeningAndPubGet(modifiableRootModel.getModule(), generatedFiles);
          }
        }
        catch (IOException ignore) {/*unlucky*/}
      });
    }
  }

  private static void setupSdk(final @NotNull ModifiableRootModel modifiableRootModel,
                               final @NotNull DartProjectWizardData wizardData) {
    // similar to DartConfigurable.apply()
    if (DartSdkUtil.isDartSdkHome(wizardData.dartSdkPath)) {
      final Project project = modifiableRootModel.getProject();
      DartSdkUtil.updateKnownSdkPaths(project, wizardData.dartSdkPath);

      // Consulo: Dart SDK is a Consulo SDK selected in the Dart module extension
      ApplicationManager.getApplication().runWriteAction(() -> DartSdkLibUtil.ensureDartSdkConfigured(project, wizardData.dartSdkPath));
      enableDart(modifiableRootModel, DartSdkLibUtil.findDartSdkByHomePath(wizardData.dartSdkPath));
    }
  }

  public static void enableDart(final @NotNull ModifiableRootModel modifiableRootModel, final @Nullable Sdk sdk) {
    final DartMutableModuleExtension extension = modifiableRootModel.getExtensionWithoutCheck(DartMutableModuleExtension.class);
    if (extension == null) return;

    extension.setEnabled(true);
    if (sdk != null) {
      extension.getInheritableSdk().set(null, sdk);
      modifiableRootModel.addModuleExtensionSdkEntry(extension);
    }
  }

  public static boolean isPubGetScheduledForNewlyCreatedProject(final @NotNull Project project) {
    return project.getUserData(PUB_GET_SCHEDULED_KEY) == Boolean.TRUE;
  }

  private static void scheduleFilesOpeningAndPubGet(final @NotNull Module module, final @NotNull Collection<VirtualFile> files) {
    module.getProject().putUserData(PUB_GET_SCHEDULED_KEY, Boolean.TRUE);
    runWhenNonModalIfModuleNotDisposed(() -> {
      module.getProject().putUserData(PUB_GET_SCHEDULED_KEY, null);

      final FileEditorManager manager = FileEditorManager.getInstance(module.getProject());
      for (VirtualFile file : files) {
        manager.openFile(file, true);

        if (PubspecYamlUtil.PUBSPEC_YAML.equals(file.getName())) {
          final AnAction pubGetAction = ActionManager.getInstance().getAction("Dart.pub.get");
          if (pubGetAction instanceof DartPubGetAction) {
            ((DartPubGetAction)pubGetAction).performPubAction(module, file, false);
          }
        }
      }
    }, module);
  }

  static void runWhenNonModalIfModuleNotDisposed(final @NotNull Runnable runnable, final @NotNull Module module) {
    // runnable must not be executed immediately because the new project model might be not yet committed, so Dart SDK won't be found.
    // DumbService.runWhenSmart waits for the project to be fully initialized, loaded, and indexed.
    // This perfectly handles both WebStorm (already initialized) and IntelliJ IDEA (needs to wait).

    DumbService.getInstance(module.getProject()).runWhenSmart(() -> {
      if (module.isDisposed()) return;
      if (ApplicationManager.getApplication().getCurrentModalityState() == ModalityState.nonModal()) {
        runnable.run();
      }
      else {
        ApplicationManager.getApplication().invokeLater(
                runnable,
                ModalityState.nonModal(),
                module::isDisposed
        );
      }
    });
  }
}