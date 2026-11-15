// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.analyzer;

import consulo.annotation.component.ComponentScope;
import consulo.project.Project;
import consulo.component.messagebus.MessageBus;
import consulo.annotation.component.TopicAPI;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class to send and receive Dart analysis busy messages on the project message bus.
 */
public final class DartAnalysisServerMessages {
  // Consulo: topics are listener classes annotated with @TopicAPI
  public static final Class<DartAnalysisNotifier> DART_ANALYSIS_TOPIC = DartAnalysisNotifier.class;

  @TopicAPI(ComponentScope.PROJECT)
  public interface DartAnalysisNotifier {
    void analysisStarted();

    void analysisFinished();
  }

  public static void sendAnalysisStarted(@NotNull Project project, boolean isStarting) {
    if (project.isDisposed()) return;

    final MessageBus bus = project.getMessageBus();
    final DartAnalysisNotifier publisher = bus.syncPublisher(DART_ANALYSIS_TOPIC);
    if (isStarting) {
      publisher.analysisStarted();
    }
    else {
      publisher.analysisFinished();
    }
  }
}
