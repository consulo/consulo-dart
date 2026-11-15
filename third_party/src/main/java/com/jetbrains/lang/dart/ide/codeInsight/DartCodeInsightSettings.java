// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.codeInsight;

import jakarta.inject.Singleton;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceImpl;
import consulo.annotation.component.ServiceAPI;
import static consulo.language.editor.CodeInsightSettings.*;
import consulo.application.ApplicationManager;
import consulo.component.persist.PersistentStateComponent;
import consulo.component.persist.State;
import consulo.component.persist.Storage;
import consulo.util.xml.serializer.XmlSerializerUtil;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;



@State(name = "DartCodeInsightSettings", storages = @Storage("other.xml"))
@ServiceAPI(ComponentScope.APPLICATION)
@ServiceImpl
@Singleton
public class DartCodeInsightSettings implements PersistentStateComponent<DartCodeInsightSettings> {
  public static DartCodeInsightSettings getInstance() {
    return ApplicationManager.getApplication().getInstance(DartCodeInsightSettings.class);
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
