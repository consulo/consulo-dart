package consulo.dart.compat;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.jetbrains.annotations.NotNull;

/**
 * Replaces IntelliJ's {@code com.intellij.markdown.utils.MarkdownToHtmlConverterKt} (bundled intellij-markdown) with flexmark.
 */
public final class MarkdownToHtmlConverterKt {
  private MarkdownToHtmlConverterKt() {
  }

  public static @NotNull String convertMarkdownToHtml(@NotNull String markdownText) {
    return HtmlRenderer.builder().build().render(Parser.builder().build().parse(markdownText));
  }
}
