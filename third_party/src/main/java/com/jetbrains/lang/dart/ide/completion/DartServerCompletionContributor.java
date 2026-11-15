// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.completion;

import consulo.annotation.component.ExtensionImpl;
import consulo.ui.image.Image;
import consulo.application.CommonBundle;
import consulo.language.editor.AutoPopupController;
import consulo.language.editor.CodeInsightSettings;
import consulo.language.editor.completion.CompletionContributor;
import consulo.language.editor.completion.CompletionInitializationContext;
import consulo.language.editor.completion.CompletionParameters;
import consulo.language.editor.completion.CompletionProvider;
import consulo.language.editor.completion.CompletionResultSet;
import consulo.language.editor.completion.CompletionSorter;
import consulo.language.editor.completion.CompletionType;
import consulo.language.editor.completion.lookup.InsertHandler;
import consulo.language.editor.completion.lookup.InsertionContext;
import consulo.application.util.matcher.PrefixMatcher;
import consulo.language.editor.completion.lookup.ParenthesesInsertHandler;
import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.completion.lookup.LookupElementWeigher;
import consulo.language.editor.template.TemplateBuilder;
import consulo.language.editor.template.TemplateBuilderFactory;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.ui.image.ImageEffects;
import consulo.language.editor.template.TextExpression;
import consulo.application.AllIcons;
import consulo.language.file.inject.VirtualFileWindow;
import consulo.language.Language;
import consulo.html.language.HTMLLanguage;
import consulo.language.inject.InjectedLanguageManager;
import consulo.xml.language.XMLLanguage;
import consulo.ui.ex.action.IdeActions;
import consulo.logging.Logger;
import consulo.document.Document;
import consulo.codeEditor.Editor;
import consulo.document.FileDocumentManager;
import consulo.ui.ex.keymap.util.KeymapUtil;
import consulo.component.ProcessCanceledException;
import consulo.project.Project;
import consulo.util.lang.Pair;
import consulo.document.util.TextRange;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiDocumentManager;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiErrorElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiReference;
import consulo.language.psi.PsiMultiReference;
import consulo.language.editor.refactoring.util.CommonRefactoringUtil;
import consulo.language.util.ProcessingContext;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.assists.AssistUtils;
import com.jetbrains.lang.dart.assists.DartSourceEditException;
import com.jetbrains.lang.dart.ide.codeInsight.DartCodeInsightSettings;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.psi.*;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.apache.commons.lang3.StringUtils;
import org.dartlang.analysis.server.protocol.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.google.common.annotations.VisibleForTesting;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static consulo.language.pattern.PlatformPatterns.psiElement;
import static consulo.language.pattern.PlatformPatterns.psiFile;
import static consulo.language.pattern.StandardPatterns.or;

