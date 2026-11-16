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

import java.util.Set;

/**
 * Settings-only provider that anchors the "Parameter names" checkbox for Dart in
 * Settings | Editor | Inlay Hints.
 *
 * The hints themselves are computed by the Dart Analysis Server and rendered by the LSP inlay
 * hint support, so this provider never collects anything. Its enabled state is read by
 * {@link com.jetbrains.lang.dart.lsp.DartLspInlayHintSupport} to decide whether parameter name hints
 * are requested from the server and displayed.
 */
// Consulo: registered via @ExtensionImpl; the attributes of the upstream plugin.xml registration
// (language, providerId, group, nameKey, descriptionKey, isEnabledByDefault, options) are the overridden getters.
@ExtensionImpl
public class DartParameterNamesInlayHintsProvider implements DeclarativeInlayHintsProvider {
  public static final String PROVIDER_ID = "dart.parameter.names";

  /** Option id of the "Only for literal arguments" sub-checkbox, see {@link #getOptions()}. */
  public static final String ONLY_LITERAL_OPTION_ID = "dart.parameter.names.only.literal";

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
    return DartBundle.messageValue("dart.inlay.hints.parameter.names.name");
  }

  @Override
  public @NotNull LocalizeValue getDescription() {
    return DartBundle.messageValue("dart.inlay.hints.parameter.names.description");
  }

  @Override
  public @NotNull LocalizeValue getPreviewFileText() {
    return DartClosingLabelsInlayHintsProvider.loadPreviewFileText(PROVIDER_ID);
  }

  @Override
  public @NotNull InlayGroup getGroup() {
    return InlayGroup.PARAMETERS_GROUP;
  }

  @Override
  public @NotNull Set<DeclarativeInlayOptionInfo> getOptions() {
    return Set.of(
      new DeclarativeInlayOptionInfo(ONLY_LITERAL_OPTION_ID,
                                     false,
                                     DartBundle.messageValue("dart.inlay.hints.parameter.names.only.literal.name"),
                                     DartBundle.messageValue("dart.inlay.hints.parameter.names.only.literal.description"))
    );
  }

  @Override
  public boolean isEnabledByDefault() {
    return false;
  }
}
