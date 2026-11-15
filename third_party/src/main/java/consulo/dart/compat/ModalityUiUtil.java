// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.application.Application;
import consulo.application.ApplicationManager;
import consulo.ui.ModalityState;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

/**
 * {@code com.intellij.util.ModalityUiUtil} of IntelliJ.
 */
public final class ModalityUiUtil {
  private ModalityUiUtil() {
  }

  public static void invokeLaterIfNeeded(@NotNull ModalityState modalityState,
                                         @NotNull Runnable runnable) {
    Application app = ApplicationManager.getApplication();
    if (app.isDispatchThread()) {
      runnable.run();
    }
    else {
      app.invokeLater(runnable, modalityState);
    }
  }

  public static void invokeLaterIfNeeded(@NotNull ModalityState modalityState,
                                         @NotNull BooleanSupplier expired, @NotNull Runnable runnable) {
    Application app = ApplicationManager.getApplication();
    if (app.isDispatchThread()) {
      runnable.run();
    }
    else {
      app.invokeLater(runnable, modalityState, expired);
    }
  }
}
