// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import consulo.annotation.component.ComponentScope;
import consulo.annotation.component.ServiceImpl;
import consulo.annotation.component.ServiceAPI;
import consulo.application.progress.ProgressIndicatorProvider;
import consulo.project.Project;
import consulo.language.psi.AnyPsiChangeListener;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.util.DartClassResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentMap;

@ServiceAPI(ComponentScope.PROJECT)
@ServiceImpl
@Singleton
public final class DartClassResolveCache {
  private final ConcurrentMap<DartClass, DartClassResolveResult> myMap = ContainerUtil.createConcurrentWeakMap();

  public static DartClassResolveCache getInstance(Project project) {
    ProgressIndicatorProvider.checkCanceled(); // We hope this method is being called often enough to cancel daemon processes smoothly
    return project.getInstance(DartClassResolveCache.class);
  }

  @Inject
  public DartClassResolveCache(@NotNull Project project) {
    project.getMessageBus().connect().subscribe(AnyPsiChangeListener.class, new AnyPsiChangeListener() {
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
