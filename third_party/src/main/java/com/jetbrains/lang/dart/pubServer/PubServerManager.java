// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.pubServer;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.net.HttpURLConnection;
import consulo.builtinWebServer.http.HttpResponse;
import consulo.builtinWebServer.http.HttpRequest;
import consulo.dart.compat.ConsoleManager;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceImpl;
import consulo.annotation.component.ServiceAPI;
import consulo.disposer.Disposable;
import consulo.logging.Logger;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.project.Project;
import consulo.disposer.Disposer;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.event.VirtualFileEvent;
import consulo.virtualFileSystem.event.VirtualFileListener;
import consulo.virtualFileSystem.VirtualFileManager;
import consulo.virtualFileSystem.event.VirtualFileMoveEvent;
import consulo.virtualFileSystem.event.VirtualFilePropertyEvent;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.util.PubspecYamlUtil;
import org.jetbrains.annotations.NotNull;

@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class PubServerManager implements Disposable {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(PubServerManager.class);

  private final Project project;
  private final ConsoleManager consoleManager = new ConsoleManager();

  private String myServedSdkVersion;

  // Consulo: no Caffeine, a concurrent map is enough for this cache without eviction
  private final ConcurrentMap<VirtualFile, PubServerService> myServedDirToPubService = new ConcurrentHashMap<>();

  public static @NotNull PubServerManager getInstance(@NotNull Project project) {
    return project.getInstance(PubServerManager.class);
  }

  @Inject
  public PubServerManager(@NotNull Project project) {
    this.project = project;

    VirtualFileManager.getInstance()
      .addVirtualFileListener(new VirtualFileListener() {
                                @Override
                                public void beforePropertyChange(final @NotNull VirtualFilePropertyEvent event) {
                                  if (VirtualFile.PROP_NAME.equals(event.getPropertyName())) {
                                    contentsChanged(event);
                                  }
                                }

                                @Override
                                public void beforeFileMovement(final @NotNull VirtualFileMoveEvent event) {
                                  contentsChanged(event);
                                }

                                @Override
                                public void fileDeleted(final @NotNull VirtualFileEvent event) {
                                  contentsChanged(event);
                                }

                                @Override
                                public void contentsChanged(final @NotNull VirtualFileEvent event) {
                                  final VirtualFile file = event.getFile();
                                  if (PubspecYamlUtil.PUBSPEC_YAML.equals(file.getName()) &&
                                      file.getFileSystem() == LocalFileSystem.getInstance()) {
                                    pubspecYamlChanged(file);
                                  }
                                }
                              },
                              this);
  }

  private void pubspecYamlChanged(final @NotNull VirtualFile file) {
    final VirtualFile mainDir = file.getParent();
    if (mainDir == null) return;

    for (VirtualFile subdir : mainDir.getChildren()) {
      if (!subdir.isDirectory()) continue;

      final PubServerService service = myServedDirToPubService.remove(subdir);
      if (service != null) {
        Disposer.dispose(service);
      }
    }
  }

  public @NotNull HttpResponse send(@NotNull HttpRequest clientRequest,
                                    @NotNull VirtualFile servedDir,
                                    @NotNull String pathForPubServer) {
    final DartSdk sdk = DartSdk.getDartSdk(project);
    if (sdk != null && !sdk.getVersion().equals(myServedSdkVersion)) {
      stopAllPubServerProcesses();
      myServedSdkVersion = sdk.getVersion();
    }

    try {
      // servedDir - web or test, direct child of directory containing pubspec.yaml
      final PubServerService service = myServedDirToPubService.computeIfAbsent(servedDir, key -> new PubServerService(project, consoleManager));
      return HttpResponse.deferred(service.sendToPubServer(clientRequest, servedDir, pathForPubServer));
    }
    catch (Exception e) {
      LOG.error(e);
      return HttpResponse.create(HttpURLConnection.HTTP_BAD_GATEWAY, null, null);
    }
  }

  public boolean hasAlivePubServerProcesses() {
    for (PubServerService service : myServedDirToPubService.values()) {
      if (service.isPubServerProcessAlive()) return true;
    }
    return false;
  }

  @Override
  public void dispose() {
    stopAllPubServerProcesses();
  }

  public void stopAllPubServerProcesses() {
    for (PubServerService service : myServedDirToPubService.values()) {
      try {
        Disposer.dispose(service);
      }
      catch (Exception e) {
        LOG.error(e);
      }
    }
  }
}
