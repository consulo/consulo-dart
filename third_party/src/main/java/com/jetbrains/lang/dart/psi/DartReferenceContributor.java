// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi;

import consulo.language.Language;
import consulo.annotation.component.ExtensionImpl;
import consulo.html.language.HTMLLanguage;
import consulo.language.inject.InjectedLanguageManager;
import consulo.logging.Logger;
import com.jetbrains.lang.dart.logging.PluginLogger;
import consulo.project.Project;
import consulo.document.util.TextRange;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiReferenceContributor;
import consulo.language.psi.PsiReferenceProvider;
import consulo.language.psi.PsiReferenceRegistrar;
import consulo.xml.language.psi.XmlAttributeValue;
import consulo.xml.language.psi.XmlTokenType;
import consulo.language.util.ProcessingContext;
import consulo.application.util.function.Processor;
import consulo.util.collection.SmartList;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartTokenTypesSets;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.analyzer.DartServerData;
import com.jetbrains.lang.dart.injection.DartMultiHostInjector;
import com.jetbrains.lang.dart.resolve.DartResolver;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static consulo.language.pattern.PlatformPatterns.psiElement;
import static consulo.language.pattern.PlatformPatterns.psiFile;
import static consulo.language.pattern.StandardPatterns.or;

@ExtensionImpl
class DartReferenceContributor extends PsiReferenceContributor {
  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartReferenceContributor.class);

  @Override
  public void registerReferenceProviders(final @NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(
      or(psiElement(DartTokenTypesSets.MULTI_LINE_DOC_COMMENT),
         psiElement(DartTokenTypesSets.SINGLE_LINE_DOC_COMMENT),
         psiElement(DartStringLiteralExpression.class),
         psiElement(XmlAttributeValue.class).inFile(psiFile().withLanguage(HTMLLanguage.INSTANCE)),
         psiElement(XmlTokenType.XML_DATA_CHARACTERS).inFile(psiFile().withLanguage(HTMLLanguage.INSTANCE))
      ),
      new DartReferenceProvider());
  }

  private static class DartReferenceProvider extends PsiReferenceProvider {
    @Override
    public PsiReference @NotNull [] getReferencesByElement(final @NotNull PsiElement element, final @NotNull ProcessingContext context) {
      if (element.getText().contains(DartMultiHostInjector.STRING_TEMPLATE_PLACEHOLDER)) {
        return PsiReference.EMPTY_ARRAY;
      }

      final Project project = element.getProject();
      if (DartAnalysisServerService.isLspNavigationEnabled(project)) return PsiReference.EMPTY_ARRAY;

      final VirtualFile file = DartResolveUtil.getRealVirtualFile(InjectedLanguageManager.getInstance(project).getTopLevelFile(element));
      if (!DartAnalysisServerService.isLocalAnalyzableFile(file)) return PsiReference.EMPTY_ARRAY;

      final List<PsiReference> result = new SmartList<>();
      final TextRange elementRangeInHost = InjectedLanguageManager.getInstance(project).injectedToHost(element, element.getTextRange());

      final Processor<DartServerData.DartNavigationRegion> processor = navigationRegion -> {
        int navRegStartOffset = navigationRegion.getOffset();
        int navRegEndOffset = navRegStartOffset + navigationRegion.getLength();
        if (navRegStartOffset < elementRangeInHost.getStartOffset() || navRegEndOffset > elementRangeInHost.getEndOffset()) {
          LOG.error(element.getClass().getSimpleName() + " [" + element.getText() + "], file:" + element.getContainingFile().getName() +
                    ", elementRange=" + element.getTextRange() + ", elementRangeInHost=" + elementRangeInHost +
                    ", navigationRange=(" + navRegStartOffset + "," + navRegEndOffset + ")");
          return true;
        }

        result.add(new DartContributedReference(element, elementRangeInHost.getStartOffset(), navigationRegion));
        return true;
      };

      DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
      DartResolver.processRegionsInRange(das.getNavigation(file), elementRangeInHost, processor);

      return result.toArray(PsiReference.EMPTY_ARRAY);
    }
  }
}
