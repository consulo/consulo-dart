// This is a generated file. Not intended for manual editing.
package com.jetbrains.lang.dart.psi;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DartClassDefinition extends DartClass {

  @Nullable
  DartClassBody getClassBody();

  @NotNull
  DartComponentName getComponentName();

  @Nullable
  DartInterfaces getInterfaces();

  @NotNull
  List<DartMetadata> getMetadataList();

  @Nullable
  DartMixinApplication getMixinApplication();

  @Nullable
  DartMixins getMixins();

  @Nullable
  DartStringLiteralExpression getStringLiteralExpression();

  @Nullable
  DartSuperclass getSuperclass();

  @Nullable
  DartTypeParameters getTypeParameters();

}
