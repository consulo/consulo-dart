// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.codeInsight;

import consulo.application.ApplicationManager;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.util.xml.serializer.XmlSerializerUtil;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;



@State(name = "DartCodeInsightSettings", storages = @Storage("other.xml"))
public class DartCodeInsightSettings implements PersistentStateComponent<DartCodeInsightSettings> {
  public static DartCodeInsightSettings getInstance() {
    return ApplicationManager.getApplication().getService(DartCodeInsightSettings.class);
  }

  public boolean INSERT_DEFAULT_ARG_VALUES = true;

  @MagicConstant(intValues = {YES, NO, ASK})
  public int ADD_IMPORTS_ON_PASTE = ASK;

  @Override
  public DartCodeInsightSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull DartCodeInsightSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }
}
