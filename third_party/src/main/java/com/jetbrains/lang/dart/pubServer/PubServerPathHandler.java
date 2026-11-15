// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.pubServer;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.ReadAction;
import consulo.builtinWebServer.http.HttpRequest;
import consulo.builtinWebServer.http.HttpResponse;
import consulo.builtinWebServer.webServer.WebServerPathHandler;
import consulo.project.Project;
import consulo.util.lang.Pair;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Consulo: the path handler answers with an {@link HttpResponse} instead of writing to the Netty channel.
 */
@ExtensionImpl
public final class PubServerPathHandler implements WebServerPathHandler {
  @Override
  public @Nullable HttpResponse process(@NotNull String path,
                                        @NotNull Project project,
                                        @NotNull HttpRequest request,
                                        @NotNull String projectName,
                                        boolean isCustomHost) {
    final Pair<VirtualFile, String> servedDirAndPathForPubServer =
      ReadAction.nonBlocking(() -> PubServerPathHandlerKt.getServedDirAndPathForPubServer(project, path, projectName)).executeSynchronously();
    if (servedDirAndPathForPubServer == null) return null;

    return PubServerManager.getInstance(project).send(request, servedDirAndPathForPubServer.first, servedDirAndPathForPubServer.second);
  }
}
