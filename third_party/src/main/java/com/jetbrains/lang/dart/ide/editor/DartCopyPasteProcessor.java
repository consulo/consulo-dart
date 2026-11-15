// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.editor;

import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.CodeInsightSettings;
import consulo.ide.impl.idea.codeInsight.editorActions.CopyPastePostProcessor;
import consulo.ide.impl.idea.codeInsight.editorActions.TextBlockTransferableData;
import consulo.application.ApplicationManager;
import consulo.application.WriteAction;
import consulo.codeEditor.Editor;
import consulo.document.RangeMarker;
import consulo.document.FileDocumentManager;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import consulo.module.Module;
import consulo.language.util.ModuleUtilCore;
import consulo.project.Project;
import consulo.module.content.ProjectFileIndex;
import consulo.ui.ex.awt.DialogWrapper;
import com.intellij.openapi.ui.DoNotAskOption;
import consulo.ui.ex.awt.MessageDialogBuilder;
import consulo.util.lang.ref.Ref;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiFile;
import consulo.util.collection.SmartList;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.assists.AssistUtils;
import com.jetbrains.lang.dart.ide.codeInsight.DartCodeInsightSettings;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.dartlang.analysis.server.protocol.ImportedElements;
import org.dartlang.analysis.server.protocol.SourceFileEdit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@ExtensionImpl
public final class DartCopyPasteProcessor extends CopyPastePostProcessor<DartCopyPasteProcessor.DartImportsTransferableData> {
  private static final DataFlavor FLAVOR = new DataFlavor(DartImportsTransferableData.class, "Dart imports");

  @Override
  public @NotNull List<DartImportsTransferableData> collectTransferableData(final @NotNull PsiFile psiFile,
                                                                            final @NotNull Editor editor,
                                                                            final int @NotNull [] startOffsets,
                                                                            final int @NotNull [] endOffsets) {
    if (!(psiFile instanceof DartFile)) return Collections.emptyList();
    final Project project = psiFile.getProject();
    final VirtualFile vFile = DartResolveUtil.getRealVirtualFile(psiFile);
    if (!DartAnalysisServerService.isLocalAnalyzableFile(vFile)) return Collections.emptyList();
    if (DartCodeInsightSettings.getInstance().ADD_IMPORTS_ON_PASTE == CodeInsightSettings.NO) return Collections.emptyList();
    final Module module = ModuleUtilCore.findModuleForPsiElement(psiFile);
    if (module == null || !DartSdkLibUtil.isDartSdkEnabled(module)) return Collections.emptyList();
    if (!ProjectFileIndex.getInstance(project).isInContent(vFile)) return Collections.emptyList();
    final DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
    if (!das.serverReadyForRequest()) return Collections.emptyList();

    das.updateFilesContent();

    final List<ImportedElements> importedElements = new SmartList<>();
    for (int i = 0; i < startOffsets.length; i++) {
      final int offset = startOffsets[i];
      final int length = endOffsets[i] - startOffsets[i];
      final List<ImportedElements> elements = das.analysis_getImportedElements(vFile, offset, length);
      if (elements != null) {
        importedElements.addAll(elements);
      }
    }

    return !importedElements.isEmpty() ? Collections.singletonList(new DartImportsTransferableData(importedElements))
                                       : Collections.emptyList();
  }

  @Override
  public @NotNull List<DartImportsTransferableData> extractTransferableData(final @NotNull Transferable content) {
    try {
      final Object data = content.getTransferData(FLAVOR);
      if (data instanceof DartImportsTransferableData) {
        return Collections.singletonList((DartImportsTransferableData)data);
      }
    }
    catch (UnsupportedFlavorException | IOException ignored) {/**/}
    return Collections.emptyList();
  }

  @Override
  public void processTransferableData(final @NotNull Project project,
                                      final @NotNull Editor editor,
                                      final @NotNull RangeMarker bounds,
                                      final int caretOffset,
                                      final @NotNull Ref<? super Boolean> indented,
                                      final @NotNull List<? extends DartImportsTransferableData> values) {
    final DartImportsTransferableData data = ContainerUtil.getFirstItem(values);
    if (data == null) return;

    final VirtualFile file = FileDocumentManager.getInstance().getFile(editor.getDocument());
    if (file == null || !FileTypeRegistry.getInstance().isFileOfType(file, DartFileType.INSTANCE) || !DartAnalysisServerService.isLocalAnalyzableFile(file)) return;

    if (DartCodeInsightSettings.getInstance().ADD_IMPORTS_ON_PASTE == CodeInsightSettings.NO) return;

    final SourceFileEdit edit =
      DartAnalysisServerService.getInstance(project).edit_importElements(file, data.getImportedElements(), caretOffset);

    if (edit != null && edit.getEdits() != null && !edit.getEdits().isEmpty()) {
      if (DartCodeInsightSettings.getInstance().ADD_IMPORTS_ON_PASTE == CodeInsightSettings.ASK &&
          !ApplicationManager.getApplication().isUnitTestMode()) {
        final String message = DartBundle.message("dialog.paste.on.import.text");
        final String title = DartBundle.message("dialog.paste.on.import.title");

        final DoNotAskOption doNotAskOption = new DoNotAskOption.Adapter() {
          @Override
          public boolean shouldSaveOptionsOnCancel() {
            return true;
          }

          @Override
          public void rememberChoice(boolean isSelected, int exitCode) {
            if (!isSelected) return;

            if (exitCode == DialogWrapper.OK_EXIT_CODE) {
              DartCodeInsightSettings.getInstance().ADD_IMPORTS_ON_PASTE = CodeInsightSettings.YES;
            }
            else if (exitCode == DialogWrapper.CANCEL_EXIT_CODE) {
              DartCodeInsightSettings.getInstance().ADD_IMPORTS_ON_PASTE = CodeInsightSettings.NO;
            }
          }
        };

        if (!MessageDialogBuilder.yesNo(title, message).doNotAsk(doNotAskOption).ask(project)) {
          return;
        }
      }

      WriteAction.run(() -> AssistUtils.applyFileEdit(project, edit));
    }
  }

  public static class DartImportsTransferableData implements TextBlockTransferableData {
    private final @NotNull List<ImportedElements> myImportedElements;

    public DartImportsTransferableData(final @NotNull List<ImportedElements> importedElements) {
      myImportedElements = importedElements;
    }

    public @NotNull List<ImportedElements> getImportedElements() {
      return myImportedElements;
    }

    @Override
    public @Nullable DataFlavor getFlavor() {
      return FLAVOR;
    }
  }
}
