/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.google.dart.server.ResponseListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.application.ApplicationManager;
import consulo.application.ReadAction;
import consulo.logging.Logger;
import consulo.project.Project;
import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.CallHierarchyIncomingCall;
import org.eclipse.lsp4j.CallHierarchyIncomingCallsParams;
import org.eclipse.lsp4j.CallHierarchyItem;
import org.eclipse.lsp4j.CallHierarchyOutgoingCall;
import org.eclipse.lsp4j.CallHierarchyOutgoingCallsParams;
import org.eclipse.lsp4j.CallHierarchyPrepareParams;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.ConfigurationItem;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.ExecuteCommandOptions;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.FileOperationFilter;
import org.eclipse.lsp4j.FileOperationOptions;
import org.eclipse.lsp4j.FileOperationPattern;
import org.eclipse.lsp4j.FileOperationsServerCapabilities;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.InlayHintParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameFilesParams;
import org.eclipse.lsp4j.SemanticTokens;
import org.eclipse.lsp4j.SemanticTokensLegend;
import org.eclipse.lsp4j.SemanticTokensParams;
import org.eclipse.lsp4j.SemanticTokensWithRegistrationOptions;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpOptions;
import org.eclipse.lsp4j.SignatureHelpParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TypeDefinitionParams;
import org.eclipse.lsp4j.TypeHierarchyItem;
import org.eclipse.lsp4j.TypeHierarchyPrepareParams;
import org.eclipse.lsp4j.TypeHierarchySubtypesParams;
import org.eclipse.lsp4j.TypeHierarchySupertypesParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.WorkspaceServerCapabilities;
import org.eclipse.lsp4j.jsonrpc.ResponseErrorException;
import org.eclipse.lsp4j.jsonrpc.json.MessageJsonHandler;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.jsonrpc.messages.ResponseError;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.LanguageClientAware;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

/**
 * DartBridgeLspServer acts as a lightweight translation bridge between the JetBrains LSP client
 * (which expects a standard LSP server) and the legacy Dart Analysis Server (DAS) running in legacy mode.
 *
 * <h2>Request Flow</h2>
 * When the JetBrains client sends an LSP request (e.g., {@link #hover}), the bridge intercepts it,
 * wraps it in a legacy DAS {@code lsp.handle} request, and forwards it to the existing DAS instance.
 * When DAS responds, the bridge extracts the nested LSP response and completes the future returned to the client.
 *
 * <h2>Migration Strategy: Shared vs LSP-Only Handlers</h2>
 * DAS contains two sets of LSP handlers:
 * <ol>
 * <li><b>Shared Handlers</b> (e.g., Hover, Formatting): These are registered in DAS even when running in legacy mode.
 *    We can forward these requests directly to DAS via {@code lsp.handle}.</li>
 * <li><b>LSP-Only Handlers</b> (e.g., Go to Definition, Completion): These are NOT registered in legacy DAS.
 *    If we forward them, DAS will return {@code MethodNotFound}. To support these, the bridge must manually
 *    intercept the LSP request, translate it to a legacy DAS request (e.g., {@code analysis.getNavigation} for Definition),
 *    and translate the legacy response back to LSP classes.</li>
 * </ol>
 *
 * <h2>Document Synchronization</h2>
 * Standard LSP document sync notifications ({@link #didOpen}, {@link #didChange}, {@link #didClose}) are ignored in this bridge.
 * The legacy Dart plugin already handles robust file content synchronization via {@code analysis.updateContent}.
 * Since DAS shares the same in-memory overlay filesystem between legacy and LSP handlers, the shared LSP handlers
 * (like Hover) will automatically see the up-to-date content synced by the legacy plugin.
 * To ensure the server is fully up to date before processing a request, we also explicitly call
 * {@code das.updateFilesContent()} before forwarding any client request.
 */
public final class DartBridgeLspServer implements DartLanguageServer, TextDocumentService, WorkspaceService, LanguageClientAware {
  private static final Logger logger = PluginLogger.INSTANCE.createLogger(DartBridgeLspServer.class);
  private static final String LSP_MESSAGE_KEY = "lspMessage";
  private static final String LSP_NOTIFICATION_KEY = "lspNotification";
  private static final String LSP_NOTIFICATION_EVENT = "lsp.notification";
  private static final String LSP_RESPONSE_KEY = "lspResponse";
  private static final String JSONRPC_VERSION = "2.0";

