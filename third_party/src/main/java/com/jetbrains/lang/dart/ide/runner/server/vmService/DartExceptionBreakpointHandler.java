// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server.vmService;

import consulo.project.Project;
import consulo.execution.debug.XDebugSession;
import consulo.execution.debug.XDebuggerManager;
import consulo.execution.debug.breakpoint.XBreakpoint;
import consulo.execution.debug.breakpoint.XBreakpointHandler;
import consulo.execution.debug.XBreakpointManager;
import consulo.execution.debug.breakpoint.XBreakpointType;
import com.jetbrains.lang.dart.ide.runner.DartExceptionBreakpointProperties;
import com.jetbrains.lang.dart.ide.runner.DartExceptionBreakpointType;
import com.jetbrains.lang.dart.ide.runner.server.vmService.vmServiceDrivers.service.element.ExceptionPauseMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class DartExceptionBreakpointHandler extends XBreakpointHandler<XBreakpoint<DartExceptionBreakpointProperties>> {

  private final DartVmServiceDebugProcess myDebugProcess;

  public DartExceptionBreakpointHandler(final @NotNull DartVmServiceDebugProcess debugProcess) {
    super(DartExceptionBreakpointType.class);
    myDebugProcess = debugProcess;
  }

  public static @NotNull XBreakpoint<DartExceptionBreakpointProperties> getDefaultExceptionBreakpoint(final @NotNull Project project) {
    final XBreakpointManager bpManager = XDebuggerManager.getInstance(project).getBreakpointManager();
    final DartExceptionBreakpointType bpType = XBreakpointType.EXTENSION_POINT_NAME.findExtension(DartExceptionBreakpointType.class);
    assert bpType != null;
    // Consulo: a single default breakpoint per type
    final XBreakpoint<DartExceptionBreakpointProperties> breakpoint = bpManager.getDefaultBreakpoint(bpType);
    assert breakpoint != null;
    return breakpoint;
  }

  public static @NotNull ExceptionPauseMode getBreakOnExceptionMode(@NotNull XDebugSession session,
                                                                    final @Nullable XBreakpoint<DartExceptionBreakpointProperties> bp) {
    if (session.areBreakpointsMuted()) {
      return ExceptionPauseMode.None;
    }
    if (bp == null) return ExceptionPauseMode.Unhandled; // Default to breaking on unhandled exceptions.
    if (!bp.isEnabled()) return ExceptionPauseMode.None;
    return bp.getProperties().isBreakOnAllExceptions() ? ExceptionPauseMode.All : ExceptionPauseMode.Unhandled;
  }

  @Override
  public void registerBreakpoint(final @NotNull XBreakpoint<DartExceptionBreakpointProperties> breakpoint) {
    final VmServiceWrapper vmServiceWrapper = myDebugProcess.getVmServiceWrapper();
    if (vmServiceWrapper != null) {
      vmServiceWrapper.setExceptionPauseMode(getBreakOnExceptionMode(myDebugProcess.getSession(), breakpoint));
    }
  }

  @Override
  public void unregisterBreakpoint(final @NotNull XBreakpoint<DartExceptionBreakpointProperties> breakpoint, final boolean temporary) {
    registerBreakpoint(breakpoint);
  }
}
