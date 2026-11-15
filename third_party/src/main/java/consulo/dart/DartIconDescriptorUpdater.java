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
package consulo.dart;

import com.jetbrains.lang.dart.psi.impl.AbstractDartComponentImpl;
import com.jetbrains.lang.dart.psi.impl.DartFunctionExpressionBase;
import com.jetbrains.lang.dart.psi.impl.DartLibraryNameElementBase;
import com.jetbrains.lang.dart.psi.impl.DartNamedElementImpl;
import consulo.annotation.access.RequiredReadAction;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.icon.IconDescriptor;
import consulo.language.icon.IconDescriptorUpdater;
import consulo.language.psi.PsiElement;
import consulo.ui.image.Image;

/**
 * Consulo has no {@code PsiElement.getIcon(int)}: the upstream overrides are kept as plain methods and called from here.
 */
@ExtensionImpl
public class DartIconDescriptorUpdater implements IconDescriptorUpdater {
  @RequiredReadAction
  @Override
  public void updateIcon(IconDescriptor iconDescriptor, PsiElement element, int flags) {
    Image icon = null;
    if (element instanceof AbstractDartComponentImpl component) {
      icon = component.getIcon(flags);
    }
    else if (element instanceof DartFunctionExpressionBase functionExpression) {
      icon = functionExpression.getIcon(flags);
    }
    else if (element instanceof DartLibraryNameElementBase libraryNameElement) {
      icon = libraryNameElement.getIcon(flags);
    }
    else if (element instanceof DartNamedElementImpl namedElement) {
      icon = namedElement.getIcon(flags);
    }

    if (icon != null) {
      iconDescriptor.setMainIcon(icon);
    }
  }
}
