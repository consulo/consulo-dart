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
package consulo.dart.notification;

import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ExtensionImpl;
import consulo.project.ui.notification.NotificationDisplayType;
import consulo.project.ui.notification.NotificationGroup;
import consulo.project.ui.notification.NotificationGroupContributor;

import java.util.List;
import java.util.function.Consumer;

/**
 * Notification groups of upstream plugin.xml. {@link #get(String)} replaces
 * {@code NotificationGroupManager.getInstance().getNotificationGroup(id)} at upstream call sites.
 */
@ExtensionImpl
public class DartNotificationGroups implements NotificationGroupContributor {
  public static final NotificationGroup DART_SUPPORT =
    new NotificationGroup("Dart Support", DartBundle.messageValue("notification.group.dart.support"), NotificationDisplayType.BALLOON, true);
  public static final NotificationGroup DART_ANALYSIS =
    new NotificationGroup("Dart Analysis", DartBundle.messageValue("notification.group.dart.analysis"), NotificationDisplayType.TOOL_WINDOW, false, "Dart Analysis");
  public static final NotificationGroup DART_WEBDEV =
    new NotificationGroup("Dart Webdev", DartBundle.messageValue("notification.group.dart.webdev"), NotificationDisplayType.TOOL_WINDOW, false, "Dart Webdev");
  public static final NotificationGroup DART_ANALYSIS_SERVER =
    new NotificationGroup("Dart Analysis Server", DartBundle.messageValue("notification.group.dart.analysis.server"), NotificationDisplayType.BALLOON, true);
  public static final NotificationGroup DART_PUB_TOOL =
    new NotificationGroup("Dart Pub Tool", DartBundle.messageValue("notification.group.dart.pub.tool"), NotificationDisplayType.BALLOON, true);
  public static final NotificationGroup DART_ANALYSIS_ISSUE =
    new NotificationGroup("Dart analysis issue", DartBundle.messageValue("notification.group.dart.analysis.issue"), NotificationDisplayType.NONE, true);

  private static final List<NotificationGroup> ALL =
    List.of(DART_SUPPORT, DART_ANALYSIS, DART_WEBDEV, DART_ANALYSIS_SERVER, DART_PUB_TOOL, DART_ANALYSIS_ISSUE);

  public static NotificationGroup get(String id) {
    for (NotificationGroup group : ALL) {
      if (group.getId().equals(id)) {
        return group;
      }
    }
    throw new IllegalArgumentException("Unknown notification group: " + id);
  }

  @Override
  public void contribute(Consumer<NotificationGroup> registrator) {
    ALL.forEach(registrator);
  }
}
