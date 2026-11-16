/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.google.gson.JsonObject;
import com.intellij.platform.lsp.api.Lsp4jClient;
import com.intellij.platform.lsp.api.Lsp4jServer;
import com.intellij.platform.lsp.api.LspCommunicationChannel;
import com.intellij.platform.lsp.api.LspServerListener;
import com.intellij.platform.lsp.api.LspServerNotificationsHandler;
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor;
import com.intellij.platform.lsp.api.customization.*;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.ide.toolingDaemon.DartToolingDaemonService;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import consulo.language.psi.PsiFile;
import consulo.project.Project;
import consulo.util.io.OSAgnosticPathUtil;
import consulo.util.io.URLUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import org.eclipse.lsp4j.ClientCapabilities;
import org.eclipse.lsp4j.ConfigurationItem;
import org.eclipse.lsp4j.FileOperationsWorkspaceCapabilities;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.PublishDiagnosticsCapabilities;
import org.eclipse.lsp4j.TextDocumentClientCapabilities;
import org.eclipse.lsp4j.WorkspaceClientCapabilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

/**
 * Configuration descriptor that defines how the JetBrains LSP client communicates with the Dart Bridge server.
 * <p>
 * This descriptor specifies:
 * <ol>
 * <li>Which files are supported (only {@code .dart} files).</li>
 * <li>The communication channel to use (a TCP Socket channel using the dynamically allocated port of {@link DartBridgeLspServerManager}).
 *    {@code startProcess = false} tells the platform that the server is already running internally, so it only needs to connect.</li>
 * <li>The LSP feature customizations (e.g. enabling/disabling hover support dynamically based on settings).</li>
 * </ol>
 */
public class DartLspServerDescriptor extends ProjectWideLspServerDescriptor {
  private static final String DART_CONFIGURATION_SECTION = "dart";

  /**
   * Consulo: the last {@link DartLsp4jClient} created by {@link #createLsp4jClient}, so that other code can call
   * the public {@link Lsp4jClient#refreshInlayHints()} on it instead of using an implementation-only API.
   */
  private volatile @Nullable DartLsp4jClient lsp4jClient;

  public DartLspServerDescriptor(@NotNull Project project) {
    super(project, "Dart (Bridge)");
  }

  /**
   * Consulo: returns the last created {@link DartLsp4jClient}, or {@code null} if no client has been created yet.
   */
  public @Nullable DartLsp4jClient getLsp4jClient() {
    return lsp4jClient;
  }

  @Override
  public @NotNull Class<? extends Lsp4jServer> getLsp4jServerClass() {
    return DartLanguageServer.class;
  }

  @Override
  public @NotNull Lsp4jClient createLsp4jClient(@NotNull LspServerNotificationsHandler handler) {
    DartLsp4jClient client = new DartLsp4jClient(getProject(), handler);
    lsp4jClient = client;
    return client;
  }

  private final @NotNull LspServerListener lspServerListener = new LspServerListener() {
    @Override
    public void serverInitialized(@NotNull InitializeResult params) {
      // If the Dart Tooling Daemon (DTD) started before the LSP server (or if the LSP server was
      // restarted), connect the newly initialized LSP server to the running DTD instance.
      DartToolingDaemonService dtdService = getProject().getInstanceIfCreated(DartToolingDaemonService.class);
      String dtdUri = dtdService != null ? dtdService.getUri() : null;
      if (dtdUri != null) {
        DartLspService.connectToDtd(getProject(), dtdUri);
      }
    }
  };

  @Override
  public @NotNull LspServerListener getLspServerListener() {
    return lspServerListener;
  }

  @Override
  public @NotNull ClientCapabilities getClientCapabilities() {
    ClientCapabilities capabilities = super.getClientCapabilities();
    WorkspaceClientCapabilities ws = capabilities.getWorkspace();
    if (ws == null) {
      ws = new WorkspaceClientCapabilities();
      capabilities.setWorkspace(ws);
    }
    FileOperationsWorkspaceCapabilities fileOperations = new FileOperationsWorkspaceCapabilities();
    fileOperations.setWillRename(true);
    ws.setFileOperations(fileOperations);
    TextDocumentClientCapabilities td = capabilities.getTextDocument();
    if (td == null) {
      td = new TextDocumentClientCapabilities();
      capabilities.setTextDocument(td);
    }
    PublishDiagnosticsCapabilities pd = td.getPublishDiagnostics();
    if (pd == null) {
      pd = new PublishDiagnosticsCapabilities();
      td.setPublishDiagnostics(pd);
    }
    pd.setRelatedInformation(true);
    pd.setCodeDescriptionSupport(true);
    capabilities.setExperimental(Map.of("closingLabels", Collections.<String, Object>emptyMap()));
    return capabilities;
  }

