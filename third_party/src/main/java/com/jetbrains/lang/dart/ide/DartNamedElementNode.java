// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide;

import consulo.language.editor.generation.ClassMember;
import consulo.language.editor.generation.MemberChooserObject;
import consulo.language.editor.generation.PsiElementMemberChooserObject;
import consulo.navigation.ItemPresentation;
import consulo.component.util.Iconable;
import consulo.language.spellchecker.editor.impl.Strings;
import consulo.language.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;

import org.jetbrains.annotations.Nullable;

public class DartNamedElementNode extends PsiElementMemberChooserObject implements ClassMember {
  public DartNamedElementNode(final DartComponent dartComponent) {
    super(dartComponent, buildPresentationText(dartComponent), dartComponent.getIcon(Iconable.ICON_FLAG_VISIBILITY));
  }

  private static @Nullable String buildPresentationText(DartComponent dartComponent) {
    final ItemPresentation presentation = dartComponent.getPresentation();
    if (presentation == null) {
      return dartComponent.getName();
    }
    if (dartComponent instanceof DartClass) {
      final String location = presentation.getLocationString();
      if (Strings.isNotEmpty(location)) {
        return dartComponent.getName() + " " + location;
      }
      return dartComponent.getName();
    }
    else {
      return presentation.getPresentableText();
    }
  }

  @Override
  public @Nullable MemberChooserObject getParentNodeDelegate() {
    final DartComponent result = PsiTreeUtil.getParentOfType(getPsiElement(), DartComponent.class);
    return result == null ? null : new DartNamedElementNode(result);
  }
}
