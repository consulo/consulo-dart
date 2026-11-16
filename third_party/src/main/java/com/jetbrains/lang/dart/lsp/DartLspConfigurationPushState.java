/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Decides when the server has to be told that the configuration has changed.
 * <p>
 * The state is shared between the thread that computes the inlay hints and the response reader
 * thread of the server, which answers the pull, so it is guarded by a lock.
 */
final class DartLspConfigurationPushState {

  private final Object lock = new Object();

  /** The section the server read last, or {@code null} while it has not read the settings at all. */
  private @Nullable JsonObject lastSentSection;

  /** The section a notification is on its way for, or {@code null} while none is outstanding. */
  private @Nullable JsonObject pendingSection;

  /**
   * Returns whether the server has to be notified of {@code currentSection}, and remembers that it is
   * being notified. A caller that gets {@code true} and cannot send the notification has to say so with
   * {@link #pushFailed}.
   */
  boolean beginPush(@NotNull JsonObject currentSection) {
    synchronized (lock) {
      // As long as the server has not read the settings there is nothing to push: whenever it
      // gets around to reading them, it reads the current ones.
      JsonObject sentSection = lastSentSection;
      if (sentSection == null) return false;
      if (sentSection.equals(currentSection)) return false;
      // A notification is only still outstanding while it is about these very settings; once the
      // settings have moved on, the server has to hear about the new ones.
      if (Objects.equals(pendingSection, currentSection)) return false;

      pendingSection = currentSection;
      return true;
    }
  }

  /** Takes back a {@link #beginPush} whose notification never went out. */
  void pushFailed() {
    synchronized (lock) {
      pendingSection = null;
    }
  }

  /**
   * Remembers the section that the server has just read, and returns whether the server has
   * thereby changed its mind about which hints to compute, i.e. whether the hints it computed
   * before are stale now.
   * <p>
   * Every read makes whatever notification was outstanding pointless: the server has just taken
   * the current settings, so there is nothing left for it to be told about them. Keeping the
   * notification would block the very same change from being pushed again - the user flips a
   * checkbox back and forth while the notification is on its way, the server reads the old
   * settings, and the new ones would never be sent again.
   * <p>
   * A read that changed nothing needs no new hints - that is the pull the server makes while it
   * starts up, and any pull it makes on its own afterwards.
   */
  boolean configurationSentToServer(@NotNull JsonObject section) {
    synchronized (lock) {
      JsonObject previousSection = lastSentSection;
      lastSentSection = section;
      pendingSection = null;
      return previousSection != null && !previousSection.equals(section);
    }
  }

  /** Forgets what the server knew; the next server has to read the settings again. */
  void serverStopped() {
    synchronized (lock) {
      lastSentSection = null;
      pendingSection = null;
    }
  }
}
