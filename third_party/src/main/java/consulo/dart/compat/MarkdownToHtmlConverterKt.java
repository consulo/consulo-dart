// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.language.ast.IElementType;
import org.intellij.markdown.MarkdownElementType;
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor;
import org.intellij.markdown.html.HtmlGenerator;
import org.intellij.markdown.parser.MarkdownParser;
import org.intellij.plugins.markdown.html.TreeBuildingSink;
import org.jetbrains.annotations.NotNull;

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
    // Consulo: MarkdownParser.parse(root, text) is parseBlocks + walkBlocks into a tree sink
    MarkdownParser parser = new MarkdownParser(flavour);
    TreeBuildingSink sink = new TreeBuildingSink(markdownText);
    parser.walkBlocks(parser.parseBlocks(embeddedHtmlType, markdownText), markdownText, true, sink);
    return new HtmlGenerator(markdownText, sink.getRoot(), flavour).generateHtml();
  }
}
