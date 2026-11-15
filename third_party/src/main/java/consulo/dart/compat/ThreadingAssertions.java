// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.application.Application;

/**
 * Subset of {@code com.intellij.util.concurrency.ThreadingAssertions} of IntelliJ, backed by {@link Application} checks.
 */
public final class ThreadingAssertions {
  private ThreadingAssertions() {
  }

  public static void assertEventDispatchThread() {
    Application.get().assertIsDispatchThread();
  }

  public static void assertBackgroundThread() {
    if (Application.get().isDispatchThread()) {
      throw new IllegalStateException("Access from the UI thread is not allowed");
    }
  }

  public static void assertReadAccess() {
    Application.get().assertReadAccessAllowed();
  }

  public static void assertNoReadAccess() {
    if (Application.get().isReadAccessAllowed()) {
      throw new IllegalStateException("Must not be executed under read action");
    }
  }

  public static void assertWriteAccess() {
    Application.get().assertWriteAccessAllowed();
  }
}
