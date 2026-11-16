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

import com.jetbrains.lang.dart.DartBundle;
import consulo.annotation.component.ExtensionImpl;
import consulo.dart.localize.DartConsuloLocalize;
import consulo.module.creation.scratch.NewModuleBuilder;
import consulo.module.creation.scratch.NewModuleContext;
import consulo.module.creation.scratch.NewModuleContextGroup;
import icons.DartIcons;

/**
 * Consulo replacement of upstream {@code DartModuleBuilder} / {@code DartProjectGenerator} wizard UI.
 */
@ExtensionImpl(id = "dart")
public class DartNewModuleBuilder implements NewModuleBuilder {
  @Override
  public void setupContext(NewModuleContext context) {
    NewModuleContextGroup group = context.addGroup("dart", DartBundle.messageValue("dart.title"));
    group.add(DartConsuloLocalize.newModuleDartProject(), DartIcons.Dart_16, new DartNewModuleBuilderProcessor());
  }
}
