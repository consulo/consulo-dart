package com.jetbrains.lang.dart.ide;

import javax.annotation.Nullable;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.MemberChooserObject;
import com.intellij.codeInsight.generation.PsiElementMemberChooserObject;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartComponent;
import consulo.awt.TargetAWT;
import consulo.ide.IconDescriptorUpdaters;

/**
 * @author: Fedor.Korotkov
 */
public class DartNamedElementNode extends PsiElementMemberChooserObject implements ClassMember {
  public DartNamedElementNode(final DartComponent haxeNamedComponent) {
    super(haxeNamedComponent, buildPresentationText(haxeNamedComponent), IconDescriptorUpdaters.getIcon(haxeNamedComponent, Iconable.ICON_FLAG_VISIBILITY));
  }

  @Nullable
  private static String buildPresentationText(DartComponent haxeNamedComponent) {
    final ItemPresentation presentation = haxeNamedComponent.getPresentation();
    if (presentation == null) {
      return haxeNamedComponent.getName();
    }
    final StringBuilder result = new StringBuilder();
    if (haxeNamedComponent instanceof DartClass) {
      result.append(haxeNamedComponent.getName());
      final String location = presentation.getLocationString();
      if (location != null && !location.isEmpty()) {
        result.append(" ").append(location);
      }
    }
    else {
      result.append(presentation.getPresentableText());
    }
    return result.toString();
  }

  @Nullable
  @Override
  public MemberChooserObject getParentNodeDelegate() {
    final DartComponent result = PsiTreeUtil.getParentOfType(getPsiElement(), DartComponent.class);
    return result == null ? null : new DartNamedElementNode(result);
  }
}
