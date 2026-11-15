// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import consulo.application.AllIcons;
import consulo.ui.image.Image;
import consulo.language.ast.ASTNode;
import consulo.navigation.ItemPresentation;
import consulo.util.lang.StringUtil;
import consulo.language.psi.PsiElement;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.localize.LocalizeValue;
import consulo.ui.image.ImageEffects;
import consulo.language.util.IncorrectOperationException;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.util.DartPresentableUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractDartComponentImpl extends DartPsiCompositeElementImpl implements DartComponent {
  public AbstractDartComponentImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public String getName() {
    final DartComponentName name = getComponentName();
    if (name != null) {
      return name.getText();
    }
    return super.getName();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    final DartComponentName componentName = getComponentName();
    if (componentName != null) {
      componentName.setName(name);
    }
    return this;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    // getComponentName returns composite of composite WTF?
    DartComponentName name = getComponentName();
    PsiElement id = name == null ? null : name.getFirstChild();
    return id == null ? null : id.getFirstChild();
  }

  // Consulo: not an override, used by consulo.dart.DartIconDescriptorUpdater
  public Image getIcon(int flags) {
    final DartComponentType type = DartComponentType.typeOf(this);
    Image icon = type == null ? null : type.getIcon(this);

    icon = doOverlays(icon);

    return ImageEffects.appendRight(icon, isPublic() ? AllIcons.Nodes.C_public : AllIcons.Nodes.C_private);
  }

  private Image doOverlays(Image icon) {
    if (isStatic() && !isGetter() && !isSetter()) {
      icon = ImageEffects.layered(icon, AllIcons.Nodes.StaticMark);
    }
    if (isFinal()) {
      icon = AllIcons.Nodes.FinalMark;
    }
    if (isConst()) {
      //TODO: find a distinct const icon
      icon = AllIcons.Nodes.FinalMark;
    }

    return icon;
  }

  public boolean isConst() {
    return findChildByType(DartTokenTypes.CONST) != null;
  }

  @Override
  public boolean isFinal() {
    return findChildByType(DartTokenTypes.FINAL) != null;
  }

  @Override
  public boolean isOperator() {
    return findChildByType(DartTokenTypes.OPERATOR) != null;
  }

  @Override
  public boolean isStatic() {
    if (this instanceof DartVarDeclarationListPart) {
      return ((DartVarDeclarationList)getParent()).getVarAccessDeclaration().isStatic();
    }
    return findChildByType(DartTokenTypes.STATIC) != null;
  }

  @Override
  public boolean isPublic() {
    final String name = getName();
    return name != null && !name.startsWith("_");
  }

  @Override
  public boolean isConstructor() {
    return DartComponentType.typeOf(this) == DartComponentType.CONSTRUCTOR;
  }

  @Override
  public boolean isSetter() {
    return findChildByType(DartTokenTypes.SET) != null;
  }

  @Override
  public boolean isGetter() {
    return findChildByType(DartTokenTypes.GET) != null;
  }

  @Override
  public boolean isAbstract() {
    final DartComponentType componentType = DartComponentType.typeOf(this);
    return componentType == DartComponentType.CLASS &&
           findChildByType(DartTokenTypes.ABSTRACT) != null
           ||
           componentType == DartComponentType.METHOD &&
           findChildByType(DartTokenTypes.EXTERNAL) == null &&
           findChildByType(DartTokenTypes.FUNCTION_BODY) == null;
  }

  @Override
  public boolean isUnitMember() {
    return PsiTreeUtil.getParentOfType(this, DartComponent.class) == null;
  }

  @Override
  public @Nullable DartMetadata getMetadataByName(final @NotNull String name) {
    for (DartMetadata metadata : PsiTreeUtil.getChildrenOfTypeAsList(this, DartMetadata.class)) {
      if (name.equals(metadata.getReferenceExpression().getText())) {
        return metadata;
      }
    }
    return null;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Override
      public LocalizeValue getPresentableText() {
        final StringBuilder result = new StringBuilder();
        result.append(getComponentName());
        final DartComponentType type = DartComponentType.typeOf(AbstractDartComponentImpl.this);
        if ((type == DartComponentType.METHOD ||
             type == DartComponentType.FUNCTION ||
             type == DartComponentType.CONSTRUCTOR ||
             type == DartComponentType.OPERATOR) && !(isGetter() || isSetter())) {
          final String parameterList = DartPresentableUtil.getPresentableParameterList(AbstractDartComponentImpl.this);
          result.append("(").append(parameterList).append(")");
        }
        if (type == DartComponentType.METHOD ||
            type == DartComponentType.FIELD ||
            type == DartComponentType.FUNCTION ||
            type == DartComponentType.OPERATOR) {
          final DartReturnType returnType = PsiTreeUtil.getChildOfType(AbstractDartComponentImpl.this, DartReturnType.class);
          final DartType dartType = PsiTreeUtil.getChildOfType(AbstractDartComponentImpl.this, DartType.class);
          if (returnType != null) {
            result.append(" ").append(DartPresentableUtil.RIGHT_ARROW).append(" ");
            result.append(DartPresentableUtil.buildTypeText(AbstractDartComponentImpl.this, returnType, null));
          }
          else if (dartType != null) {
            result.append(" ").append(DartPresentableUtil.RIGHT_ARROW).append(" ");
            result.append(DartPresentableUtil.buildTypeText(AbstractDartComponentImpl.this, dartType, null));
          }
        }
        return LocalizeValue.of(result.toString());
      }

      private @Nullable String getComponentName() {
        String name = getName();
        if (DartComponentType.typeOf(AbstractDartComponentImpl.this) == DartComponentType.CONSTRUCTOR) {
          DartClass dartClass = PsiTreeUtil.getParentOfType(AbstractDartComponentImpl.this, DartClass.class);
          if (dartClass == null) {
            return name;
          }
          return StringUtil.isEmpty(name) ? dartClass.getName() : dartClass.getName() + "." + name;
        }
        return name;
      }

      @Override
      public String getLocationString() {
        if (!isValid()) {
          return "";
        }
        if (!(AbstractDartComponentImpl.this instanceof DartClass)) {
          final DartClass dartClass = PsiTreeUtil.getParentOfType(AbstractDartComponentImpl.this, DartClass.class);
          if (dartClass != null) {
            return dartClass.getName();
          }
        }
        DartExecutionScope root = PsiTreeUtil.getTopmostParentOfType(AbstractDartComponentImpl.this, DartExecutionScope.class);
        DartPartOfStatement partOfStatement = PsiTreeUtil.getChildOfType(root, DartPartOfStatement.class);
        return partOfStatement == null ? null : partOfStatement.getLibraryName();
      }

      @Override
      public Image getIcon(boolean open) {
        return AbstractDartComponentImpl.this.getIcon(0);
      }
    };
  }

  @Override
  public int getTextOffset() {
    final DartComponentName name = getComponentName();
    return name != null ? name.getTextOffset() : super.getTextOffset();
  }
}
