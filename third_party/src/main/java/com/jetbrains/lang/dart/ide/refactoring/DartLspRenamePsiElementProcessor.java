/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.ide.refactoring;

import com.intellij.platform.lsp.util.Lsp4jUtilKt;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.lsp.DartLspService;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.progress.ProgressManager;
import consulo.component.ProcessCanceledException;
import consulo.document.Document;
import consulo.document.FileDocumentManager;
import consulo.language.editor.WriteCommandAction;
import consulo.language.editor.refactoring.rename.RenamePsiElementProcessor;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFileSystemItem;
import consulo.language.psi.PsiReference;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.content.ProjectFileIndex;
import consulo.project.Project;
import consulo.util.io.FileUtil;
import consulo.util.io.OSAgnosticPathUtil;
import consulo.util.io.URLUtil;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.eclipse.lsp4j.FileRename;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.SnippetTextEdit;
import org.eclipse.lsp4j.TextDocumentEdit;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.ResourceOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Intercepts renaming of Dart files and directories to send {@code workspace/willRenameFiles}
 * to the LSP server and apply the resulting workspace edit (e.g. updating imports)
 * before IntelliJ renames the file on disk.
 * <p>
 * Note: JetBrains' platform LSP ({@code intellij.platform.lsp}) does not currently support
 * LSP 3.16 {@code workspace.fileOperations} ({@code workspace/willRenameFiles}). This custom processor
 * may be removed or simplified if JetBrains implements built-in platform support
 * in the future (tracked by IJPL-249183 and IJPL-158718).
 */
