// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.folding;

import jakarta.inject.Singleton;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceImpl;
import consulo.annotation.component.ServiceAPI;
import consulo.application.ApplicationManager;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.util.xml.serializer.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

@State(name = "DartCodeFoldingSettings", storages = @Storage("editor"))
@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
@Singleton
public class DartCodeFoldingSettings implements PersistentStateComponent<DartCodeFoldingSettings> {
  private boolean myCollapseParts = true;
  private boolean myCollapseGenericParams;

  public static DartCodeFoldingSettings getInstance() {
    return ApplicationManager.getApplication().getInstance(DartCodeFoldingSettings.class);
  }

  @Override
  public DartCodeFoldingSettings getState() {
    return this;
  }

  @Override
  public void loadState(final @NotNull DartCodeFoldingSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  public boolean isCollapseGenericParameters() {
    return myCollapseGenericParams;
  }

  public void setCollapseGenericParameters(final boolean collapseGenericParams) {
    myCollapseGenericParams = collapseGenericParams;
  }

  public boolean isCollapseParts() {
    return myCollapseParts;
  }

  public void setCollapseParts(final boolean collapseParts) {
    myCollapseParts = collapseParts;
  }
}
