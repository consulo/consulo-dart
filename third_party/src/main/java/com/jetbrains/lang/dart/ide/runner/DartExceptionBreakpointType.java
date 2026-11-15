// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.runner;

import consulo.execution.debug.icon.ExecutionDebugIconGroup;
import consulo.annotation.component.ExtensionImpl;
import consulo.application.AllIcons;
import consulo.project.Project;
import consulo.ui.ex.awt.IdeBorderFactory;
import consulo.ui.ex.awt.JBRadioButton;
import consulo.execution.debug.breakpoint.XBreakpoint;
import consulo.execution.debug.event.XBreakpointListener;
import consulo.execution.debug.breakpoint.XBreakpointType;
import consulo.execution.debug.breakpoint.ui.XBreakpointCustomPropertiesPanel;
import com.jetbrains.lang.dart.DartBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

@ExtensionImpl
public final class DartExceptionBreakpointType
  extends XBreakpointType<XBreakpoint<DartExceptionBreakpointProperties>, DartExceptionBreakpointProperties> {

  public DartExceptionBreakpointType() {
    super("dart-exception", DartBundle.message("breakpoint.type.title.dart.exception.breakpoint"));
  }

  @Override
  public @NotNull Icon getEnabledIcon() {
    return ExecutionDebugIconGroup.breakpointBreakpointexception();
  }

  @Override
  public @NotNull Icon getDisabledIcon() {
    return ExecutionDebugIconGroup.breakpointBreakpointexceptiondisabled();
  }

  @Override
  public DartExceptionBreakpointProperties createProperties() {
    return new DartExceptionBreakpointProperties();
  }

  @Override
  public String getDisplayText(XBreakpoint<DartExceptionBreakpointProperties> breakpoint) {
    return DartBundle.message("break.on.exceptions");
  }

  @Override
  public XBreakpoint<DartExceptionBreakpointProperties> createDefaultBreakpoint(@NotNull XBreakpointCreator<DartExceptionBreakpointProperties> creator) {
    final XBreakpoint<DartExceptionBreakpointProperties> breakpoint = creator.createBreakpoint(new DartExceptionBreakpointProperties());
    breakpoint.setEnabled(true);
    return breakpoint;
  }

  @Override
  public @Nullable XBreakpointCustomPropertiesPanel<XBreakpoint<DartExceptionBreakpointProperties>> createCustomPropertiesPanel(final @NotNull Project project) {
    return new DartExceptionBreakpointPropertiesPanel(project);
  }

  private static class DartExceptionBreakpointPropertiesPanel
    extends XBreakpointCustomPropertiesPanel<XBreakpoint<DartExceptionBreakpointProperties>> {

    private final Project myProject;
    private JBRadioButton myBreakOnUncaughtExceptions;
    private JBRadioButton myBreakOnAllExceptions;

    DartExceptionBreakpointPropertiesPanel(Project project) {
      myProject = project;
    }

    @Override
    public @NotNull JComponent getComponent() {
      myBreakOnUncaughtExceptions = new JBRadioButton(DartBundle.message("radio.text.break.on.uncaught.exceptions"));
      myBreakOnAllExceptions = new JBRadioButton(DartBundle.message("radio.text.break.on.all.exceptions"));

      final ButtonGroup group = new ButtonGroup();
      group.add(myBreakOnUncaughtExceptions);
      group.add(myBreakOnAllExceptions);

      final JPanel panel = new JPanel(new BorderLayout());
      panel.add(myBreakOnUncaughtExceptions, BorderLayout.NORTH);
      panel.add(myBreakOnAllExceptions, BorderLayout.SOUTH);
      panel.setBorder(IdeBorderFactory.createTitledBorder(DartBundle.message("border.breaking.policy")));

      return panel;
    }

    @Override
    public void saveTo(final @NotNull XBreakpoint<DartExceptionBreakpointProperties> breakpoint) {
      final boolean oldValue = breakpoint.getProperties().isBreakOnAllExceptions();
      final boolean newValue = myBreakOnAllExceptions.isSelected();
      if (oldValue != newValue) {
        breakpoint.getProperties().setBreakOnAllExceptions(newValue);
        @SuppressWarnings("unchecked")
        final XBreakpointListener<XBreakpoint<DartExceptionBreakpointProperties>> publisher = myProject.getMessageBus().syncPublisher(XBreakpointListener.TOPIC);
        publisher.breakpointChanged(breakpoint);
      }
    }

    @Override
    public void loadFrom(final @NotNull XBreakpoint<DartExceptionBreakpointProperties> breakpoint) {
      if (breakpoint.getProperties().isBreakOnAllExceptions()) {
        myBreakOnAllExceptions.setSelected(true);
      }
      else {
        myBreakOnUncaughtExceptions.setSelected(true);
      }
    }
  }
}