  @Override
  public @NotNull Object createInitializationOptions() {
    return Map.of("closingLabels", true);
  }

  @Override
  public @Nullable Object getWorkspaceConfiguration(@NotNull ConfigurationItem item) {
    if (DART_CONFIGURATION_SECTION.equals(item.getSection())) {
      JsonObject dartSection = DartLspInlayHintsConfiguration.INSTANCE.buildDartSection();
      DartLspConfigurationSync.getInstance(getProject()).configurationSentToServer(dartSection);
      return dartSection;
    }
    return super.getWorkspaceConfiguration(item);
  }

  @Override
  public boolean isSupportedFile(@NotNull VirtualFile file) {
    return DartAnalysisServerService.isFileNameRespectedByAnalysisServer(file.getName());
  }

  /**
   * Re-implements {@code LspServerDescriptor.getFileUri} to preserve uppercase Windows drive letters ({@code C:}).
   * <p>
   * By default, the underlying JetBrains {@code LspServerDescriptor.getFileUri} lowercases drive letters ({@code c%3A})
   * to match VS Code conventions. However, legacy server messages sent by the Dart plugin (such as file
   * synchronizations via {@code analysis.updateContent}) use uppercase drive letters derived from IntelliJ VFS paths.
   * Because the Analysis Server evaluates path strings case-sensitively, a casing discrepancy between legacy
   * and LSP-over-legacy requests causes the server to treat the same file as two distinct contexts, leading
   * to duplicate analysis errors and sticky markers (see dart-lang/sdk#63819).
   */
  @Override
  public @NotNull String getFileUri(@NotNull VirtualFile file) {
    String escapedPath = URLUtil.encodePath(getFilePath(file));
    String url = VirtualFileManager.constructUrl(URLUtil.FILE_PROTOCOL, escapedPath);
    URI javaUri = VirtualFileUtil.toUri(url);
    String uri = javaUri != null ? javaUri.toString() : url;
    String prefix = "file:///";
    if (uri.startsWith(prefix) && OSAgnosticPathUtil.startsWithWindowsDrive(uri.substring(prefix.length()))) {
      return prefix + Character.toUpperCase(uri.charAt(prefix.length())) + uri.substring(prefix.length() + 1);
    }
    return uri;
  }

  @Override
  public @NotNull LspCommunicationChannel getLspCommunicationChannel() {
    DartBridgeLspServerManager manager = getProject().getInstance(DartBridgeLspServerManager.class);
    int port = manager.getPort();
    // The JetBrains LSP framework calls this getter to determine how to connect to the server.
    // We return a Socket channel pointing to the random port allocated by our Bridge Manager.
    // 'startProcess = false' tells the platform that the server process is already running
    // (managed by DartBridgeLspServerManager) and it should only establish a socket connection.
    if (port == -1) {
      return new LspCommunicationChannel.Socket(0, false);
    }
    return new LspCommunicationChannel.Socket(port, false);
  }

