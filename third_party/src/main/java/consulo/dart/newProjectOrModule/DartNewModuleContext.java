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

import com.jetbrains.lang.dart.projectWizard.DartProjectTemplate;
import consulo.content.bundle.Sdk;
import consulo.module.creation.NewModuleWizardContextBase;
import org.jetbrains.annotations.Nullable;

public class DartNewModuleContext extends NewModuleWizardContextBase {
  private @Nullable Sdk mySdk;
  private @Nullable DartProjectTemplate myTemplate;

  public DartNewModuleContext(boolean isNewProject) {
    super(isNewProject);
  }

  public @Nullable Sdk getSdk() {
    return mySdk;
  }

  public void setSdk(@Nullable Sdk sdk) {
    mySdk = sdk;
  }

  /**
   * @return template to generate sample content with, {@code null} for an empty module
   */
  public @Nullable DartProjectTemplate getTemplate() {
    return myTemplate;
  }

  public void setTemplate(@Nullable DartProjectTemplate template) {
    myTemplate = template;
  }
}