  // We use a JSON handler with default lsp4j configuration to serialize/deserialize lsp4j objects.
  private static final MessageJsonHandler JSON_HANDLER = new MessageJsonHandler(Map.of());
  private static final Gson GSON = JSON_HANDLER.getGson();

  private final @NotNull Project project;

  private volatile @Nullable LanguageClient client;
  private volatile boolean initialized = false;
  private final ConcurrentHashMap<String, PendingRequest<?>> pendingRequests = new ConcurrentHashMap<>();
  private volatile boolean isSemanticTokensAdvertised = false;
  private @Nullable ResponseListener dasMessageListener;

  // Overridable in tests.
  private @NotNull BooleanSupplier isLspHighlightingEnabled;

  public DartBridgeLspServer(@NotNull Project project) {
    this.project = project;
    this.isLspHighlightingEnabled = () -> DartAnalysisServerService.isLspHighlightingEnabled(project);
    setupDasMessageListener();
  }

  public boolean isClientConnected() {
    return client != null;
  }

  public boolean isSemanticTokensAdvertised() {
    return isSemanticTokensAdvertised;
  }

  public @NotNull BooleanSupplier isLspHighlightingEnabled() {
    return isLspHighlightingEnabled;
  }

  public void setLspHighlightingEnabled(@NotNull BooleanSupplier isLspHighlightingEnabled) {
    this.isLspHighlightingEnabled = isLspHighlightingEnabled;
  }

  private @NotNull DartAnalysisServerService getDas() {
    return DartAnalysisServerService.getInstance(project);
  }

  @Override
  public void connect(LanguageClient client) {
    this.client = client;
    logger.info("Connected to LanguageClient");
  }

  /**
   * Sets up a listener on the legacy Dart Analysis Server (DAS) to intercept messages
   * (responses, notifications, and reverse requests) containing LSP payloads.
   */
  private void setupDasMessageListener() {
    ResponseListener listener = rawMessage -> {
      // Intercept only those messages that contain LSP payload keys.
      if (!rawMessage.contains(LSP_MESSAGE_KEY) && !rawMessage.contains(LSP_RESPONSE_KEY) && !rawMessage.contains(LSP_NOTIFICATION_KEY)) {
        return;
      }

      try {
        JsonObject jsonObject = JsonParser.parseString(rawMessage).getAsJsonObject();
        handleDasMessage(jsonObject);
      }
      catch (Exception e) {
        logger.error("Error handling DAS message", e);
      }
    };
    this.dasMessageListener = listener;
    getDas().addResponseListener(listener);
  }

  private static @Nullable JsonObject asJsonObjectOrNull(@Nullable JsonElement element) {
    return element != null && element.isJsonObject() ? element.getAsJsonObject() : null;
  }

  private static @Nullable JsonArray asJsonArrayOrNull(@Nullable JsonElement element) {
    return element != null && element.isJsonArray() ? element.getAsJsonArray() : null;
  }

  /**
   * Processes raw JSON messages from the Dart Analysis Server.
   * Depending on the payload, it either:
   * <ol>
   * <li>Forwards server-initiated messages (e.g. publishDiagnostics, applyEdit) directly to the LSP client.</li>
   * <li>Resolves matching pending request futures for client-initiated requests (e.g. hover, codeAction).</li>
   * </ol>
   */
  private void handleDasMessage(@NotNull JsonObject jsonObject) {
    JsonElement idElement = jsonObject.get("id");
    String topLevelId = idElement != null && idElement.isJsonPrimitive() ? idElement.getAsString() : null;

    // Check if it's a server-initiated notification or request from DAS.
    if (jsonObject.has("params")) {
      JsonObject params = jsonObject.get("params").getAsJsonObject();
      JsonObject msgObj;
      if (params.has(LSP_NOTIFICATION_KEY)) {
        msgObj = params.get(LSP_NOTIFICATION_KEY).getAsJsonObject();
      }
      else if (params.has(LSP_MESSAGE_KEY)) {
        msgObj = params.get(LSP_MESSAGE_KEY).getAsJsonObject();
      }
      else {
        msgObj = null;
      }
      if (msgObj != null) {
        JsonPrimitive methodPrimitive = msgObj.getAsJsonPrimitive("method");
        String method = methodPrimitive != null ? methodPrimitive.getAsString() : null;
        if (method != null) {
          // Forward server message to client.
          forwardServerMessageToClient(topLevelId, method, msgObj);
        }
        return;
      }
    }

    // Check if it's a response to a client-initiated request.
    if (topLevelId != null) {
      handlePendingRequestResponse(topLevelId, jsonObject);
    }
  }

