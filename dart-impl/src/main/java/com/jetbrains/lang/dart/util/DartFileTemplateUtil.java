package com.jetbrains.lang.dart.util;

import java.util.List;

import javax.swing.Icon;

import org.jetbrains.annotations.NotNull;
import com.intellij.icons.AllIcons;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.intellij.ide.fileTemplates.FileTemplateManager;
import com.intellij.openapi.util.Condition;
import com.intellij.util.SmartList;
import com.intellij.util.containers.ContainerUtil;
import com.jetbrains.lang.dart.DartFileType;
import consulo.ide.IconDescriptor;
import consulo.ui.image.Image;
import consulo.ui.image.ImageEffects;
import icons.DartIcons;

/**
 * @author: Fedor.Korotkov
 */
public class DartFileTemplateUtil {
  private final static String DART_TEMPLATE_PREFIX = "Dart ";

  public static List<FileTemplate> getApplicableTemplates() {
    return getApplicableTemplates(new Condition<FileTemplate>() {
      @Override
      public boolean value(FileTemplate fileTemplate) {
        return DartFileType.DEFAULT_EXTENSION.equals(fileTemplate.getExtension());
      }
    });
  }

  public static List<FileTemplate> getApplicableTemplates(Condition<FileTemplate> filter) {
    final List<FileTemplate> applicableTemplates = new SmartList<FileTemplate>();
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getInternalTemplates(), filter));
    applicableTemplates.addAll(ContainerUtil.findAll(FileTemplateManager.getInstance().getAllTemplates(), filter));
    return applicableTemplates;
  }

  public static String getTemplateShortName(String templateName) {
    if (templateName.startsWith(DART_TEMPLATE_PREFIX)) {
      return templateName.substring(DART_TEMPLATE_PREFIX.length());
    }
    return templateName;
  }

  @NotNull
  public static Image getTemplateIcon(String name) {
    name = getTemplateShortName(name);
    if ("Class".equals(name)) {
      return ImageEffects.folded(AllIcons.Nodes.Class, DartIcons.DartLang);
    }
    else if ("Interface".equals(name)) {
      return ImageEffects.folded(AllIcons.Nodes.Interface, DartIcons.DartLang);
    }
    return icons.DartIcons.Dart;
  }
}
