// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.findUsages;

import com.google.dart.server.utilities.general.ObjectUtilities;
import consulo.language.editor.util.NavigationItemFileStatus;
import consulo.language.editor.CommonDataKeys;
import consulo.dataContext.DataSink;
import consulo.dataContext.UiDataProvider;
import consulo.component.util.Iconable;

import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.status.FileStatus;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.SmartPointerManager;
import consulo.language.psi.SmartPsiElementPointer;
import consulo.usage.UsageInfo;
import consulo.usage.UsageGroup;
import consulo.usage.UsageView;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartComponentName;
import consulo.language.icon.IconDescriptorUpdaters;
import consulo.ui.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


class DartComponentUsageGroup implements UsageGroup, UiDataProvider {
  private final VirtualFile myFile;
  private final SmartPsiElementPointer<DartComponent> myElementPointer;
  private final String myText;
  private final Image myIcon;

  DartComponentUsageGroup(@NotNull DartComponent element) {
    myFile = element.getContainingFile().getVirtualFile();
    myText = StringUtil.notNullize(element.getName());
    myElementPointer = SmartPointerManager.getInstance(element.getProject()).createSmartPsiElementPointer(element);
    myIcon = IconDescriptorUpdaters.getIcon(element, Iconable.ICON_FLAG_VISIBILITY | Iconable.ICON_FLAG_READ_STATUS);
  }

  @Override
  public boolean canNavigate() {
    return isValid();
  }

  @Override
  public boolean canNavigateToSource() {
    return canNavigate();
  }

  @Override
  public int compareTo(@NotNull UsageGroup usageGroup) {
    return getText(null).compareToIgnoreCase(usageGroup.getText(null));
  }

  @Override
  public boolean equals(Object object) {
    if (object instanceof DartComponentUsageGroup other) {
      return ObjectUtilities.equals(other.myFile, myFile) && ObjectUtilities.equals(other.myText, myText);
    }
    return false;
  }

  @Override
  public void uiDataSnapshot(@NotNull DataSink sink) {
    sink.lazy(CommonDataKeys.PSI_ELEMENT, this::getNameElement);
    sink.lazy(UsageView.USAGE_INFO_KEY, () -> {
      final DartComponentName nameElement = getNameElement();
      return nameElement != null ? new UsageInfo(nameElement) : null;
    });
  }

  @Override
  public FileStatus getFileStatus() {
    return isValid() ? NavigationItemFileStatus.get(getComponentElement()) : null;
  }

  @Override
  public Image getIcon() {
    return myIcon;
  }

  public @NotNull String getPresentableGroupText() {
    return myText;
  }

  // Consulo: UsageGroup has getText(UsageView) and update() instead of getPresentableGroupText()
  @Override
  public @NotNull String getText(@Nullable UsageView view) {
    return getPresentableGroupText();
  }

  @Override
  public void update() {
  }

  @Override
  public int hashCode() {
    return myText.hashCode();
  }

  @Override
  public boolean isValid() {
    DartComponent componentElement = getComponentElement();
    return componentElement != null && componentElement.isValid();
  }

  @Override
  public void navigate(boolean focus) throws UnsupportedOperationException {
    final DartComponentName nameElement = getNameElement();
    if (nameElement != null && nameElement.isValid()) {
      nameElement.navigate(focus);
    }
  }

  private DartComponent getComponentElement() {
    return myElementPointer.getElement();
  }

  private DartComponentName getNameElement() {
    final DartComponent componentElement = getComponentElement();
    return componentElement != null ? componentElement.getComponentName() : null;
  }
}