  private final @NotNull LspCustomization lspCustomization = new LspCustomization() {
    private final LspHoverCustomizer hoverCustomizer = new LspHoverSupport();
    private final LspGoToTypeDefinitionCustomizer goToTypeDefinitionCustomizer = new LspGoToTypeDefinitionSupport();
    private final LspDocumentHighlightsCustomizer documentHighlightsCustomizer = new LspDocumentHighlightsSupport() {
      // The default implementation only serves plain-text/TextMate files.
      @Override
      public boolean shouldAskServerForDocumentHighlights(@NotNull PsiFile psiFile) {
        return true;
      }
    };
    private final LspTypeHierarchyCustomizer typeHierarchyCustomizer = new LspTypeHierarchySupport();

    @Override
    public LspHoverCustomizer getHoverCustomizer() {
      return hoverCustomizer;
    }

    @Override
    public LspGoToDefinitionCustomizer getGoToDefinitionCustomizer() {
      if (DartAnalysisServerService.isLspNavigationEnabled(getProject())) {
        return new LspGoToDefinitionSupport();
      }
      else {
        return LspGoToDefinitionDisabled.INSTANCE;
      }
    }

    @Override
    public LspGoToTypeDefinitionCustomizer getGoToTypeDefinitionCustomizer() {
      return goToTypeDefinitionCustomizer;
    }

    @Override
    public LspCompletionCustomizer getCompletionCustomizer() {
      if (DartAnalysisServerService.isLspCompletionEnabled(getProject())) {
        return DartLspCompletionSupport.INSTANCE;
      }
      else {
        return LspCompletionDisabled.INSTANCE;
      }
    }

    @Override
    public LspSemanticTokensCustomizer getSemanticTokensCustomizer() {
      if (DartAnalysisServerService.isLspHighlightingEnabled(getProject())) {
        return DartLspSemanticTokensSupport.INSTANCE;
      }
      else {
        return LspSemanticTokensDisabled.INSTANCE;
      }
    }

    @Override
    public LspDiagnosticsCustomizer getDiagnosticsCustomizer() {
      if (DartAnalysisServerService.isLspPublishDiagnosticsEnabled(getProject())) {
        return new LspDiagnosticsSupport();
      }
      else {
        return LspDiagnosticsDisabled.INSTANCE;
      }
    }

    @Override
    public LspCodeActionsCustomizer getCodeActionsCustomizer() {
      if (DartConfigurable.isLspCodeActionsEnabled(getProject())) {
        return new LspCodeActionsSupport();
      }
      else {
        return LspCodeActionsDisabled.INSTANCE;
      }
    }

    @Override
    public LspCommandsCustomizer getCommandsCustomizer() {
      if (DartConfigurable.isLspCodeActionsEnabled(getProject())) {
        return new LspCommandsSupport();
      }
      else {
        return LspCommandsDisabled.INSTANCE;
      }
    }

    @Override
    public LspFormattingCustomizer getFormattingCustomizer() {
      return LspFormattingDisabled.INSTANCE;
    }

    @Override
    public LspFindReferencesCustomizer getFindReferencesCustomizer() {
      if (DartAnalysisServerService.isLspReferencesEnabled(getProject())) {
        return new LspFindReferencesSupport();
      }
      else {
        return LspFindReferencesDisabled.INSTANCE;
      }
    }

    @Override
    public LspOptimizeImportsCustomizer getOptimizeImportsCustomizer() {
      return LspOptimizeImportsDisabled.INSTANCE;
    }

    @Override
    public LspDocumentColorCustomizer getDocumentColorCustomizer() {
      return LspDocumentColorDisabled.INSTANCE;
    }

    @Override
    public LspDocumentLinkCustomizer getDocumentLinkCustomizer() {
      return LspDocumentLinkDisabled.INSTANCE;
    }

    @Override
    public LspFoldingRangeCustomizer getFoldingRangeCustomizer() {
      return LspFoldingRangeDisabled.INSTANCE;
    }

    @Override
    public LspInlayHintCustomizer getInlayHintCustomizer() {
      if (DartAnalysisServerService.isLspInlayHintsEnabled(getProject())) {
        return new DartLspInlayHintSupport(getProject());
      }
      else {
        return LspInlayHintDisabled.INSTANCE;
      }
    }

    @Override
    public LspDocumentHighlightsCustomizer getDocumentHighlightsCustomizer() {
      return documentHighlightsCustomizer;
    }

    @Override
    public LspSignatureHelpCustomizer getSignatureHelpCustomizer() {
      if (DartConfigurable.isExperimentalLspFeaturesEnabled(getProject())) {
        return new LspSignatureHelpSupport();
      }
      else {
        return LspSignatureHelpDisabled.INSTANCE;
      }
    }

    @Override
    public LspDocumentSymbolCustomizer getDocumentSymbolCustomizer() {
      if (DartConfigurable.isExperimentalLspFeaturesEnabled(getProject())) {
        return new LspDocumentSymbolSupport();
      }
      else {
        return LspDocumentSymbolDisabled.INSTANCE;
      }
    }

    @Override
    public LspWorkspaceSymbolCustomizer getWorkspaceSymbolCustomizer() {
      return LspWorkspaceSymbolDisabled.INSTANCE;
    }

    @Override
    public LspCallHierarchyCustomizer getCallHierarchyCustomizer() {
      if (DartConfigurable.isExperimentalLspFeaturesEnabled(getProject())) {
        return new LspCallHierarchySupport();
      }
      else {
        return LspCallHierarchyDisabled.INSTANCE;
      }
    }

    @Override
    public LspTypeHierarchyCustomizer getTypeHierarchyCustomizer() {
      return typeHierarchyCustomizer;
    }

    @Override
    public LspSelectionRangeCustomizer getSelectionRangeCustomizer() {
      return LspSelectionRangeDisabled.INSTANCE;
    }

    @Override
    public LspCodeLensCustomizer getCodeLensCustomizer() {
      return LspCodeLensDisabled.INSTANCE;
    }

    @Override
    public LspRenameCustomizer getRenameCustomizer() {
      return LspRenameDisabled.INSTANCE;
    }
  };

  @Override
  public @NotNull LspCustomization getLspCustomization() {
    return lspCustomization;
  }
}
