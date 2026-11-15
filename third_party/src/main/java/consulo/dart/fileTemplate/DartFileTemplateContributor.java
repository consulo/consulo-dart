package consulo.dart.fileTemplate;

import consulo.annotation.component.ExtensionImpl;
import consulo.fileTemplate.FileTemplateContributor;
import consulo.fileTemplate.FileTemplateRegistrator;
import org.jetbrains.annotations.NotNull;

/**
 * Upstream plugin.xml {@code internalFileTemplate} registrations.
 */
@ExtensionImpl
public class DartFileTemplateContributor implements FileTemplateContributor {
  @Override
  public void register(@NotNull FileTemplateRegistrator registrator) {
    registrator.registerInternalTemplate("Dart File");
  }
}
