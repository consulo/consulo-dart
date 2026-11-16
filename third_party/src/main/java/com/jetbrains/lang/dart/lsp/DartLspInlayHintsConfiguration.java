/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.google.gson.JsonObject;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.hints.DartParameterNamesInlayHintsProvider;
import com.jetbrains.lang.dart.hints.DartTypesInlayHintsProvider;
import consulo.language.editor.inlay.DeclarativeInlayHintsProviderFactoryUtil;
import consulo.language.editor.inlay.DeclarativeInlayHintsSettings;
import consulo.language.editor.inlay.DeclarativeInlayOptionInfo;
import consulo.language.editor.inlay.InlayProviderInfo;
import consulo.util.lang.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Translates the Dart checkboxes in Settings | Editor | Inlay Hints into the {@code dart} configuration
 * section that the Dart Analysis Server asks for via {@code workspace/configuration}.
 * <p>
 * The server groups its inlay hints into the categories below and computes only the enabled ones,
 * so a category that is switched off here is never computed at all - unlike the client-side filter
 * in {@link DartLspInlayHintSupport}, which can only drop hints after the server has produced them.
 * <p>
 * A parent checkbox that is off disables all of its categories, regardless of the state of the
 * sub-checkboxes: the sub-checkboxes stay in the settings so that their state is remembered, but
 * they only take effect while their parent is on.
 */
public final class DartLspInlayHintsConfiguration {
  public static final DartLspInlayHintsConfiguration INSTANCE = new DartLspInlayHintsConfiguration();

  static final String INLAY_HINTS_KEY = "inlayHints";
  static final String ENABLED_KEY = "enabled";

  // The names the server gives its hint categories; they are the wire format of the `dart`
  // configuration section and are spelled out once, here.
  static final String PARAMETER_NAMES_KEY = "parameterNames";
  static final String VARIABLE_TYPES_KEY = "variableTypes";
  static final String RETURN_TYPES_KEY = "returnTypes";
  static final String PARAMETER_TYPES_KEY = "parameterTypes";
  static final String TYPE_ARGUMENTS_KEY = "typeArguments";
  static final String DOT_SHORTHAND_TYPES_KEY = "dotShorthandTypes";

  private static final String PARAMETER_NAMES_NONE = "none";
  private static final String PARAMETER_NAMES_LITERAL = "literal";
  private static final String PARAMETER_NAMES_ALL = "all";

  /** The type categories of the server, paired with the option that switches them on and off. */
  private static final List<Pair<String, String>> TYPE_CATEGORIES = List.of(
    Pair.create(VARIABLE_TYPES_KEY, DartTypesInlayHintsProvider.VARIABLE_TYPES_OPTION_ID),
    Pair.create(RETURN_TYPES_KEY, DartTypesInlayHintsProvider.RETURN_TYPES_OPTION_ID),
    Pair.create(PARAMETER_TYPES_KEY, DartTypesInlayHintsProvider.PARAMETER_TYPES_OPTION_ID),
    Pair.create(TYPE_ARGUMENTS_KEY, DartTypesInlayHintsProvider.TYPE_ARGUMENTS_OPTION_ID),
    Pair.create(DOT_SHORTHAND_TYPES_KEY, DartTypesInlayHintsProvider.DOT_SHORTHAND_TYPES_OPTION_ID)
  );

  /** Every hint category the {@code inlayHints} object carries. */
  static final Set<String> SERVER_CATEGORY_KEYS;

  static {
    Set<String> keys = new LinkedHashSet<>();
    keys.add(PARAMETER_NAMES_KEY);
    for (Pair<String, String> category : TYPE_CATEGORIES) {
      keys.add(category.getFirst());
    }
    SERVER_CATEGORY_KEYS = Collections.unmodifiableSet(keys);
  }

  private DartLspInlayHintsConfiguration() {
  }

  /**
   * Builds the whole {@code dart} configuration section, i.e. {@code {"inlayHints": {...}}}.
   */
  public @NotNull JsonObject buildDartSection() {
    JsonObject inlayHints = new JsonObject();

    JsonObject parameterNames = new JsonObject();
    parameterNames.addProperty(ENABLED_KEY, parameterNamesMode());
    inlayHints.add(PARAMETER_NAMES_KEY, parameterNames);

    boolean typesEnabled = isProviderEnabled(DartTypesInlayHintsProvider.PROVIDER_ID);
    for (Pair<String, String> typeCategory : TYPE_CATEGORIES) {
      String serverKey = typeCategory.getFirst();
      String optionId = typeCategory.getSecond();
      JsonObject category = new JsonObject();
      category.addProperty(
        ENABLED_KEY,
        typesEnabled && isOptionEnabled(optionId, DartTypesInlayHintsProvider.PROVIDER_ID)
      );
      inlayHints.add(serverKey, category);
    }

    JsonObject dartSection = new JsonObject();
    dartSection.add(INLAY_HINTS_KEY, inlayHints);
    return dartSection;
  }

  private @NotNull String parameterNamesMode() {
    String providerId = DartParameterNamesInlayHintsProvider.PROVIDER_ID;
    if (!isProviderEnabled(providerId)) return PARAMETER_NAMES_NONE;
    boolean onlyLiteral = isOptionEnabled(DartParameterNamesInlayHintsProvider.ONLY_LITERAL_OPTION_ID, providerId);
    return onlyLiteral ? PARAMETER_NAMES_LITERAL : PARAMETER_NAMES_ALL;
  }

  /**
   * Returns whether the checkbox of the given provider is on, falling back to the
   * {@code isEnabledByDefault} value of its registration while the user has not toggled it yet.
   */
  public boolean isProviderEnabled(@NotNull String providerId) {
    Boolean enabled = DeclarativeInlayHintsSettings.getInstance().isProviderEnabled(providerId);
    if (enabled != null) return enabled;
    InlayProviderInfo info = providerInfo(providerId);
    return info != null && info.isEnabledByDefault();
  }

  /**
   * Returns whether the given sub-checkbox of the given provider is on, falling back to the
   * {@code enabledByDefault} value of its option registration while the user has not toggled it yet.
   */
  private boolean isOptionEnabled(@NotNull String optionId, @NotNull String providerId) {
    Boolean enabled = DeclarativeInlayHintsSettings.getInstance().isOptionEnabled(optionId, providerId);
    if (enabled != null) return enabled;
    InlayProviderInfo info = providerInfo(providerId);
    if (info == null) return false;
    for (DeclarativeInlayOptionInfo option : info.options()) {
      if (option.id().equals(optionId)) {
        return option.isEnabledByDefault();
      }
    }
    return false;
  }

  private static @Nullable InlayProviderInfo providerInfo(@NotNull String providerId) {
    // Consulo: InlayHintsProviderFactory.getProviderInfo -> DeclarativeInlayHintsProviderFactoryUtil.getProviderInfo
    return DeclarativeInlayHintsProviderFactoryUtil.getProviderInfo(DartLanguage.INSTANCE, providerId);
  }
}
