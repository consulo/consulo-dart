// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import com.jetbrains.lang.dart.DartLanguage;
import consulo.application.ReadAction;
import consulo.language.Language;
import consulo.language.ast.ASTNode;
import consulo.language.ast.IElementType;
import consulo.language.editor.codeBlock.CodeBlockHighlighter;
import consulo.project.Project;
import consulo.util.lang.StringUtil;
import org.intellij.markdown.MarkdownElementType;
import org.intellij.markdown.MarkdownElementTypes;
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor;
import org.intellij.markdown.html.CodeFenceGeneratingProvider;
import org.intellij.markdown.html.GeneratingProvider;
import org.intellij.markdown.html.HtmlGenerator;
import org.intellij.markdown.parser.LinkMap;
import org.intellij.markdown.parser.MarkdownParser;
import org.intellij.plugins.markdown.html.TreeBuildingSink;
import org.intellij.plugins.markdown.injection.aliases.CodeFenceLanguageGuesser;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * IntelliJ's {@code com.intellij.markdown.utils.MarkdownToHtmlConverterKt} (platform/markdown-utils) on the markdown engine of
 * the consulo.markdown plugin. The tree root is not {@code MARKDOWN_FILE}, so the html has no {@code <body>} wrapper.
 */
public final class MarkdownToHtmlConverterKt {
  private static final IElementType embeddedHtmlType = new MarkdownElementType("ROOT");

  private MarkdownToHtmlConverterKt() {
  }

  public static @NotNull String convertMarkdownToHtml(@NotNull String markdownText) {
    GFMFlavourDescriptor flavour = new GFMFlavourDescriptor();
    ASTNode parsedTree = parse(flavour, markdownText);
    return new HtmlGenerator(markdownText, parsedTree, flavour).generateHtml();
  }

  /**
   * Consulo: like {@link #convertMarkdownToHtml(String)}, with the code of the fences highlighted, as the documentation of
   * consulo-java and consulo-rust. A fence without a known language is Dart code.
   */
  public static @NotNull String convertMarkdownToHtml(@NotNull String markdownText, @NotNull Project project) {
    GFMFlavourDescriptor flavour = new GFMFlavourDescriptor();
    ASTNode parsedTree = parse(flavour, markdownText);
    Map<IElementType, GeneratingProvider> providers = new HashMap<>(flavour.createHtmlGeneratingProviders(LinkMap.buildLinkMap(parsedTree, markdownText), null));
    providers.put(MarkdownElementTypes.CODE_FENCE, new CodeFenceGeneratingProvider((infoString, code) -> {
      Language language = StringUtil.isEmptyOrSpaces(infoString) ? null : CodeFenceLanguageGuesser.guessLanguageForInjection(infoString.trim());
      Language codeLanguage = language != null ? language : DartLanguage.INSTANCE;
      return ReadAction.compute(() -> project.getApplication().getInstance(CodeBlockHighlighter.class)
        .newHtmlBuilder(project, codeLanguage, code)
        .build());
    }));
    return new HtmlGenerator(markdownText, parsedTree, providers, false).generateHtml();
  }

  // Consulo: MarkdownParser.parse(root, text) is parseBlocks + walkBlocks into a tree sink
  private static @NotNull ASTNode parse(@NotNull GFMFlavourDescriptor flavour, @NotNull String markdownText) {
    MarkdownParser parser = new MarkdownParser(flavour);
    TreeBuildingSink sink = new TreeBuildingSink(markdownText);
    parser.walkBlocks(parser.parseBlocks(embeddedHtmlType, markdownText), markdownText, true, sink);
    return sink.getRoot();
  }
}
