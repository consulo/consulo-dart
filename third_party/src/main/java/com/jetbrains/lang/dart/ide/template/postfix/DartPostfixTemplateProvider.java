// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.template.postfix;

import consulo.annotation.component.ExtensionImpl;
import com.jetbrains.lang.dart.DartLanguage;
import consulo.language.Language;
import consulo.language.editor.postfixTemplate.PostfixTemplate;
import consulo.language.editor.postfixTemplate.PostfixTemplateProvider;
import consulo.logging.Logger;
import consulo.codeEditor.Editor;
import consulo.language.psi.PsiFile;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.logging.PluginLogger;
import org.dartlang.analysis.server.protocol.PostfixTemplateDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ExtensionImpl
public final class DartPostfixTemplateProvider extends PostfixTemplateProvider {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartPostfixTemplateProvider.class);

  private static final String UNINITIALIZED_KEY = "none";
  private static final Map<String, Set<PostfixTemplate>> TEMPLATE_CACHE = new HashMap<>();

  static {
    Set<PostfixTemplate> one = new HashSet<>();
    one.add(new DartRemotePostfixTemplate("none", ".none", "none"));
    // Prime the cache to enable postfix completion UI in Preferences before any analysis server is started.
    TEMPLATE_CACHE.put(UNINITIALIZED_KEY, one);
  }

  public static @Nullable Set<PostfixTemplate> getTemplates(String version) {
    return TEMPLATE_CACHE.get(version);
  }

  public static void initializeTemplates(final @NotNull DartAnalysisServerService service) {
    String version = service.getSdkVersion();
    if (TEMPLATE_CACHE.get(version) != null) return;

    PostfixTemplateDescriptor[] templates = service.edit_listPostfixCompletionTemplates();
    Set<PostfixTemplate> set = new HashSet<>();
    if (templates != null) {
      try {
        for (PostfixTemplateDescriptor template : templates) {
          set.add(DartRemotePostfixTemplate.createTemplate(template));
        }
        TEMPLATE_CACHE.put(version, set);
      }
      catch (Exception ex) {
        LOG.error(ex);
      }
    }
  }

  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  public @NotNull String getId() {
    return "builtin.dart";
  }

  // Consulo: PostfixTemplateProvider.getTemplates() is final and computed once (see CONSULO.md, missing APIs)
  @Override
  protected @NotNull Set<PostfixTemplate> buildTemplates() {
    // Find the largest initialized set. This may return templates that are not recognized
    // by the analysis server actually used for expansion, but at least the user will see
    // all possible templates.
    Set<PostfixTemplate> set = TEMPLATE_CACHE.get(UNINITIALIZED_KEY);
    for (Set<PostfixTemplate> cache : TEMPLATE_CACHE.values()) {
      if (set.size() < cache.size()) {
        set = cache;
      }
    }
    return set;
  }

  @Override
  public boolean isTerminalSymbol(char currentChar) {
    return currentChar == '.' || currentChar == '!';
  }

  @Override
  public void preExpand(@NotNull PsiFile file, @NotNull Editor editor) {
  }

  @Override
  public void afterExpand(@NotNull PsiFile file, @NotNull Editor editor) {
  }

  @Override
  public @NotNull PsiFile preCheck(@NotNull PsiFile copyFile, @NotNull Editor realEditor, int currentOffset) {
    // TODO(messick) See if we need to add a semicolon, like the Java implementation does.
    return copyFile;
  }
}