@ExtensionImpl(id = "dartLspRename", order = "first")
public class DartLspRenamePsiElementProcessor extends RenamePsiElementProcessor {
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DartLspRenamePsiElementProcessor.class);

  public static @NotNull String getFileUri(@NotNull String filePath) {
    String escapedPath = URLUtil.encodePath(FileUtil.toSystemIndependentName(filePath));
    String url = VirtualFileManager.constructUrl(URLUtil.FILE_PROTOCOL, escapedPath);
    URI parsedUri = VirtualFileUtil.toUri(url);
    String uri = parsedUri != null ? parsedUri.toString() : url;
    String prefix = "file:///";
    if (uri.startsWith(prefix) && OSAgnosticPathUtil.startsWithWindowsDrive(uri.substring(prefix.length()))) {
      return prefix + Character.toUpperCase(uri.charAt(prefix.length())) + uri.substring(prefix.length() + 1);
    }
    return uri;
  }

  public static void applyWorkspaceEdit(@NotNull Project project, @NotNull WorkspaceEdit workspaceEdit) {
    List<Either<TextDocumentEdit, ResourceOperation>> documentChanges = workspaceEdit.getDocumentChanges();
    if (documentChanges == null) return;
    if (documentChanges.isEmpty()) return;
    logger.info("applyWorkspaceEdit: " + documentChanges.size() + " document changes to update");

    List<Pair<Document, List<Either<TextEdit, SnippetTextEdit>>>> editsToApply = new ArrayList<>();
    for (Either<TextDocumentEdit, ResourceOperation> either : documentChanges) {
      if (!either.isLeft()) continue;
      TextDocumentEdit textDocEdit = either.getLeft();
      String uri = textDocEdit.getTextDocument().getUri();
      List<Either<TextEdit, SnippetTextEdit>> textEdits = textDocEdit.getEdits();
      VirtualFile virtualFile = findVirtualFileByUri(uri);
      logger.debug("applyWorkspaceEdit for uri: " + uri + " -> resolved VirtualFile: " + virtualFile + " with " + textEdits.size() + " edits");
      if (virtualFile != null) {
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        if (document != null) {
          editsToApply.add(Pair.create(document, textEdits));
        }
        else {
          logger.warn("Could not obtain Document for " + virtualFile);
        }
      }
      else {
        logger.warn("Could not find VirtualFile for URI: " + uri);
      }
    }

    if (editsToApply.isEmpty()) return;

    WriteCommandAction.runWriteCommandAction(project, "Update Dart Imports", null, () -> {
      for (Pair<Document, List<Either<TextEdit, SnippetTextEdit>>> pair : editsToApply) {
        Document document = pair.getFirst();
        // Consulo: lsp4j 1.0 has Either<TextEdit, SnippetTextEdit> edits; applySimpleTextEdits applies the TextEdit side
        // like the upstream (patched) applyTextEdits does.
        Lsp4jUtilKt.applySimpleTextEdits(document, pair.getSecond());
        FileDocumentManager.getInstance().saveDocument(document);
      }
    });
  }

  public static <T> @Nullable T awaitFutureCheckingCanceled(@NotNull CompletableFuture<T> future)
    throws TimeoutException, InterruptedException, ExecutionException {
    return awaitFutureCheckingCanceled(future, 10);
  }

  public static <T> @Nullable T awaitFutureCheckingCanceled(@NotNull CompletableFuture<T> future, long timeoutSeconds)
    throws TimeoutException, InterruptedException, ExecutionException {
    long timeoutMillis = timeoutSeconds * 1000;
    long startTime = System.currentTimeMillis();
    while (true) {
      ProgressManager.checkCanceled();
      if (future.isDone()) {
        return future.get();
      }
      if (System.currentTimeMillis() > startTime + timeoutMillis) {
        throw new TimeoutException("Timed out waiting for LSP response after " + timeoutSeconds + "s");
      }
      try {
        return future.get(50, TimeUnit.MILLISECONDS);
      }
      catch (TimeoutException ignored) {
        // Continue checking canceled
      }
    }
  }

  private static @Nullable VirtualFile findVirtualFileByUri(@NotNull String uri) {
    VirtualFileManager vfm = VirtualFileManager.getInstance();
    VirtualFile file = vfm.findFileByUrl(uri);
    if (file != null) return file;
    file = vfm.refreshAndFindFileByUrl(uri);
    if (file != null) return file;
    try {
      URI parsed = new URI(uri);
      String path = parsed.getPath();
      if (path == null) {
        path = uri.startsWith("file://") ? uri.substring("file://".length()) : uri;
      }
      file = vfm.findFileByUrl(VirtualFileManager.constructUrl("file", path));
      return file != null ? file : VirtualFileUtil.findFileByIoFile(new File(path), true);
    }
    catch (Exception e) {
      return null;
    }
  }

  @Override
  public boolean canProcessElement(@NotNull PsiElement element) {
    Project project = element.getProject();
    if (!DartConfigurable.isExperimentalLspFeaturesEnabled(project)) {
      return false;
    }
    if (DartSdk.getDartSdk(project) == null) {
      return false;
    }

    ProjectFileIndex fileIndex = ProjectFileIndex.getInstance(project);
    if (element instanceof DartFile dartFile) {
      VirtualFile vFile = dartFile.getVirtualFile();
      if (vFile == null) return false;
      return fileIndex.isInContent(vFile);
    }
    else if (element instanceof PsiDirectory directory) {
      VirtualFile vFile = directory.getVirtualFile();
      Module module = fileIndex.getModuleForFile(vFile);
      if (module == null) return false;
      return DartSdkLibUtil.isDartSdkEnabled(module);
    }
    else {
      return false;
    }
  }

  // Consulo: upstream overrides findReferences(element, searchScope, searchInCommentsAndStrings), which Consulo's
  // RenamePsiElementProcessor does not have; RenameUtil calls findReferences(element, searchInCommentsAndStrings).
  @Override
  public @NotNull Collection<PsiReference> findReferences(@NotNull PsiElement element, boolean searchInCommentsAndStrings) {
    // Return empty collection so IntelliJ's legacy PSI string slicer does not duplicate edits
    return Collections.emptyList();
  }

  @Override
  public @NotNull Collection<PsiReference> findReferences(@NotNull PsiElement element) {
    return Collections.emptyList();
  }

  @Override
  public void prepareRenaming(@NotNull PsiElement element, @NotNull String newName, @NotNull Map<PsiElement, String> allRenames) {
    if (!(element instanceof PsiFileSystemItem psiFileSystemItem)) return;
    VirtualFile virtualFile = psiFileSystemItem.getVirtualFile();
    if (virtualFile == null) return;
    Project project = element.getProject();

    DartAnalysisServerService.getInstance(project).updateFilesContent();

    String oldUri = getFileUri(virtualFile.getPath());
    VirtualFile parent = virtualFile.getParent();
    if (parent == null) return;
    String parentPath = parent.getPath();
    String newPath = parentPath + "/" + newName;
    String newUri = getFileUri(newPath);
    logger.info("prepareRenaming via LSP: oldUri=" + oldUri + ", newUri=" + newUri);

    RenameFilesParams params = new RenameFilesParams(List.of(new FileRename(oldUri, newUri)));

    try {
      CompletableFuture<WorkspaceEdit> workspaceEditFuture = DartLspService.willRenameFiles(project, params);
      WorkspaceEdit workspaceEdit = awaitFutureCheckingCanceled(workspaceEditFuture, 10);
      logger.debug("prepareRenaming willRenameFiles response: " + workspaceEdit);
      if (workspaceEdit != null) {
        applyWorkspaceEdit(project, workspaceEdit);
      }
    }
    catch (ProcessCanceledException e) {
      logger.info("willRenameFiles was canceled");
      throw e;
    }
    catch (TimeoutException e) {
      logger.warn("Timeout waiting for willRenameFiles from Dart Analysis Server", e);
    }
    catch (Exception e) {
      logger.error("Error executing willRenameFiles for Dart rename", e);
    }
  }
}
