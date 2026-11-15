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
package consulo.dart.newProjectOrModule;

import com.jetbrains.lang.dart.projectWizard.DartModuleBuilder;
import com.jetbrains.lang.dart.projectWizard.DartProjectWizardData;
import consulo.annotation.access.RequiredReadAction;
import consulo.content.bundle.Sdk;
import consulo.module.content.layer.ContentEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.creation.scratch.NewModuleBuilderProcessor;
import consulo.ui.ex.wizard.WizardStep;
import consulo.virtualFileSystem.VirtualFile;

import java.util.function.Consumer;

public class DartNewModuleBuilderProcessor implements NewModuleBuilderProcessor<DartNewModuleContext> {
  @Override
  public DartNewModuleContext createContext(boolean isNewProject) {
    return new DartNewModuleContext(isNewProject);
  }

  @Override
  public void buildSteps(Consumer<WizardStep<DartNewModuleContext>> consumer, DartNewModuleContext context) {
    consumer.accept(new DartNewModuleSetupStep(context));
  }

  @RequiredReadAction
  @Override
  public void process(DartNewModuleContext context, ContentEntry contentEntry, ModifiableRootModel modifiableRootModel) {
    VirtualFile baseDir = contentEntry.getFile();
    Sdk sdk = context.getSdk();
    String sdkHomePath = sdk == null ? null : sdk.getHomePath();
    if (baseDir == null || sdkHomePath == null) {
      DartModuleBuilder.enableDart(modifiableRootModel, sdk);
      return;
    }
    DartModuleBuilder.setupProject(modifiableRootModel, baseDir, new DartProjectWizardData(sdkHomePath, context.getTemplate()));
  }
}
