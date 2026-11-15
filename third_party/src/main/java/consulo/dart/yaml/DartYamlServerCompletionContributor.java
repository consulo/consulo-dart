package consulo.dart.yaml;

import com.jetbrains.lang.dart.ide.completion.DartServerCompletionContributor;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.Language;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLLanguage;

/**
 * Upstream dart-yaml.xml registers {@link DartServerCompletionContributor} for YAML too.
 */
@ExtensionImpl
public class DartYamlServerCompletionContributor extends DartServerCompletionContributor {
  @Override
  public @NotNull Language getLanguage() {
    return YAMLLanguage.INSTANCE;
  }
}
