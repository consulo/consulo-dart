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
package consulo.dart.sdk;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.sdk.DartSdkUtil;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.Application;
import consulo.content.base.BinariesOrderRootType;
import consulo.content.base.DocumentationOrderRootType;
import consulo.content.base.SourcesOrderRootType;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkModificator;
import consulo.content.bundle.SdkType;
import consulo.platform.Platform;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import icons.DartIcons;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Dart SDK as a Consulo SDK. Upstream keeps the SDK as the "Dart SDK" project library, here the SDK is selected in
 * {@link consulo.dart.module.extension.DartModuleExtension}; the SDK roots are the ones upstream adds to its library.
 */
@ExtensionImpl
public class DartSdkType extends SdkType {
  private static final Set<String> ourAllowedRootTypeIds = Set.of(
    BinariesOrderRootType.ID,
    SourcesOrderRootType.ID,
    DocumentationOrderRootType.ID
  );

  public static DartSdkType getInstance() {
    return Application.get().getExtensionPoint(SdkType.class).findExtensionOrFail(DartSdkType.class);
  }

  public DartSdkType() {
    // id is kept from the old Consulo Dart plugin, so existing SDK definitions stay valid
    super("Dart SDK", DartBundle.messageValue("dart.title"), DartIcons.Dart_16);
  }

  @Override
  public boolean canCreatePredefinedSdks() {
    return true;
  }

  /**
   * Detection of installed SDKs: the {@code dart} executable in {@code PATH}, and the usual
   * install locations (archive, package managers, Flutter's bundled SDK).
   */
  @Override
  public Collection<String> suggestHomePaths() {
    Platform platform = Platform.current();
    Set<String> paths = new LinkedHashSet<>();

    Path dartExecutable = platform.findExecutableInPath("dart");
    if (dartExecutable != null) {
      try {
        // <sdk>/bin/dart, possibly a symlink (/usr/bin/dart, flutter/bin/dart is a script next to cache/dart-sdk)
        Path real = dartExecutable.toRealPath();
        addIfSdk(paths, real.getParent() == null ? null : real.getParent().getParent());
        addIfSdk(paths, dartExecutable.getParent().resolve("cache").resolve("dart-sdk"));
      }
      catch (IOException ignored) {
      }
    }

    Path home = platform.user().homePath();
    List<Path> candidates = new ArrayList<>();
    if (platform.os().isWindows()) {
      candidates.add(Path.of("C:\\tools\\dart-sdk"));
      String localAppData = platform.os().getEnvironmentVariable("LOCALAPPDATA");
      if (localAppData != null) {
        candidates.add(Path.of(localAppData, "dart-sdk"));
      }
      candidates.add(home.resolve("flutter").resolve("bin").resolve("cache").resolve("dart-sdk"));
    }
    else {
      if (platform.os().isMac()) {
        candidates.add(Path.of("/opt/homebrew/opt/dart/libexec"));
        candidates.add(Path.of("/usr/local/opt/dart/libexec"));
      }
      candidates.add(Path.of("/usr/lib/dart"));
      candidates.add(Path.of("/opt/dart-sdk"));
      candidates.add(Path.of("/opt/flutter/bin/cache/dart-sdk"));
      candidates.add(home.resolve("dart-sdk"));
      candidates.add(home.resolve("flutter").resolve("bin").resolve("cache").resolve("dart-sdk"));
      candidates.add(home.resolve("snap").resolve("flutter").resolve("common").resolve("flutter").resolve("bin").resolve("cache").resolve("dart-sdk"));
    }
    for (Path candidate : candidates) {
      addIfSdk(paths, candidate);
    }
    return paths;
  }

  @Override
  public Set<String> getEnvironmentVariables(Platform platform) {
    return Set.of("DART_SDK");
  }

  /**
   * Accepts the folder above the SDK and a Flutter SDK, which bundles the Dart SDK.
   */
  @Override
  public String adjustSelectedSdkHome(String homePath) {
    for (String relative : List.of("dart-sdk", "bin/cache/dart-sdk")) {
      String candidate = homePath + "/" + relative;
      if (DartSdkUtil.isDartSdkHome(candidate)) {
        return candidate;
      }
    }
    return homePath;
  }

  private static void addIfSdk(Set<String> paths, @Nullable Path path) {
    if (path != null && DartSdkUtil.isDartSdkHome(path.toString())) {
      paths.add(FileUtil.toSystemIndependentName(path.toString()));
    }
  }

  @Override
  public @Nullable String getVersionString(String sdkHome) {
    return DartSdkUtil.getSdkVersion(sdkHome);
  }

  @Override
  public boolean isValidSdkHome(String path) {
    return DartSdkUtil.isDartSdkHome(path);
  }

  @Override
  public boolean isRootTypeApplicable(String type) {
    return ourAllowedRootTypeIds.contains(type);
  }

  @Override
  public void setupSdkPaths(Sdk sdk) {
    String homePath = sdk.getHomePath();
    if (homePath == null) {
      return;
    }

    SdkModificator modificator = sdk.getSdkModificator();
    for (String url : DartSdkLibUtil.getRootUrlsFailover(homePath)) {
      VirtualFile root = VirtualFileManager.getInstance().refreshAndFindFileByUrl(url);
      if (root != null) {
        modificator.addRoot(root, BinariesOrderRootType.ID);
        modificator.addRoot(root, SourcesOrderRootType.ID);
      }
    }
    modificator.commitChanges();
  }
}
