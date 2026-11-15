// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.pubServer;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.actions.DartPubActionBase;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.sdk.DartSdk;
import consulo.application.ApplicationManager;
import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandlerBuilder;
import consulo.process.cmd.GeneralCommandLine;
import consulo.process.util.CapturingProcessRunner;
import consulo.process.util.ProcessOutput;
import consulo.project.Project;
import org.jetbrains.annotations.NotNull;

public final class DartWebdev {
  public static final DartWebdev INSTANCE = new DartWebdev();

  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartWebdev.class);

  private boolean activated = false;

  private DartWebdev() {
  }

  public boolean getActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  /**
   * @return {@code false} only if explicitly cancelled by user
   */
  public boolean ensureWebdevActivated(@NotNull Project project) {
    ApplicationManager.getApplication().assertIsDispatchThread();

    if (activated) return true;

    final DartSdk sdk = DartSdk.getDartSdk(project);
    if (sdk == null) return false; // should't happen, checked before

    try {
      final ProcessOutput processOutput = ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {
        final ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
        indicator.setIndeterminate(true);
        indicator.setText2("pub global activate webdev");

        final GeneralCommandLine command = new GeneralCommandLine();
        command.setRedirectErrorStream(true);
        DartPubActionBase.setupPubExePath(command, sdk);
        command.addParameters("global", "activate", "webdev");
        command.withEnvironment(DartPubActionBase.PUB_ENV_VAR_NAME, DartPubActionBase.getPubEnvValue() + ".webdev.activate");

        return new CapturingProcessRunner(ProcessHandlerBuilder.create(command).build()).runProcess(indicator, 60 * 1000, false);
      }, DartBundle.messageValue("progress.title.activating.package.webdev"), true, project);

      if (processOutput.isCancelled()) return false;
      if (processOutput.isTimeout()) {
        // It could be already activated so we want to give a chance and therefore return true here.
        LOG.warn("Webdev activation timed out. Output: " + processOutput.getStdout());
        return true;
      }
    }
    catch (ExecutionException e) {
      // It could be already activated so we want to give a chance and therefore return true here.
      LOG.warn("Failed to activate webdev: " + e.getMessage());
      return true;
    }

    activated = true;
    return true;
  }
}