@ExtensionImpl
public class DartServerCompletionContributor extends CompletionContributor { // Consulo: not final, see consulo.dart.yaml.DartYamlServerCompletionContributor
  @Override
  public @NotNull Language getLanguage() {
    return DartLanguage.INSTANCE;
  }

  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartServerCompletionContributor.class);
  @VisibleForTesting
  static final Set<String> DISABLED_EXTENSIONS = ConcurrentHashMap.newKeySet();

  public DartServerCompletionContributor() {
    extend(CompletionType.BASIC,
           or(psiElement().withLanguage(DartLanguage.INSTANCE),
              psiElement().inFile(psiFile().withLanguage(HTMLLanguage.INSTANCE)),
              psiElement().inFile(psiFile().withName("analysis_options.yaml")),
              psiElement().inFile(psiFile().withName("pubspec.yaml")),
              psiElement().inFile(psiFile().withName("fix_data.yaml"))
           ),
           new CompletionProvider() {
             @Override
             public void addCompletions(@NotNull CompletionParameters parameters,
                                           @NotNull ProcessingContext context,
                                           @NotNull CompletionResultSet originalResultSet) {
               final PsiFile originalFile = parameters.getOriginalFile();
               final Project project = originalFile.getProject();

               if (originalResultSet.getPrefixMatcher().getPrefix().isEmpty() &&
                   isRightAfterBadIdentifier(parameters.getEditor().getDocument().getImmutableCharSequence(), parameters.getOffset())) {
                 return;
               }

               final CompletionSorter sorter = createSorter(parameters, originalResultSet.getPrefixMatcher());
               final String uriPrefix = getPrefixIfCompletingUri(parameters);
               final CompletionResultSet resultSet = uriPrefix != null
                                                     ? originalResultSet.withRelevanceSorter(sorter).withPrefixMatcher(uriPrefix)
                                                     : originalResultSet.withRelevanceSorter(sorter);

               // If completion is requested in "Evaluate Expression" dialog or when editing a breakpoint condition, use runtime completion.
               if (originalFile instanceof DartExpressionCodeFragment) {
                 appendRuntimeCompletion(parameters, resultSet);
                 return;
               }

               if (DartAnalysisServerService.isLspCompletionEnabled(project)) {
                 return;
               }

               VirtualFile file = DartResolveUtil.getRealVirtualFile(originalFile);
               if (file instanceof VirtualFileWindow) {
                 file = ((VirtualFileWindow)file).getDelegate();
               }

               if (file == null) return;

               final DartSdk sdk = DartSdk.getDartSdk(project);
               if (sdk == null || !DartAnalysisServerService.isDartSdkVersionSufficient(sdk)) return;

               final DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
               das.updateFilesContent();

               final int startOffsetInHostFile =
                 InjectedLanguageManager.getInstance(project).injectedToHost(originalFile, parameters.getOffset());

               if (!das.getServerVersion().isEmpty() && das.shouldUseCompletion2()) {
                 handleCompletion2(project, resultSet, file, startOffsetInHostFile, parameters.getInvocationCount());
                 return;
               }

               final String completionId = das.completion_getSuggestions(file, startOffsetInHostFile);
               if (completionId == null) return;

               final VirtualFile targetFile = file;
               das.addCompletions(file, completionId, (replacementOffset, replacementLength, suggestion) -> {
                 final CompletionResultSet updatedResultSet;
                 if (uriPrefix != null) {
                   updatedResultSet = resultSet;
                 }
                 else {
                   final String specialPrefix = getPrefixForSpecialCases(parameters, replacementOffset);
                   if (specialPrefix != null) {
                     updatedResultSet = resultSet.withPrefixMatcher(specialPrefix);
                   }
                   else {
                     updatedResultSet = resultSet;
                   }
                 }

                 updatedResultSet.addElement(createLookupElementAskingExtensions(project, suggestion, null, null));
               }, (includedSet, includedKinds, includedRelevanceTags, libraryFilePathSD) -> {
                 if (includedKinds.isEmpty()) {
                   return;
                 }

                 final AvailableSuggestionSet suggestionSet = das.getAvailableSuggestionSet(includedSet.getId());
                 if (suggestionSet == null) {
                   return;
                 }

                 Map<String, Map<String, Set<String>>> existingImports = das.getExistingImports(libraryFilePathSD);
                 for (AvailableSuggestion suggestion : suggestionSet.getItems()) {
                   final String kind = suggestion.getElement().getKind();
                   if (!includedKinds.contains(kind)) {
                     continue;
                   }

                   Set<String> importedLibraries = new HashSet<>();
                   if (existingImports != null) {
                     for (Map.Entry<String, Map<String, Set<String>>> entry : existingImports.entrySet()) {
                       String importedLibraryUri = entry.getKey();
                       Map<String, Set<String>> importedLibrary = entry.getValue();
                       Set<String> names = importedLibrary.get(suggestion.getDeclaringLibraryUri());
                       if (names != null && names.contains(suggestion.getLabel())) {
                         importedLibraries.add(importedLibraryUri);
                       }
                     }
                   }

                   if (!importedLibraries.isEmpty() && !importedLibraries.contains(suggestionSet.getUri())) {
                     // If some library exports this label but the current suggestion set does not, we should filter.
                     continue;
                   }

                   CompletionSuggestion completionSuggestion =
                     createCompletionSuggestionFromAvailableSuggestion(suggestion, includedSet.getRelevance(), includedRelevanceTags);
                   String displayUri = includedSet.getDisplayUri() != null ? includedSet.getDisplayUri() : suggestionSet.getUri();
                   SuggestionDetailsInsertHandlerBase insertHandler =
                     new SuggestionDetailsInsertHandler(project, targetFile, completionSuggestion, startOffsetInHostFile,
                                                        suggestionSet.getId());

                   resultSet.addElement(createLookupElementAskingExtensions(project, completionSuggestion, displayUri, insertHandler));
                 }
               });
             }
           });
  }

  private static void handleCompletion2(@NotNull Project project,
                                        @NotNull CompletionResultSet resultSet,
                                        @NotNull VirtualFile file,
                                        int startOffsetInHostFile,
                                        int invocationCount) {
    // Invocation count is the number of times that the user has pressed Ctrl + Space querying for completions at this location.
    // If 0 or 1, the initial query, then get only the first 100 results, if more than 1, then return (invocationCount - 1) * 1000
    // completions.
    int maxResults;
    if (invocationCount <= 1) {
      maxResults = 100;
    }
    else {
      maxResults = (invocationCount - 1) * 1000;
    }
    DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
    DartAnalysisServerService.CompletionInfo2 completionInfo2 =
      das.completion_getSuggestions2(file, startOffsetInHostFile, maxResults, CompletionMode.BASIC, invocationCount);
    if (completionInfo2 == null) {
      return;
    }

    Set<CompletionSuggestion> addedCompletions = new HashSet<>();
    addToCompletionList(project, resultSet, file, startOffsetInHostFile, completionInfo2, addedCompletions);

    int retryCount = 0;
    while (completionInfo2.myIsIncomplete && completionInfo2.mySuggestions.size() < maxResults && retryCount++ < 3) {
      completionInfo2 = das.completion_getSuggestions2(file, startOffsetInHostFile, maxResults, CompletionMode.BASIC, invocationCount);
      if (completionInfo2 == null) return;

      addToCompletionList(project, resultSet, file, startOffsetInHostFile, completionInfo2, addedCompletions);
    }
  }

  private static void addToCompletionList(@NotNull Project project,
                                          @NotNull CompletionResultSet resultSet,
                                          @NotNull VirtualFile file,
                                          int startOffsetInHostFile,
                                          @NotNull DartAnalysisServerService.CompletionInfo2 completionInfo2,
                                          @NotNull Set<CompletionSuggestion> addedCompletions) {
    List<CompletionSuggestion> suggestions = completionInfo2.mySuggestions;

    // Add all the completion results that came back from the completion_getSuggestions2 call to this result set reference.
    for (CompletionSuggestion suggestion : suggestions) {
      if (!addedCompletions.add(suggestion)) continue;

      String libraryUri = suggestion.getLibraryUri();
      String libraryUriToImport = suggestion.getIsNotImported() == Boolean.TRUE ? libraryUri : null;
      String libraryUriToDisplay =
        libraryUri != null && libraryUri.startsWith("file:") ? StringUtil.substringAfterLast(libraryUri, "/") : libraryUri;

      SuggestionDetailsInsertHandlerBase insertHandler = null;
      if (libraryUriToImport != null) {
        insertHandler = new SuggestionDetailsInsertHandler2(project, file, startOffsetInHostFile, suggestion, libraryUriToImport);
      }

      resultSet.addElement(createLookupElementAskingExtensions(project, suggestion, libraryUriToDisplay, insertHandler));
    }

    // As the user types additional characters, restart the completion only if we don't already have the complete set of completions.
    if (completionInfo2.myIsIncomplete) {
      resultSet.restartCompletionOnAnyPrefixChange();

      String shortcut = KeymapUtil.getFirstKeyboardShortcutText(IdeActions.ACTION_CODE_COMPLETION);
      resultSet.addLookupAdvertisement(DartBundle.message("press.completion.shortcut.again.for.more.results", shortcut));
    }
  }

  private static boolean isRightAfterBadIdentifier(@NotNull CharSequence text, int offset) {
    if (offset == 0 || offset >= text.length()) return false;

    int currentOffset = offset - 1;
    if (!Character.isJavaIdentifierPart(text.charAt(currentOffset))) return false;

    while (currentOffset > 0 && Character.isJavaIdentifierPart(text.charAt(currentOffset - 1))) {
      currentOffset--;
    }

    return !Character.isJavaIdentifierStart(text.charAt(currentOffset));
  }

  private static void appendRuntimeCompletion(@NotNull CompletionParameters parameters,
                                              @NotNull CompletionResultSet resultSet) {
    final PsiFile originalFile = parameters.getOriginalFile();
    final Project project = originalFile.getProject();
    final PsiElement contextElement = originalFile.getContext();
    if (contextElement == null) {
      return;
    }

    final VirtualFile contextFile = contextElement.getContainingFile().getVirtualFile();
    final int contextOffset = contextElement.getTextOffset();

    final Document dummyDocument = FileDocumentManager.getInstance().getDocument(originalFile.getVirtualFile());
    if (dummyDocument == null) {
      return;
    }

    final String code = dummyDocument.getText();
    final int codeOffset = parameters.getOffset();

    final DartAnalysisServerService das = DartAnalysisServerService.getInstance(project);
    final Pair<List<CompletionSuggestion>, List<RuntimeCompletionExpression>> completionResult =
      das.execution_getSuggestions(code, codeOffset,
                                   contextFile, contextOffset,
                                   Collections.emptyList(), Collections.emptyList());
    if (completionResult != null && completionResult.getFirst() != null) {
      for (CompletionSuggestion suggestion : completionResult.getFirst()) {
        resultSet.addElement(createLookupElementAskingExtensions(project, suggestion, null, null));
      }
    }
  }

  private static CompletionSorter createSorter(@NotNull CompletionParameters parameters, @NotNull PrefixMatcher prefixMatcher) {
    final LookupElementWeigher dartWeigher = new LookupElementWeigher("dartRelevance", true, false) {
      @Override
      public Integer weigh(@NotNull LookupElement element) {
        final Object lookupObject = element.getObject();
        return lookupObject instanceof DartLookupObject ? ((DartLookupObject)lookupObject).getRelevance() : 0;
      }
    };

    final CompletionSorter defaultSorter = CompletionSorter.defaultSorter(parameters, prefixMatcher);
    return defaultSorter.weighBefore("liftShorter", dartWeigher);
  }

  private static @Nullable String getPrefixIfCompletingUri(@NotNull CompletionParameters parameters) {
    final PsiElement psiElement = parameters.getOriginalPosition();
    final PsiElement parent = psiElement != null ? psiElement.getParent() : null;
    final PsiElement parentParent = parent instanceof DartStringLiteralExpression ? parent.getParent() : null;
    if (parentParent instanceof DartUriElement) {
      final int uriStringOffset = ((DartUriElement)parentParent).getUriStringAndItsRange().second.getStartOffset();
      if (parameters.getOffset() >= parentParent.getTextRange().getStartOffset() + uriStringOffset) {
        return parentParent.getText().substring(uriStringOffset, parameters.getOffset() - parentParent.getTextRange().getStartOffset());
      }
    }
    return null;
  }

  /**
   * Handles completion provided by angular_analyzer_plugin in HTML files and inside string literals;
   * our PSI doesn't allow to calculate prefix in such cases
   */
  private static @Nullable String getPrefixForSpecialCases(@NotNull CompletionParameters parameters, int replacementOffset) {
    final PsiElement psiElement = parameters.getOriginalPosition();
    if (psiElement == null) return null;

    final PsiElement parent = psiElement.getParent();
    final Language language = psiElement.getContainingFile().getLanguage();
    if (parent instanceof DartStringLiteralExpression || language.isKindOf(XMLLanguage.INSTANCE)) {
      return getPrefixUsingServerData(parameters, replacementOffset);
    }

    return null;
  }

  private static @Nullable String getPrefixUsingServerData(@NotNull CompletionParameters parameters, int replacementOffset) {
    PsiElement element = parameters.getOriginalPosition();
    if (element == null) return null;

    final InjectedLanguageManager manager = InjectedLanguageManager.getInstance(element.getProject());
    final PsiFile injectedContext = parameters.getOriginalFile();

    final int completionOffset = manager.injectedToHost(injectedContext, parameters.getOffset());
    final TextRange range = manager.injectedToHost(injectedContext, element.getTextRange());

    if (completionOffset < range.getStartOffset() || completionOffset > range.getEndOffset()) return null; // shouldn't happen
    if (replacementOffset > completionOffset) return null; // shouldn't happen

    while (element != null) {
      final int elementStartOffset = manager.injectedToHost(injectedContext, element.getTextRange().getStartOffset());
      if (elementStartOffset <= replacementOffset) {
        break; // that's good, we can use this element to calculate prefix
      }
      element = element.getParent();
    }

    if (element != null) {
      final int startOffset = manager.injectedToHost(injectedContext, element.getTextRange().getStartOffset());
      return element.getText().substring(replacementOffset - startOffset, completionOffset - startOffset);
    }

    return null;
  }

  @Override
  public void beforeCompletion(@NotNull CompletionInitializationContext context) {
    if (DartAnalysisServerService.isLspCompletionEnabled(context.getProject()) &&
        !(context.getFile() instanceof DartExpressionCodeFragment)) {
      return;
    }
    final PsiElement psiElement = context.getFile().findElementAt(context.getStartOffset());
    final PsiElement parent = psiElement != null ? psiElement.getParent() : null;
    if (parent instanceof DartStringLiteralExpression) {
      final PsiElement parentParent = parent.getParent();
      if (parentParent instanceof DartUriElement) {
        final Pair<String, TextRange> uriAndRange = ((DartUriElement)parentParent).getUriStringAndItsRange();
        context.setReplacementOffset(parentParent.getTextRange().getStartOffset() + uriAndRange.second.getEndOffset());
      }
      else {
        // If replacement context is not set explicitly then com.intellij.codeInsight.completion.CompletionProgressIndicator#duringCompletion
        // implementation looks for the reference at caret and on Tab replaces the whole reference.
        // angular_analyzer_plugin provides angular-specific completion inside Dart string literals. Without the following hack Tab replaces
        // too much useful text. This hack is not ideal though as it may leave a piece of tail not replaced.
        // TODO: use replacementLength received from the server
        context.setReplacementOffset(context.getReplacementOffset());
      }
    }
    else {
      PsiReference reference = context.getFile().findReferenceAt(context.getStartOffset());
      if (reference instanceof PsiMultiReference && ((PsiMultiReference)reference).getReferences().length > 0) {
        reference.getRangeInElement(); // to ensure that references are sorted by range
        reference = ((PsiMultiReference)reference).getReferences()[0];
      }
      if (reference instanceof DartNewExpression) {
        // historically DartNewExpression is a reference; it can appear here only in situation like new Foo(o.<caret>);
        // without the following hack closing paren is replaced on Tab. We won't get here if at least one symbol after dot typed.
        if (((DartNewExpression) reference).getLastChild() instanceof PsiErrorElement) {
          // f(.new) or f(.named) - we want default replacement logic
        } else {
          context.setReplacementOffset(context.getStartOffset());
        }
      } else if (reference instanceof DartParenthesizedExpression ||
          reference instanceof DartListLiteralExpression ||
          reference instanceof DartSetOrMapLiteralExpression) {
        context.setReplacementOffset(context.getStartOffset());
      }
      if (reference instanceof DartReferenceExpression) {
        final PsiElement firstChild = ((DartReferenceExpression)reference).getFirstChild();
        final PsiElement lastChild = ((DartReferenceExpression)reference).getLastChild();
        if (firstChild != lastChild &&
            lastChild instanceof PsiErrorElement &&
            context.getStartOffset() <= firstChild.getTextRange().getEndOffset()) {
          context.setReplacementOffset(firstChild.getTextRange().getEndOffset());
        }
      }
    }
  }

  private static Image applyOverlay(Image base, boolean condition, Image overlay) {
    if (condition) {
      return ImageEffects.layered(base, overlay);
    }
    return base;
  }

  @VisibleForTesting
  @SuppressWarnings("deprecation")
  static @NotNull LookupElement createLookupElementAskingExtensions(@NotNull Project project,
                                                                    @NotNull CompletionSuggestion suggestion,
                                                                    @Nullable String displayUri,
                                                                    @Nullable SuggestionDetailsInsertHandlerBase insertHandler) {
    for (DartCompletionExtension extension : DartCompletionExtension.getExtensions()) {
      String extensionName = extension.getClass().getName();
      if (DISABLED_EXTENSIONS.contains(extensionName)) {
        continue;
      }
      try {
        LookupElement lookupElement = extension.createLookupElement(project, suggestion);
        if (lookupElement != null) {
          return lookupElement;
        }
      }
      catch (ProcessCanceledException e) {
        throw e;
      }
      catch (Throwable t) {
        if (DISABLED_EXTENSIONS.add(extensionName)) {
          LOG.warn("Exception from DartCompletionExtension (disabling for session): " + extensionName, t);
        }
      }
    }
    return createLookupElement(project, suggestion, displayUri, insertHandler);
  }

  // used by Flutter plugin
  public static @NotNull LookupElementBuilder createLookupElement(@NotNull Project project, @NotNull CompletionSuggestion suggestion) {
    return createLookupElement(project, suggestion, null, null);
  }

  private static @NotNull LookupElementBuilder createLookupElement(@NotNull Project project,
                                                                   @NotNull CompletionSuggestion suggestion,
                                                                   @Nullable String displayUri,
                                                                   @Nullable SuggestionDetailsInsertHandlerBase insertHandler) {
    final Element element = suggestion.getElement();
    final Location location = element == null ? null : element.getLocation();
    final DartLookupObject lookupObject = new DartLookupObject(project, location, suggestion.getRelevance());

    final String lookupString = suggestion.getCompletion();
    LookupElementBuilder lookup = LookupElementBuilder.create(lookupObject, lookupString);

    if (suggestion.getDisplayText() != null) {
      lookup = lookup.withPresentableText(suggestion.getDisplayText());
    }

    // keywords are bold
    if (suggestion.getKind().equals(CompletionSuggestionKind.KEYWORD)) {
      lookup = lookup.bold();
    }

    final int dotIndex = lookupString.indexOf('.');
    if (dotIndex > 0 && dotIndex < lookupString.length() - 1 &&
        StringUtil.isJavaIdentifier(lookupString.substring(0, dotIndex)) &&
        StringUtil.isJavaIdentifier(lookupString.substring(dotIndex + 1))) {
      // 'path.Context' should match 'Conte' prefix
      lookup = lookup.withLookupString(lookupString.substring(dotIndex + 1));
    }

    boolean shouldSetSelection = true;
    if (element != null) {
      // @deprecated
      if (element.isDeprecated()) {
        lookup = lookup.strikeout();
      }

      if (StringUtil.isEmpty(suggestion.getDisplayText())) {
        // append type parameters
        final String typeParameters = element.getTypeParameters();
        if (typeParameters != null) {
          lookup = lookup.appendTailText(typeParameters, false);
        }
        // append parameters
        final String parameters = element.getParameters();
        if (parameters != null) {
          lookup = lookup.appendTailText(parameters, false);
        }
      }

      // append return type
      final String returnType = element.getReturnType();
      if (!StringUtils.isEmpty(returnType)) {
        lookup = lookup.withTypeText(returnType, true);
      }

      // If this is a class or similar global symbol, try to show which package it's coming from.
      if (!StringUtils.isEmpty(displayUri)) {
        String packageInfo = "(" + displayUri + ")";
        lookup = lookup.withTypeText(StringUtils.isEmpty(returnType) ? packageInfo : returnType + " " + packageInfo, true);
      }

      // icon
      Image icon = getBaseImage(element);
      if (icon != null) {
        if (suggestion.getKind().equals(CompletionSuggestionKind.OVERRIDE)) {
          icon = ImageEffects.appendRight(icon, AllIcons.Gutter.OverridingMethod);
        }
        else {
          icon = ImageEffects.appendRight(icon, element.isPrivate() ? PlatformIconGroup.nodesC_private()
                                                                         : PlatformIconGroup.nodesC_public());
          icon = applyOverlay(icon, element.isFinal(), AllIcons.Nodes.FinalMark);
          icon = applyOverlay(icon, element.isConst(), AllIcons.Nodes.FinalMark);
        }

        lookup = lookup.withIcon(icon);
      }

      // Prepare for typing arguments, if any.
      if (insertHandler == null &&
          CompletionSuggestionKind.INVOCATION.equals(suggestion.getKind()) &&
          suggestion.getParameterNames() != null) {
        shouldSetSelection = false;
        lookup = lookup.withInsertHandler((context, item) -> handleFunctionInvocationInsertion(context, item, suggestion));
      }
    }

    if (insertHandler != null) {
      lookup = lookup.withInsertHandler(insertHandler);
    }
    else if (shouldSetSelection) {
      // Use selection offset / length.
      lookup = lookup.withInsertHandler((context, item) -> {
        final Editor editor = context.getEditor();
        final int startOffset = context.getStartOffset() + suggestion.getSelectionOffset();
        final int endOffset = startOffset + suggestion.getSelectionLength();
        editor.getCaretModel().moveToOffset(startOffset);
        if (endOffset > startOffset) {
          editor.getSelectionModel().setSelection(startOffset, endOffset);
        }
      });
    }

    return lookup;
  }

  private static void handleFunctionInvocationInsertion(@NotNull InsertionContext context,
                                                        @NotNull LookupElement item,
                                                        @NotNull CompletionSuggestion suggestion) {
    List<String> parameterNames = suggestion.getParameterNames();
    if (parameterNames == null) {
      return;
    }

    // like in JavaCompletionUtil.insertParentheses()
    final boolean needRightParenth = CodeInsightSettings.getInstance().AUTOINSERT_PAIR_BRACKET ||
                                     parameterNames.isEmpty() && context.getCompletionChar() != '(';

    boolean hasParameters = !parameterNames.isEmpty();
    final ParenthesesInsertHandler<LookupElement> handler =
      ParenthesesInsertHandler.getInstance(hasParameters, false, false, needRightParenth, false);
    handler.handleInsert(context, item);

    final Editor editor = context.getEditor();
    if (hasParameters && DartCodeInsightSettings.getInstance().INSERT_DEFAULT_ARG_VALUES) {
      final String argumentListString = suggestion.getDefaultArgumentListString();
      if (argumentListString != null) {
        final Document document = editor.getDocument();
        int offset = editor.getCaretModel().getOffset();

        // At this point caret is expected to be right after the opening paren.
        // But if user was completing using Tab over the existing method call with arguments then old arguments are still there,
        // if so, skip inserting argumentListString

        final CharSequence text = document.getCharsSequence();
        if (text.charAt(offset - 1) == '(' && text.charAt(offset) == ')') {
          document.insertString(offset, argumentListString);

          PsiDocumentManager.getInstance(context.getProject()).commitDocument(document);

          final TemplateBuilder builder = TemplateBuilderFactory.getInstance().createTemplateBuilder(context.getFile());

          final int[] ranges = suggestion.getDefaultArgumentListTextRanges();
          // Only proceed if ranges are provided and well-formed.
          if (ranges != null && (ranges.length & 1) == 0) {
            int index = 0;
            while (index < ranges.length) {
              final int start = ranges[index];
              final int length = ranges[index + 1];
              final String arg = argumentListString.substring(start, start + length);
              final TextExpression expression = new TextExpression(arg);
              final TextRange range = new TextRange(offset + start, offset + start + length);

              index += 2;
              // Consulo: no public TemplateBuilder.replaceRange(range, name, expression, alwaysStopAt), file-relative range is the same
              builder.replaceElement(context.getFile(), range, "group_" + (index - 1), expression, true);
            }

            builder.run(editor, true);
          }
        }
      }
    }

    Object itemObj = item.getObject();
    if (itemObj instanceof DartLookupObject) {
      AutoPopupController.getInstance(context.getProject()).autoPopupParameterInfo(editor, ((DartLookupObject)itemObj).findPsiElement());
    }
  }


  private static @NotNull CompletionSuggestion createCompletionSuggestionFromAvailableSuggestion(@NotNull AvailableSuggestion suggestion,
                                                                                                 int suggestionSetRelevance,
                                                                                                 @NotNull Map<String, IncludedSuggestionRelevanceTag> includedSuggestionRelevanceTags) {
    int relevanceBoost = 0;
    List<String> relevanceTags = suggestion.getRelevanceTags();
    if (relevanceTags != null) {
      for (String tag : relevanceTags) {
        IncludedSuggestionRelevanceTag relevanceTag = includedSuggestionRelevanceTags.get(tag);
        if (relevanceTag != null) {
          relevanceBoost = Math.max(relevanceBoost, relevanceTag.getRelevanceBoost());
        }
      }
    }

    Element element = suggestion.getElement();
    return new CompletionSuggestion(
      "UNKNOWN", // we don't have info about CompletionSuggestionKind
      suggestionSetRelevance + relevanceBoost,
      suggestion.getLabel(),
      null,
      0,
      0,
      0,
      0,
      element.isDeprecated(),
      false,
      null,
      null,
      null,
      suggestion.getDefaultArgumentListString(),
      suggestion.getDefaultArgumentListTextRanges(),
      element,
      element.getReturnType(),
      suggestion.getParameterNames(),
      null,
      null,
      null,
      null,
      null,
      null,
      null);
  }

  static Image getBaseImage(Element element) {
    final String elementKind = element.getKind();
    return switch (elementKind) {
      case ElementKind.CLASS, ElementKind.CLASS_TYPE_ALIAS -> element.isAbstract() ? AllIcons.Nodes.AbstractClass : AllIcons.Nodes.Class;
      case ElementKind.ENUM -> AllIcons.Nodes.Enum;
      case ElementKind.MIXIN -> AllIcons.Nodes.AbstractClass;
      case ElementKind.ENUM_CONSTANT, ElementKind.FIELD -> AllIcons.Nodes.Field;
      case ElementKind.COMPILATION_UNIT -> PlatformIconGroup.filetypesAny_type();
      case ElementKind.CONSTRUCTOR -> AllIcons.Nodes.ClassInitializer;
      case ElementKind.GETTER -> element.isTopLevelOrStatic() ? AllIcons.Nodes.PropertyReadStatic : AllIcons.Nodes.PropertyRead;
      case ElementKind.SETTER -> element.isTopLevelOrStatic() ? AllIcons.Nodes.PropertyWriteStatic : AllIcons.Nodes.PropertyWrite;
      case ElementKind.METHOD -> element.isAbstract() ? AllIcons.Nodes.AbstractMethod : AllIcons.Nodes.Method;
      case ElementKind.FUNCTION -> AllIcons.Nodes.Lambda;
      case ElementKind.FUNCTION_TYPE_ALIAS, ElementKind.TYPE_ALIAS -> AllIcons.Nodes.Annotationtype;
      case ElementKind.TOP_LEVEL_VARIABLE -> AllIcons.Nodes.Variable;
      case ElementKind.EXTENSION -> PlatformIconGroup.nodesInterface(); // Consulo: no Nodes.Include icon
      default -> null;
    };
  }

  private abstract static class SuggestionDetailsInsertHandlerBase implements InsertHandler<LookupElement> {
    protected final @NotNull Project myProject;
    protected final @NotNull VirtualFile myFile;
    protected final int myStartOffsetInHostFile;
    protected final @NotNull CompletionSuggestion mySuggestion;

    private SuggestionDetailsInsertHandlerBase(@NotNull Project project,
                                               @NotNull VirtualFile file,
                                               int startOffsetInHostFile,
                                               @NotNull CompletionSuggestion suggestion) {
      myProject = project;
      myFile = file;
      myStartOffsetInHostFile = startOffsetInHostFile;
      mySuggestion = suggestion;
    }

    protected abstract @Nullable Pair<String, SourceChange> getSuggestionDetails(@NotNull DartAnalysisServerService das);

    @Override
    public final void handleInsert(@NotNull InsertionContext context, @NotNull LookupElement item) {
      Pair<String, SourceChange> result = getSuggestionDetails(DartAnalysisServerService.getInstance(myProject));
      if (result == null) return;

      context.getDocument().replaceString(context.getStartOffset(), context.getTailOffset(), result.getFirst());

      SourceChange change = result.getSecond();
      if (change == null) return;

      try {
        AssistUtils.applySourceChange(myProject, change, true);
      }
      catch (DartSourceEditException e) {
        CommonRefactoringUtil.showErrorHint(myProject, context.getEditor(), e.getMessage(), CommonBundle.getErrorTitle(), null);
        return;
      }

      Element element = mySuggestion.getElement();
      if (element != null &&
          (ElementKind.FUNCTION.equals(element.getKind()) || ElementKind.CONSTRUCTOR.equals(element.getKind())) &&
          mySuggestion.getParameterNames() != null) {
        handleFunctionInvocationInsertion(context, item, mySuggestion);
      }
    }
  }

  private static class SuggestionDetailsInsertHandler extends SuggestionDetailsInsertHandlerBase {
    private final int mySuggestionSetId;

    private SuggestionDetailsInsertHandler(@NotNull Project project,
                                           @NotNull VirtualFile file,
                                           @NotNull CompletionSuggestion suggestion,
                                           int startOffsetInHostFile,
                                           int suggestionSetId) {
      super(project, file, startOffsetInHostFile, suggestion);
      mySuggestionSetId = suggestionSetId;
    }

    @Override
    protected @Nullable Pair<String, SourceChange> getSuggestionDetails(@NotNull DartAnalysisServerService das) {
      return das.completion_getSuggestionDetails(myFile, mySuggestionSetId, mySuggestion.getCompletion(), myStartOffsetInHostFile);
    }
  }

  private static class SuggestionDetailsInsertHandler2 extends SuggestionDetailsInsertHandlerBase {
    private final String myLibraryUriToImport;

    private SuggestionDetailsInsertHandler2(@NotNull Project project,
                                            @NotNull VirtualFile file,
                                            int startOffsetInHostFile,
                                            @NotNull CompletionSuggestion suggestion,
                                            @NotNull String libraryUriToImport) {
      super(project, file, startOffsetInHostFile, suggestion);
      myLibraryUriToImport = libraryUriToImport;
    }

    @Override
    protected @Nullable Pair<String, SourceChange> getSuggestionDetails(@NotNull DartAnalysisServerService das) {
      return das.completion_getSuggestionDetails2(myFile, myStartOffsetInHostFile, mySuggestion.getCompletion(), myLibraryUriToImport);
    }
  }
}
