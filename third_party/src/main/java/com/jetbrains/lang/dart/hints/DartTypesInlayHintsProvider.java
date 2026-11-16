package com.jetbrains.lang.dart.hints;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.Language;
import consulo.language.editor.inlay.DeclarativeInlayHintsCollector;
import consulo.language.editor.inlay.DeclarativeInlayHintsProvider;
import consulo.language.editor.inlay.DeclarativeInlayOptionInfo;
import consulo.language.editor.inlay.InlayGroup;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Settings-only provider that anchors the "Types" checkbox for Dart in
 * Settings | Editor | Inlay Hints.
 *
 * The hints themselves are computed by the Dart Analysis Server and rendered by the LSP inlay
 * hint support, so this provider never collects anything. Its enabled state is read by
 * {@link com.jetbrains.lang.dart.lsp.DartLspInlayHintSupport} to decide whether type hints are
 * requested from the server and displayed.
 */
// Consulo: registered via @ExtensionImpl; the attributes of the upstream plugin.xml registration
// (language, providerId, group, nameKey, descriptionKey, isEnabledByDefault, options) are the overridden getters.
@ExtensionImpl
public class DartTypesInlayHintsProvider implements DeclarativeInlayHintsProvider {
  public static final String PROVIDER_ID = "dart.types";

  /** Option ids of the type category sub-checkboxes, see {@link #getOptions()}. */
  public static final String VARIABLE_TYPES_OPTION_ID = "dart.types.variable";
  public static final String RETURN_TYPES_OPTION_ID = "dart.types.return";
  public static final String PARAMETER_TYPES_OPTION_ID = "dart.types.parameter";
  public static final String TYPE_ARGUMENTS_OPTION_ID = "dart.types.type.arguments";
  public static final String DOT_SHORTHAND_TYPES_OPTION_ID = "dart.types.dot.shorthand";

  @Override
  public @Nullable DeclarativeInlayHintsCollector createCollector(@NotNull PsiFile file, @NotNull Editor editor) {
    return null;
  }

  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  @Override
  public @NotNull String getId() {
    return PROVIDER_ID;
  }

  @Override
  public @NotNull LocalizeValue getName() {
    return DartBundle.messageValue("dart.inlay.hints.types.name");
  }

  @Override
  public @NotNull LocalizeValue getDescription() {
    return DartBundle.messageValue("dart.inlay.hints.types.description");
  }

  @Override
  public @NotNull LocalizeValue getPreviewFileText() {
    return DartClosingLabelsInlayHintsProvider.loadPreviewFileText(PROVIDER_ID);
  }

  @Override
  public @NotNull InlayGroup getGroup() {
    return InlayGroup.TYPES_GROUP;
  }

  @Override
  public @NotNull Set<DeclarativeInlayOptionInfo> getOptions() {
    Set<DeclarativeInlayOptionInfo> options = new LinkedHashSet<>();
    options.add(option(VARIABLE_TYPES_OPTION_ID, "dart.inlay.hints.types.variable"));
    options.add(option(RETURN_TYPES_OPTION_ID, "dart.inlay.hints.types.return"));
    options.add(option(PARAMETER_TYPES_OPTION_ID, "dart.inlay.hints.types.parameter"));
    options.add(option(TYPE_ARGUMENTS_OPTION_ID, "dart.inlay.hints.types.type.arguments"));
    options.add(option(DOT_SHORTHAND_TYPES_OPTION_ID, "dart.inlay.hints.types.dot.shorthand"));
    return options;
  }

  @Override
  public boolean isEnabledByDefault() {
    return false;
  }

  /** All type options are {@code enabledByDefault="true"} in the upstream registration. */
  private static @NotNull DeclarativeInlayOptionInfo option(@NotNull String optionId, @NotNull String keyPrefix) {
    return new DeclarativeInlayOptionInfo(optionId,
                                          true,
                                          DartBundle.messageValue(keyPrefix + ".name"),
                                          DartBundle.messageValue(keyPrefix + ".description"));
  }
}
