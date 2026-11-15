// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.actions;

import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.language.editor.hint.HintManager;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.language.editor.CommonDataKeys;
import consulo.ui.ex.action.Presentation;
import consulo.application.ApplicationManager;
import consulo.ui.ModalityState;
import consulo.codeEditor.Editor;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import consulo.module.Module;
import consulo.language.util.ModuleUtilCore;
import consulo.application.dumb.DumbAware;
import consulo.project.Project;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.ProjectRootManager;

import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiFile;
import consulo.language.psi.search.FileTypeIndex;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.psi.scope.GlobalSearchScopesCore;
import consulo.util.collection.SmartList;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * An abstract {@link AnAction} for processing a single Dart file open in the editor,
 * or a group of selected Dart files.
 */
public abstract class AbstractDartFileProcessingAction extends AnAction implements DumbAware, AnActionWithSyncUpdate {


  @Override
  public void actionPerformed(final @NotNull AnActionEvent event) {
    final Project project = event.getData(Project.KEY);
    if (project == null) return;

    final DartSdk sdk = DartSdk.getDartSdk(project);
    if (sdk == null || !DartAnalysisServerService.isDartSdkVersionSufficient(sdk)) return;


    PsiDocumentManager.getInstance(project).commitAllDocuments();
    final Editor editor = event.getData(CommonDataKeys.EDITOR);

    if (editor != null) {
      final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
      if (psiFile == null || !isApplicableFile(project, psiFile.getVirtualFile())) return;
      runOverEditor(project, editor, psiFile);
    }
    else {
      final VirtualFile[] filesAndDirs = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
      if (filesAndDirs != null && DartAnalysisServerService.getInstance(project).serverReadyForRequest()) {
        final List<VirtualFile> files = getApplicableVirtualFiles(project, filesAndDirs);
        runOverFiles(project, files);
      }
    }
  }

  protected abstract @NotNull String getActionTextForEditor();

  protected abstract @NotNull String getActionTextForFiles();

  protected abstract void runOverEditor(final @NotNull Project project,
                                        final @NotNull Editor editor,
                                        final @NotNull PsiFile psiFile);

  protected abstract void runOverFiles(final @NotNull Project project, final @NotNull List<VirtualFile> dartFiles);

  @Override
  public void update(final @NotNull AnActionEvent event) {
    final Presentation presentation = event.getPresentation();
    final Project project = event.getData(Project.KEY);
    if (project == null) {
      presentation.setEnabledAndVisible(false);
      return;
    }

    final DartSdk sdk = DartSdk.getDartSdk(project);
    if (sdk == null || !DartAnalysisServerService.isDartSdkVersionSufficient(sdk)) {
      presentation.setEnabledAndVisible(false);
      return;
    }

    final Editor editor = event.getData(CommonDataKeys.EDITOR);
    if (editor != null) {
      final PsiFile psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.getDocument());
      // visible for any Dart file, but enabled for applicable only
      presentation.setVisible(psiFile != null && psiFile.getFileType() == DartFileType.INSTANCE);
      presentation.setEnabled(psiFile != null && isApplicableFile(project, psiFile.getVirtualFile()));
      presentation.setText(getActionTextForEditor());
      return;
    }

    final VirtualFile[] filesAndDirs = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY);
    if (filesAndDirs == null) {
      presentation.setEnabledAndVisible(false);
      return;
    }

    presentation.setEnabledAndVisible(mayHaveApplicableDartFiles(project, filesAndDirs));
    presentation.setText(getActionTextForFiles());
  }

  public static void showHintLater(final @NotNull Editor editor, final @NotNull String text, final boolean error) {
    ApplicationManager.getApplication().invokeLater(() -> {
      // Consulo: no public HintManager.showEditorHint(LightweightHint, ...)
      if (error) {
        final int offset = editor.getCaretModel().getOffset();
        HintManager.getInstance().showErrorHint(editor, text, offset, offset, HintManager.UNDER, HintManager.HIDE_BY_ANY_KEY |
                                                                                                 HintManager.HIDE_BY_TEXT_CHANGE |
                                                                                                 HintManager.HIDE_BY_SCROLLING, 0);
      }
      else {
        HintManager.getInstance().showInformationHint(editor, text, HintManager.UNDER);
      }
    }, ModalityState.nonModal(), () -> editor.isDisposed() || !editor.getComponent().isShowing());
  }

  private static @NotNull List<VirtualFile> getApplicableVirtualFiles(final @NotNull Project project,
                                                                      final VirtualFile @NotNull [] filesAndDirs) {
    final List<VirtualFile> result = new SmartList<>();

    GlobalSearchScope dirScope = null;

    for (VirtualFile fileOrDir : filesAndDirs) {
      if (fileOrDir.isDirectory()) {
        if (dirScope == null) {
          dirScope = GlobalSearchScopesCore.directoryScope(project, fileOrDir, true);
        }
        else {
          dirScope = dirScope.union(GlobalSearchScopesCore.directoryScope(project, fileOrDir, true));
        }
      }
      else if (isApplicableFile(project, fileOrDir)) {
        result.add(fileOrDir);
      }
    }

    if (dirScope != null) {
      for (VirtualFile file : FileTypeIndex
        .getFiles(DartFileType.INSTANCE, GlobalSearchScope.projectScope(project).intersectWith(dirScope))) {
        if (isApplicableFile(project, file)) {
          result.add(file);
        }
      }
    }

    return result;
  }

  private static boolean isApplicableFile(final @NotNull Project project, final @Nullable VirtualFile file) {
    if (file == null || !FileTypeRegistry.getInstance().isFileOfType(file, DartFileType.INSTANCE)) return false;
    if (!ProjectFileIndex.getInstance(project).isInContent(file)) return false;

    final Module module = ModuleUtilCore.findModuleForFile(file, project);
    if (module == null) return false;
      return DartSdkLibUtil.isDartSdkEnabled(module);
  }

  private static boolean mayHaveApplicableDartFiles(final @NotNull Project project,
                                                    final VirtualFile @NotNull [] files) {
    for (VirtualFile fileOrDir : files) {
      if (!fileOrDir.isDirectory() && isApplicableFile(project, fileOrDir)) {
        return true;
      }

      if (fileOrDir.isDirectory() &&
          ProjectRootManager.getInstance(project).getFileIndex().isInContent(fileOrDir) &&
          FileTypeIndex.containsFileOfType(DartFileType.INSTANCE, GlobalSearchScopesCore.directoryScope(project, fileOrDir, true))) {
        return true;
      }
    }

    return false;
  }
}
