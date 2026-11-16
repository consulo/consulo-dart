/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.customization.LspCompletionSupport;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.Image;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class DartLspCompletionSupport extends LspCompletionSupport {
  public static final DartLspCompletionSupport INSTANCE = new DartLspCompletionSupport();

  private DartLspCompletionSupport() {
  }

  @Override
  public @Nullable Image getIcon(@NotNull CompletionItem item) {
    CompletionItemKind kind = item.getKind();
    if (kind == CompletionItemKind.Constructor) {
      return PlatformIconGroup.nodesClassinitializer();
    }
    if (kind == CompletionItemKind.Function) {
      return PlatformIconGroup.nodesLambda();
    }
    return super.getIcon(item);
  }
}
