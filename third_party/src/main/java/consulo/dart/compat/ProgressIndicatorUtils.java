// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.application.progress.ProgressIndicator;
import consulo.application.progress.ProgressManager;
import consulo.component.ProcessCanceledException;
import consulo.logging.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * {@code ProgressIndicatorUtils} methods of IntelliJ; the Consulo class is internal.
 */
public final class ProgressIndicatorUtils {
  private static final Logger LOG = Logger.getInstance(ProgressIndicatorUtils.class);

  private ProgressIndicatorUtils() {
  }

  public static void awaitWithCheckCanceled(@NotNull CountDownLatch waiter) {
    ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
    boolean success = false;
    while (!success) {
      checkCancelledEvenWithPCEDisabled(indicator);
      try {
        success = waiter.await(10, TimeUnit.MILLISECONDS);
      }
      catch (Exception e) {
        //noinspection InstanceofCatchParameter
        if (!(e instanceof InterruptedException)) {
          LOG.warn(e);
        }
        throw new ProcessCanceledException(e);
      }
    }
  }

  /**
   * Use when otherwise a deadlock is possible.
   */
  public static void checkCancelledEvenWithPCEDisabled(@Nullable ProgressIndicator indicator) {
    if (indicator != null && indicator.isCanceled()) {
      indicator.checkCanceled(); // maybe it'll throw with some useful additional information
      throw new ProcessCanceledException();
    }
  }
}
