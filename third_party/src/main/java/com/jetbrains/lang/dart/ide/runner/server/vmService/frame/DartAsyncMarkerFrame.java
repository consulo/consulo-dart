// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.runner.server.vmService.frame;

import consulo.application.AllIcons;
import consulo.ui.ex.ColoredTextContainer;
import consulo.ui.ex.SimpleTextAttributes;
import consulo.execution.debug.frame.XStackFrame;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;

/**
 * A XStackFrame used to render a separator between two sets of async stack frames.
 */
public class DartAsyncMarkerFrame extends XStackFrame {
  public DartAsyncMarkerFrame() {
  }

  @Override
  public void customizePresentation(@NotNull ColoredTextContainer component) {
    component.append(DartBundle.message("debugger.asynchronous.gap.frame"), SimpleTextAttributes.EXCLUDED_ATTRIBUTES);
    component.setIcon(AllIcons.General.SeparatorH);
  }
}
