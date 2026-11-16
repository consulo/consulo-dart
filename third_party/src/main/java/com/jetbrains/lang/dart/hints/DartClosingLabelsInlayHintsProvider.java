package com.jetbrains.lang.dart.hints;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.lsp.DartLspClosingLabel;
import com.jetbrains.lang.dart.lsp.DartLspClosingLabelsService;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.Language;
import consulo.language.editor.inlay.DeclarativeInlayHintsCollector;
import consulo.language.editor.inlay.DeclarativeInlayHintsProvider;
import consulo.language.editor.inlay.DeclarativeInlayPosition;
import consulo.language.editor.inlay.DeclarativeInlayTreeSink;
import consulo.language.editor.inlay.HintFormat;
import consulo.language.editor.inlay.InlayGroup;
import consulo.language.psi.PsiFile;
import consulo.localize.LocalizeValue;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import org.eclipse.lsp4j.Range;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

// Consulo: registered via @ExtensionImpl; the attributes of the upstream plugin.xml registration
// (language, providerId, group, nameKey, descriptionKey, isEnabledByDefault) are the overridden getters.
@ExtensionImpl
public class DartClosingLabelsInlayHintsProvider implements DeclarativeInlayHintsProvider {
  public static final String PROVIDER_ID = "dart.closing.labels";

  @Override
  public @Nullable DeclarativeInlayHintsCollector createCollector(@NotNull PsiFile file, @NotNull Editor editor) {
    return new DeclarativeInlayHintsCollector.OwnBypassCollector() {
      @Override
      public void collectHintsForFile(@NotNull PsiFile file, @NotNull DeclarativeInlayTreeSink sink) {
        VirtualFile virtualFile = file.getVirtualFile();
        if (virtualFile == null) return;
        Project project = file.getProject();

        List<DartLspClosingLabel> lspClosingLabels = DartLspClosingLabelsService.getInstance(project).getClosingLabels(virtualFile);
        for (DartLspClosingLabel item : lspClosingLabels) {
          String label = item.getLabel();
          if (label == null) continue;
          Range range = item.getRange();
          if (range == null || range.getEnd() == null) continue;
          int line = range.getEnd().getLine();
          if (line >= 0 && line < editor.getDocument().getLineCount()) {
            sink.addPresentation(new DeclarativeInlayPosition.EndOfLinePosition(line), HintFormat.DEFAULT, builder -> builder.text(label));
          }
        }
      }
    };
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
    return DartBundle.messageValue("dart.inlay.hints.closing.labels.name");
  }

  @Override
  public @NotNull LocalizeValue getDescription() {
    return DartBundle.messageValue("dart.inlay.hints.closing.labels.description");
  }

  @Override
  public @NotNull LocalizeValue getPreviewFileText() {
    return loadPreviewFileText(PROVIDER_ID);
  }

  @Override
  public @NotNull InlayGroup getGroup() {
    return InlayGroup.OTHER_GROUP;
  }

  @Override
  public boolean isEnabledByDefault() {
    return true;
  }

  /**
   * Consulo: IntelliJ picks the preview up from {@code inlayProviders/<providerId>/preview.dart} by itself,
   * Consulo asks the provider for the text.
   */
  static @NotNull LocalizeValue loadPreviewFileText(@NotNull String providerId) {
    try (InputStream stream = DartClosingLabelsInlayHintsProvider.class.getResourceAsStream("/inlayProviders/" + providerId + "/preview.dart")) {
      if (stream == null) return LocalizeValue.empty();
      return LocalizeValue.of(StringUtil.convertLineSeparators(new String(stream.readAllBytes(), StandardCharsets.UTF_8)));
    }
    catch (IOException e) {
      return LocalizeValue.empty();
    }
  }
}