  private void handlePendingRequestResponse(@NotNull String topLevelId, @NotNull JsonObject jsonObject) {
    PendingRequest<?> pending = pendingRequests.remove(topLevelId);
    if (pending == null) return;
    JsonObject topLevelError = asJsonObjectOrNull(jsonObject.get("error"));
    if (topLevelError != null) {
      pending.completeExceptionally(topLevelError);
      return;
    }

    JsonObject result = asJsonObjectOrNull(jsonObject.get("result"));
    JsonObject lspResponse = result != null ? asJsonObjectOrNull(result.get(LSP_RESPONSE_KEY)) : null;

    JsonObject lspError = lspResponse != null ? asJsonObjectOrNull(lspResponse.get("error")) : null;
    if (lspError != null) {
      pending.completeExceptionally(lspError);
    }
    else {
      pending.complete(lspResponse != null ? lspResponse.get("result") : null);
    }
  }

  private void forwardServerMessageToClient(@Nullable String dasRequestId, @NotNull String method, @NotNull JsonObject msgObj) {
    try {
      if (method.equals("workspace/configuration")) {
        handleWorkspaceConfiguration(this.client, dasRequestId, msgObj);
        return;
      }
      LanguageClient client = this.client;
      if (client == null) return;
      // Parse and forward notifications/requests to the LSP client proxy using lsp4j.
      switch (method) {
        case "textDocument/publishDiagnostics" -> handlePublishDiagnostics(client, msgObj);
        case "dart/textDocument/publishClosingLabels" -> handlePublishClosingLabels(client, msgObj);
        case "workspace/applyEdit" -> handleApplyEdit(client, dasRequestId, msgObj);
        default -> logger.debug("Ignored notification/request from DAS: " + method);
      }
    }
    catch (Exception e) {
      logger.error("Failed to forward server message for method: " + method, e);
    }
  }

  private void handlePublishDiagnostics(@NotNull LanguageClient client, @NotNull JsonObject msgObj) {
    JsonElement paramsObj = msgObj.get("params");
    PublishDiagnosticsParams params = GSON.fromJson(paramsObj, PublishDiagnosticsParams.class);
    client.publishDiagnostics(params);
  }

  private void handlePublishClosingLabels(@NotNull LanguageClient client, @NotNull JsonObject msgObj) {
    JsonElement paramsObj = msgObj.get("params");
    DartPublishClosingLabelsParams params = GSON.fromJson(paramsObj, DartPublishClosingLabelsParams.class);
    if (params == null) return;
    if (client instanceof DartLanguageClient dartClient) {
      dartClient.publishClosingLabels(params);
    }
  }

  private void handleApplyEdit(@NotNull LanguageClient client, @Nullable String dasRequestId, @NotNull JsonObject msgObj) {
    JsonElement paramsObj = msgObj.get("params");
    ApplyWorkspaceEditParams params = GSON.fromJson(paramsObj, ApplyWorkspaceEditParams.class);
    JsonElement lspId = msgObj.get("id");
    client.applyEdit(params).whenComplete((response, error) -> {
      if (dasRequestId != null) {
        JsonObject lspResponse = new JsonObject();
        lspResponse.addProperty("jsonrpc", JSONRPC_VERSION);
        if (lspId != null) {
          lspResponse.add("id", lspId);
        }
        if (error != null) {
          JsonObject errorObj = new JsonObject();
          errorObj.addProperty("code", -32603);
          errorObj.addProperty("message", error.getMessage() != null ? error.getMessage() : "Internal error");
          lspResponse.add("error", errorObj);
        }
        else {
          lspResponse.add("result", GSON.toJsonTree(response));
        }
        JsonObject legacyResponse = createLegacyResponse(dasRequestId, lspResponse);
        try {
          getDas().sendResponse(legacyResponse);
        }
        catch (Exception e) {
          logger.error("Failed to send applyEdit response to DAS", e);
        }
      }
    });
  }

