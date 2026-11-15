package consulo.dart.highlight;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.highlight.DartSyntaxHighlighter;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.highlight.SingleLazyInstanceSyntaxHighlighterFactory;
import consulo.language.editor.highlight.SyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

/**
 * Upstream plugin.xml {@code lang.syntaxHighlighter} registration of {@link DartSyntaxHighlighter}.
 */
@ExtensionImpl
public class DartSyntaxHighlighterFactory extends SingleLazyInstanceSyntaxHighlighterFactory {
  @Override
  protected @NotNull SyntaxHighlighter createHighlighter() {
    return new DartSyntaxHighlighter();
  }

  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }
}
