package consulo.dart.annotator;

import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.ide.annotator.DartAnnotator;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import consulo.language.editor.annotation.Annotator;
import consulo.language.editor.annotation.AnnotatorFactory;
import consulo.xml.language.XMLLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLLanguage;

/**
 * Upstream plugin.xml / dart-yaml.xml {@code <annotator>} registrations of {@link DartAnnotator}.
 */
public final class DartAnnotatorFactories {
  private DartAnnotatorFactories() {
  }

  private abstract static class Base implements AnnotatorFactory {
    @Override
    public @Nullable Annotator createAnnotator() {
      return new DartAnnotator();
    }
  }

  @ExtensionImpl
  public static final class Dart extends Base {
    @Override
    public @NotNull Language getLanguage() {
      return DartLanguage.INSTANCE;
    }
  }

  @ExtensionImpl
  public static final class Xml extends Base {
    @Override
    public @NotNull Language getLanguage() {
      return XMLLanguage.INSTANCE;
    }
  }

  @ExtensionImpl
  public static final class Yaml extends Base {
    @Override
    public @NotNull Language getLanguage() {
      return YAMLLanguage.INSTANCE;
    }
  }
}
