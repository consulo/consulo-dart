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
package icons;

import consulo.dart.icon.DartIconGroup;
import consulo.ui.image.Image;

/**
 * Replaces the upstream generated {@code gen/icons/DartIcons.java}, backed by ICON-LIB
 * (see tool/consulo/sync_upstream_resources.py).
 */
public final class DartIcons {
  /** 13x13 */ public static final Image Dart_13 = DartIconGroup.dart_13();
  /** 16x16 */ public static final Image Dart_16 = DartIconGroup.dart_16();
  /** 16x16 */ public static final Image Dart_file = DartIconGroup.dart_file();
  /** 16x16 */ public static final Image Dart_remote = DartIconGroup.dart_remote();
  /** 16x16 */ public static final Image Dart_test = DartIconGroup.dart_test();
  /** 16x16 */ public static final Image DartWeb = DartIconGroup.dartweb();
  /** 13x13 */ public static final Image PubServeToolWindow = DartIconGroup.pubservetoolwindow();
  /** 6x6 */ public static final Image Warning_point = DartIconGroup.warning_point();

  private DartIcons() {
  }
}