  private static @NotNull JsonObject createLegacyResponse(@NotNull String dasRequestId, @NotNull JsonObject lspResponse) {
    JsonObject legacyResponse = new JsonObject();
    legacyResponse.addProperty("id", dasRequestId);
    JsonObject result = new JsonObject();
    result.add(LSP_RESPONSE_KEY, lspResponse);
    legacyResponse.add("result", result);
    return legacyResponse;
  }

  private void handleWorkspaceConfiguration(@Nullable LanguageClient client, @Nullable String dasRequestId, @NotNull JsonObject msgObj) {
    JsonObject paramsObj = asJsonObjectOrNull(msgObj.get("params"));
    JsonArray rawItems = paramsObj != null ? asJsonArrayOrNull(paramsObj.get("items")) : null;
    int rawItemsCount = rawItems != null ? rawItems.size() : 0;
    ConfigurationParams parsedParams;
    if (paramsObj != null) {
      ConfigurationParams parsed;
      try {
        parsed = GSON.fromJson(paramsObj, ConfigurationParams.class);
      }
      catch (Exception e) {
        logger.warn("Failed to parse workspace/configuration params; falling back to null entries", e);
        parsed = null;
      }
      parsedParams = parsed;
    }
    else {
      parsedParams = null;
    }
    List<ConfigurationItem> items = parsedParams != null ? parsedParams.getItems() : null;
    int itemCount = items != null ? items.size() : rawItemsCount;
    JsonElement lspId = msgObj.get("id");
    CompletableFuture<List<Object>> future;
    if (client != null && parsedParams != null) {
      parsedParams.setItems(items != null ? items : List.of());
      CompletableFuture<List<Object>> configurationFuture;
      try {
        configurationFuture = client.configuration(parsedParams);
      }
      catch (Throwable e) {
        configurationFuture = CompletableFuture.failedFuture(e);
      }
      future = configurationFuture;
    }
    else {
      future = CompletableFuture.completedFuture(null);
    }
    future.whenComplete((response, error) -> {
      if (dasRequestId != null) {
        if (error != null) {
          logger.warn("Error computing workspace/configuration; falling back to null entries", error);
        }
        // The server blocks its initialization until it gets an answer, and the LSP protocol
        // expects one result per requested section in the same order (null for unknown sections).
        JsonArray resultArray = new JsonArray();
        for (int i = 0; i < itemCount; i++) {
          Object itemConfig = error == null && response != null && i < response.size() ? response.get(i) : null;
          resultArray.add(itemConfig != null ? GSON.toJsonTree(itemConfig) : JsonNull.INSTANCE);
        }
        JsonObject lspResponse = new JsonObject();
        lspResponse.addProperty("jsonrpc", JSONRPC_VERSION);
        if (lspId != null) {
          lspResponse.add("id", lspId);
        }
        lspResponse.add("result", resultArray);
        JsonObject legacyResponse = createLegacyResponse(dasRequestId, lspResponse);
        try {
          getDas().sendResponse(legacyResponse);
        }
        catch (Exception e) {
          logger.error("Failed to send workspace/configuration response to DAS", e);
        }
      }
    });
  }

  public void stop() {
    client = null;
    ResponseListener listener = dasMessageListener;
    if (listener != null) {
      getDas().removeResponseListener(listener);
      dasMessageListener = null;
    }
    pendingRequests.forEach((id, pending) -> pending.future.cancel(true));
    pendingRequests.clear();
    if (initialized) {
      initialized = false;
      getDas().updateClientCapabilities(false);
    }
    isSemanticTokensAdvertised = false;
    getDas().setSemanticTokensAdvertised(false);
  }

