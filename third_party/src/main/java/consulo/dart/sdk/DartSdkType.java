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
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.VirtualFileManager;
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
