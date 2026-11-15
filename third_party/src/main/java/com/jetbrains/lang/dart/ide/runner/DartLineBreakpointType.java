// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner;

import consulo.annotation.component.ExtensionImpl;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.execution.debug.breakpoint.XLineBreakpointTypeBase;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.ide.runner.base.DartDebuggerEditorsProvider;
import org.jetbrains.annotations.NotNull;

@ExtensionImpl
public final class DartLineBreakpointType extends XLineBreakpointTypeBase {

  private DartLineBreakpointType() {
    super("Dart", DartBundle.message("dart.line.breakpoints.title"), new DartDebuggerEditorsProvider());
  }

  @Override
  public boolean canPutAt(final @NotNull VirtualFile file, final int line, final @NotNull Project project) {
    return FileTypeRegistry.getInstance().isFileOfType(file, DartFileType.INSTANCE);
  }
}