  // --- LanguageServer Implementation ---

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params) {
    logger.info("Initialize called");
    initialized = true;
    DartAnalysisServerService das = getDas();
    das.updateClientCapabilities(true);
    CompletableFuture<SemanticTokensLegend> legendFuture = das.getSemanticTokensLegendFuture();
    CompletableFuture<SemanticTokensLegend> legendCompletableFuture;
    if (!isLspHighlightingEnabled.getAsBoolean()) {
      legendCompletableFuture = CompletableFuture.completedFuture(null);
    }
    else if (legendFuture.isDone()) {
      legendCompletableFuture = CompletableFuture.completedFuture(das.getSemanticTokensLegend());
    }
    else if (das.isServerProcessActive()) {
      legendCompletableFuture = legendFuture.copy().completeOnTimeout(null, 3, TimeUnit.SECONDS);
    }
    else {
      legendCompletableFuture = CompletableFuture.completedFuture(null);
    }

    return legendCompletableFuture.thenApply(serverLegend -> {
      ServerCapabilities capabilities = new ServerCapabilities();
      capabilities.setHoverProvider(true);
      capabilities.setDefinitionProvider(true);
      capabilities.setTypeDefinitionProvider(true);
      capabilities.setDocumentHighlightProvider(true);
      capabilities.setInlayHintProvider(true);
      capabilities.setTypeHierarchyProvider(true);
      capabilities.setCallHierarchyProvider(true);
      capabilities.setReferencesProvider(true);
      capabilities.setDocumentSymbolProvider(true);
      capabilities.setCompletionProvider(new CompletionOptions(true, List.of(".", "=", "'", "\"", "/", "@", ":")));
      if (serverLegend != null) {
        SemanticTokensWithRegistrationOptions semanticTokensOptions = new SemanticTokensWithRegistrationOptions();
        semanticTokensOptions.setLegend(serverLegend);
        semanticTokensOptions.setFull(true);
        semanticTokensOptions.setRange(false);
        capabilities.setSemanticTokensProvider(semanticTokensOptions);
      }
      FileOperationsServerCapabilities fileOperationsCaps = new FileOperationsServerCapabilities();
      fileOperationsCaps.setWillRename(new FileOperationOptions(List.of(new FileOperationFilter(new FileOperationPattern("**/*")))));
      WorkspaceServerCapabilities workspace = new WorkspaceServerCapabilities();
      workspace.setFileOperations(fileOperationsCaps);
      capabilities.setWorkspace(workspace);
      capabilities.setCodeActionProvider(true);
      capabilities.setExecuteCommandProvider(new ExecuteCommandOptions());
      // Include "," in triggerCharacters (not only retriggerCharacters) because
      // LspServerImpl.getSignatureHelpTriggerCharacters only inspects triggerCharacters.
      capabilities.setSignatureHelpProvider(new SignatureHelpOptions(List.of("(", ","), List.of(",")));
      // Add other capabilities as we support them.
      boolean advertised = serverLegend != null;
      isSemanticTokensAdvertised = advertised;
      das.setSemanticTokensAdvertised(advertised);
      return new InitializeResult(capabilities);
    });
  }

  @Override
  public void initialized(InitializedParams params) {
    logger.info("Initialized called");
  }

  @Override
  public CompletableFuture<Object> shutdown() {
    logger.info("Shutdown called");
    stop();
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void exit() {
    logger.info("Exit called");
  }

  @Override
  public TextDocumentService getTextDocumentService() {
    return this;
  }

  @Override
  public WorkspaceService getWorkspaceService() {
    return this;
  }

  // --- TextDocumentService Implementation ---

  @Override
  public CompletableFuture<Hover> hover(HoverParams params) {
    return forwardRequest("textDocument/hover", params, Hover.class);
  }

  // Note: We advertise linkSupport: true in server.setClientCapabilities (see RequestUtilities.java)
  // so DAS is guaranteed to return List<LocationLink> for textDocument/definition.
  @Override
  public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition(DefinitionParams params) {
    Type type = new TypeToken<List<LocationLink>>() {
    }.getType();
    return this.<List<LocationLink>>forwardRequest("textDocument/definition", params, type).thenApply(links ->
      Either.<List<? extends Location>, List<? extends LocationLink>>forRight(links != null ? links : List.of())
    );
  }

  @Override
  public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition(TypeDefinitionParams params) {
    Type type = new TypeToken<List<LocationLink>>() {
    }.getType();
    return this.<List<LocationLink>>forwardRequest("textDocument/typeDefinition", params, type).thenApply(links ->
      Either.<List<? extends Location>, List<? extends LocationLink>>forRight(links != null ? links : List.of())
    );
  }

  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams params) {
    return forwardRequest("textDocument/completion", params, CompletionList.class).thenApply(list ->
      Either.<List<CompletionItem>, CompletionList>forRight(list != null ? list : new CompletionList(false, List.of()))
    ).exceptionally(e -> {
      logger.info("textDocument/completion failed: " + e.getMessage());
      return Either.forRight(new CompletionList(false, List.of()));
    });
  }

  @Override
  public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
    return forwardRequest("completionItem/resolve", unresolved, CompletionItem.class).thenApply(resolved ->
      resolved != null ? resolved : unresolved
    ).exceptionally(e -> {
      logger.info("completionItem/resolve failed: " + e.getMessage());
      return unresolved;
    });
  }

  @Override
  public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
    Type type = new TypeToken<List<DocumentHighlight>>() {
    }.getType();
    return this.<List<? extends DocumentHighlight>>forwardRequest("textDocument/documentHighlight", params, type);
  }

  @Override
  public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
    Type type = new TypeToken<List<Location>>() {
    }.getType();

    return forwardRequest("textDocument/references", params, type);
  }

  // Unlike the other overrides above, this one must never let its future complete
  // exceptionally: the LSP client's inlay-hint cache awaits this future inside a coroutine
  // without catching exceptions, so a DAS error (e.g. FileNotAnalyzed) would otherwise
  // surface as an uncaught IDE error instead of simply showing no hints.
  @Override
  public CompletableFuture<List<InlayHint>> inlayHint(InlayHintParams params) {
    Type type = new TypeToken<List<InlayHint>>() {
    }.getType();
    return this.<List<InlayHint>>forwardRequest("textDocument/inlayHint", params, type)
      .handle((hints, error) -> {
        if (error != null) {
          logger.info("textDocument/inlayHint failed: " + error.getMessage());
          return List.of();
        }
        else {
          return hints != null ? hints : List.of();
        }
      });
  }

  @Override
  public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
    return forwardRequest("textDocument/semanticTokens/full", params, SemanticTokens.class).exceptionally(e -> {
      logger.debug("textDocument/semanticTokens/full failed: " + e.getMessage());
      return null;
    });
  }

  @Override
  public @NotNull CompletableFuture<DiagnosticServerResult> diagnosticServer() {
    return forwardRequest("dart/diagnosticServer", null, DiagnosticServerResult.class);
  }

  @Override
  public @NotNull CompletableFuture<Object> connectToDtd(@NotNull ConnectToDtdParams params) {
    return forwardRequest("dart/connectToDtd", params, Object.class).exceptionally(e -> {
      Throwable cause = e.getCause() != null ? e.getCause() : e;
      // Ignore ServerErrorCodes.stateError (-32015: "Server is already connected to DTD"),
      // which can occur if the LSP bridge restarts while the underlying DAS process stays alive.
      if (cause instanceof ResponseErrorException responseErrorException &&
          responseErrorException.getResponseError() != null &&
          responseErrorException.getResponseError().getCode() == -32015) {
        logger.debug("DAS is already connected to DTD: " + responseErrorException.getResponseError().getMessage());
        return null;
      }
      else {
        throw e instanceof RuntimeException runtimeException ? runtimeException : new RuntimeException(e);
      }
    });
  }

  // Note: We advertise codeActionLiteralSupport in server.setClientCapabilities (see DartAnalysisServerService.buildLspCapabilities)
  // so DAS is guaranteed to return List<CodeAction> for textDocument/codeAction.
  @Override
  public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
    Type responseType = new TypeToken<List<CodeAction>>() {
    }.getType();
    return this.<List<CodeAction>>forwardRequest("textDocument/codeAction", params, responseType).thenApply(actions -> {
      if (actions == null) return List.of();
      List<Either<Command, CodeAction>> result = new ArrayList<>(actions.size());
      for (CodeAction action : actions) {
        result.add(Either.forRight(action));
      }
      return result;
    });
  }

  @Override
  public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(DocumentSymbolParams params) {
    Type type = new TypeToken<List<DocumentSymbol>>() {
    }.getType();
    return this.<List<DocumentSymbol>>forwardRequest("textDocument/documentSymbol", params, type).thenApply(symbols -> {
      if (symbols == null) return List.of();
      List<Either<SymbolInformation, DocumentSymbol>> result = new ArrayList<>(symbols.size());
      for (DocumentSymbol symbol : symbols) {
        result.add(Either.forRight(symbol));
      }
      return result;
    });
  }

  @Override
  public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
    return forwardRequest("textDocument/signatureHelp", params, SignatureHelp.class);
  }

  // Implement other TextDocumentService methods as needed, returning unsupported or forwarding.

  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
    // Ignored. Document synchronization is handled by the legacy DartAnalysisServerService.
  }

  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    // Ignored. Document synchronization is handled by the legacy DartAnalysisServerService.
  }

  @Override
  public void didClose(DidCloseTextDocumentParams params) {
    // Ignored. Document synchronization is handled by the legacy DartAnalysisServerService.
  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {
    // Ignored. Document synchronization is handled by the legacy DartAnalysisServerService.
  }

  // --- WorkspaceService Implementation ---

  @Override
  public CompletableFuture<WorkspaceEdit> willRenameFiles(RenameFilesParams params) {
    return forwardRequest("workspace/willRenameFiles", params, WorkspaceEdit.class);
  }

  @Override
  public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
    return forwardRequest("workspace/executeCommand", params, Object.class);
  }

  @Override
  public void didChangeConfiguration(DidChangeConfigurationParams params) {
    // Ignored. Configuration is managed by the legacy plugin settings.
  }

  @Override
  public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
    // Ignored. File watching is handled by the legacy plugin.
  }

  // --- Type Hierarchy ---

  @Override
  public CompletableFuture<List<TypeHierarchyItem>> prepareTypeHierarchy(TypeHierarchyPrepareParams params) {
    Type type = new TypeToken<List<TypeHierarchyItem>>() {
    }.getType();
    return forwardRequest("textDocument/prepareTypeHierarchy", params, type);
  }

  @Override
  public CompletableFuture<List<TypeHierarchyItem>> typeHierarchySupertypes(TypeHierarchySupertypesParams params) {
    Type type = new TypeToken<List<TypeHierarchyItem>>() {
    }.getType();
    return forwardRequest("typeHierarchy/supertypes", params, type);
  }

  @Override
  public CompletableFuture<List<TypeHierarchyItem>> typeHierarchySubtypes(TypeHierarchySubtypesParams params) {
    Type type = new TypeToken<List<TypeHierarchyItem>>() {
    }.getType();
    return forwardRequest("typeHierarchy/subtypes", params, type);
  }

  // --- Call Hierarchy ---

  @Override
  public CompletableFuture<List<CallHierarchyItem>> prepareCallHierarchy(CallHierarchyPrepareParams params) {
    Type type = new TypeToken<List<CallHierarchyItem>>() {
    }.getType();
    return forwardRequest("textDocument/prepareCallHierarchy", params, type);
  }

  @Override
  public CompletableFuture<List<CallHierarchyIncomingCall>> callHierarchyIncomingCalls(CallHierarchyIncomingCallsParams params) {
    Type type = new TypeToken<List<CallHierarchyIncomingCall>>() {
    }.getType();
    return forwardRequest("callHierarchy/incomingCalls", params, type);
  }

  @Override
  public CompletableFuture<List<CallHierarchyOutgoingCall>> callHierarchyOutgoingCalls(CallHierarchyOutgoingCallsParams params) {
    Type type = new TypeToken<List<CallHierarchyOutgoingCall>>() {
    }.getType();
    return forwardRequest("callHierarchy/outgoingCalls", params, type);
  }


  // --- Helper Methods for Forwarding ---

  /**
   * Forwards an LSP request to the legacy DAS.
   * <p>
   * It wraps the LSP request in a legacy DAS {@code lsp.handle} request.
   * We register a {@link PendingRequest} associated with the {@code legacyId} so that when DAS returns
   * the response, we can match it, extract the inner LSP response payload, and complete the future.
   * <p>
   * Note: We use the same {@code legacyId} for both the outer legacy request and the inner LSP request
   * to simplify tracking and matching.
   */
  private <T> @NotNull CompletableFuture<T> forwardRequest(@NotNull String method, @Nullable Object params, @NotNull Class<T> responseClass) {
    return forwardRequest(method, params, (Type)responseClass);
  }

  private <T> @NotNull CompletableFuture<T> forwardRequest(@NotNull String method, @Nullable Object params, @NotNull Type responseType) {
    CompletableFuture<T> future = new CompletableFuture<>();
    DartAnalysisServerService das = getDas();

    boolean ready;
    if (das.isServerProcessActive()) {
      ready = true;
    }
    else {
      boolean readyForRequest;
      try {
        readyForRequest = ReadAction.compute(() -> das.serverReadyForRequest());
      }
      catch (Throwable e) {
        readyForRequest = false;
      }
      ready = readyForRequest;
    }
    if (!ready) {
      future.completeExceptionally(new ResponseErrorException(new ResponseError(-32001, "Dart Analysis Server is not ready", null)));
      return future;
    }

    ApplicationManager.getApplication().tryRunReadAction(() -> das.updateFilesContent());

    String legacyId = das.generateUniqueId();
    if (legacyId == null) {
      future.completeExceptionally(new ResponseErrorException(new ResponseError(-32001, "Failed to generate request ID", null)));
      return future;
    }

    PendingRequest<T> pending = new PendingRequest<>(future, responseType);
    pendingRequests.put(legacyId, pending);

    JsonObject lspRequest = new JsonObject();
    lspRequest.addProperty("jsonrpc", JSONRPC_VERSION);
    lspRequest.addProperty("id", legacyId);
    lspRequest.addProperty("method", method);
    if (params != null) {
      lspRequest.add("params", GSON.toJsonTree(params));
    }

    JsonObject legacyRequest = new JsonObject();
    legacyRequest.addProperty("id", legacyId);
    legacyRequest.addProperty("method", "lsp.handle");
    JsonObject legacyParams = new JsonObject();
    legacyParams.add("lspMessage", lspRequest);
    legacyRequest.add("params", legacyParams);

    try {
      das.sendRequest(legacyId, legacyRequest);
    }
    catch (Exception e) {
      logger.error("Failed to send request to DAS for method: " + method, e);
      pendingRequests.remove(legacyId);
      future.completeExceptionally(e);
    }

    return future;
  }

  /**
   * Forwards an LSP notification to the legacy DAS.
   * <p>
   * It wraps the LSP notification in a legacy {@code lsp.notification} notification - the same envelope
   * the server uses for the notifications it sends to us, see {@code forwardNotificationToClient}.
   * An {@code lsp.handle} request would not do: that is a legacy request, and the server answers every
   * request, while an LSP notification has no response to answer with.
   * <p>
   * Returns whether the notification was handed to the analysis server. A notification is not
   * answered, so that is as much as the caller can be told - and it has to be told, because a
   * caller that tracks what the server knows would otherwise wait forever for an effect that
   * never comes.
   */
  public boolean forwardNotification(@NotNull String method, @NotNull Object params) {
    JsonObject lspNotification = new JsonObject();
    lspNotification.addProperty("jsonrpc", JSONRPC_VERSION);
    lspNotification.addProperty("method", method);
    lspNotification.add("params", GSON.toJsonTree(params));

    JsonObject legacyNotification = new JsonObject();
    legacyNotification.addProperty("event", LSP_NOTIFICATION_EVENT);
    JsonObject legacyParams = new JsonObject();
    legacyParams.add(LSP_NOTIFICATION_KEY, lspNotification);
    legacyNotification.add("params", legacyParams);

    try {
      // A server that is going away is not a plugin bug, so this must not be logged as an
      // error: that raises an IDE fatal-error notification in internal and EAP builds.
      boolean sent = getDas().sendNotification(legacyNotification);
      if (!sent) logger.warn("No analysis server to send the notification to: " + legacyNotification);
      return sent;
    }
    catch (Exception e) {
      logger.warn("Failed to send notification to DAS for method: " + method, e);
      return false;
    }
  }

  // Helper class to store pending request info.
  private final class PendingRequest<T> {
    final @NotNull CompletableFuture<T> future;
    final @NotNull Type responseType;

    PendingRequest(@NotNull CompletableFuture<T> future, @NotNull Type responseType) {
      this.future = future;
      this.responseType = responseType;
    }

    void complete(@Nullable JsonElement resultPayload) {
      if (resultPayload == null || resultPayload.isJsonNull()) {
        future.complete(null);
        return;
      }
      try {
        T result = GSON.fromJson(resultPayload, responseType);
        future.complete(result);
      }
      catch (Exception e) {
        future.completeExceptionally(e);
      }
    }

    void completeExceptionally(@NotNull JsonObject error) {
      JsonElement codeElement = error.get("code");
      int code = codeElement != null ? codeElement.getAsInt() : -32603;
      JsonElement messageElement = error.get("message");
      String message = messageElement != null ? messageElement.getAsString() : "Unknown error";
      future.completeExceptionally(new ResponseErrorException(new ResponseError(code, message, null)));
    }
  }
}
