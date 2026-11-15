// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi;

import com.thaiopensource.util.Service;
import consulo.application.progress.ProgressIndicatorProvider;
import consulo.project.Project;
import consulo.language.psi.AnyPsiChangeListener;
import consulo.language.impl.internal.psi.PsiManagerImpl;
import consulo.util.collection.Maps;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentMap;

@Service(Service.Level.PROJECT)
public final class DartClassResolveCache {
  private final ConcurrentMap<DartClass, DartClassResolveResult> myMap = CollectionFactory.createConcurrentWeakMap();

  public static DartClassResolveCache getInstance(Project project) {
    ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly
    return project.getService(DartClassResolveCache.class);
  }

  public DartClassResolveCache(@NotNull Project project) {
    project.getMessageBus().connect().subscribe(PsiManagerImpl.ANY_PSI_CHANGE_TOPIC, new AnyPsiChangeListener() {
      @Override
      public void beforePsiChanged(boolean isPhysical) {
        myMap.clear();
      }
    });
  }

  public void put(@NotNull DartClass dartClass, @NotNull DartClassResolveResult result) {
    myMap.put(dartClass, result);
  }

  public @Nullable DartClassResolveResult get(@NotNull DartClass dartClass) {
    return myMap.get(dartClass);
  }
}
