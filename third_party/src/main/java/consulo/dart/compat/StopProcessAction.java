/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.dart.compat;

import consulo.localize.LocalizeValue;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.process.ProcessHandler;
import consulo.process.ProcessHandlerStopper;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.ui.ex.action.DumbAwareAction;
import org.jetbrains.annotations.Nullable;

/**
 * IntelliJ's {@code StopProcessAction} with a process handler which can be replaced (upstream reruns pub in the same console);
 * Consulo's {@code consulo.ide.impl.idea.execution.actions.StopProcessAction} takes it in the constructor only.
 */
public class StopProcessAction extends DumbAwareAction implements AnActionWithSyncUpdate {
  private @Nullable ProcessHandler myProcessHandler;

  public StopProcessAction(LocalizeValue text, LocalizeValue description, @Nullable ProcessHandler processHandler) {
    super(text, description, PlatformIconGroup.actionsSuspend());
    myProcessHandler = processHandler;
  }

  public void setProcessHandler(@Nullable ProcessHandler processHandler) {
    myProcessHandler = processHandler;
  }

  @Override
  public void update(AnActionEvent e) {
    consulo.ide.impl.idea.execution.actions.StopProcessAction.update(e.getPresentation(), getTemplatePresentation(), myProcessHandler);
  }

  @Override
  @RequiredUIAccess
  public void actionPerformed(AnActionEvent e) {
    ProcessHandler processHandler = myProcessHandler;
    if (processHandler != null) {
      ProcessHandlerStopper.stop(processHandler);
    }
  }
}
