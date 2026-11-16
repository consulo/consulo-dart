/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.customization.LspInlayHintSupport;
import com.jetbrains.lang.dart.hints.DartParameterNamesInlayHintsProvider;
import com.jetbrains.lang.dart.hints.DartTypesInlayHintsProvider;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import org.eclipse.lsp4j.InlayHint;
import org.eclipse.lsp4j.InlayHintKind;
import org.jetbrains.annotations.NotNull;

/**
 * Connects the inlay hints computed by the Dart Analysis Server with the Dart checkboxes in
 * Settings | Editor | Inlay Hints.
 * <p>
 * The server marks every hint it returns with an {@link InlayHintKind}: parameter name hints use
 * {@link InlayHintKind#Parameter}, and all type hints use {@link InlayHintKind#Type}. That allows filtering
 * the hints on the client without sending any configuration to the server:
 * {@link #shouldDisplayInlayHint} drops hints whose checkbox is off, and {@link #shouldAskServerForInlayHints}
 * skips the {@code textDocument/inlayHint} request entirely while both checkboxes are off.
 */
public class DartLspInlayHintSupport extends LspInlayHintSupport {
  private final @NotNull Project project;

  public DartLspInlayHintSupport(@NotNull Project project) {
    this.project = project;
  }

  @Override
  public boolean shouldAskServerForInlayHints(@NotNull VirtualFile file) {
    // This runs before every inlay hint request cycle, and nothing else notices that the user
    // has flipped a checkbox, so it is where the server learns about it.
    DartLspConfigurationSync.getInstance(project).pushConfigurationIfChanged();

    return isProviderEnabled(DartParameterNamesInlayHintsProvider.PROVIDER_ID) ||
           isProviderEnabled(DartTypesInlayHintsProvider.PROVIDER_ID);
  }

  @Override
  public boolean shouldDisplayInlayHint(@NotNull VirtualFile file, @NotNull InlayHint inlayHint) {
    InlayHintKind kind = inlayHint.getKind();
    if (kind == null) {
      // The Dart Analysis Server sets a kind on every hint. A hint without a kind cannot be
      // attributed to either checkbox, so show it as long as hints were requested at all.
      return true;
    }
    return switch (kind) {
      case Parameter -> isProviderEnabled(DartParameterNamesInlayHintsProvider.PROVIDER_ID);
      case Type -> isProviderEnabled(DartTypesInlayHintsProvider.PROVIDER_ID);
    };
  }

  // Shared with DartLspInlayHintsConfiguration, which reads the same checkboxes to build the
  // configuration section for the server.
  private static boolean isProviderEnabled(@NotNull String providerId) {
    return DartLspInlayHintsConfiguration.INSTANCE.isProviderEnabled(providerId);
  }
}
