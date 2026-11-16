/*
 * Copyright 2026 The Chromium Authors. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be
 * found in the LICENSE file.
 */
package com.jetbrains.lang.dart.lsp;

import com.intellij.platform.lsp.api.customization.LspSemanticTokensSupport;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.highlight.DartSyntaxHighlighterColors;
import consulo.colorScheme.TextAttributesKey;
import consulo.language.psi.PsiFile;
import org.eclipse.lsp4j.SemanticTokenModifiers;
import org.eclipse.lsp4j.SemanticTokenTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class DartLspSemanticTokensSupport extends LspSemanticTokensSupport {
  public static final DartLspSemanticTokensSupport INSTANCE = new DartLspSemanticTokensSupport();

  private DartLspSemanticTokensSupport() {
  }

  @Override
  public boolean shouldAskServerForSemanticTokens(@NotNull PsiFile psiFile) {
    return psiFile.getFileType() == DartFileType.INSTANCE;
  }

  @Override
  public @Nullable TextAttributesKey getTextAttributesKey(@NotNull String tokenType, @NotNull List<String> modifiers) {
    boolean isDecl = modifiers.contains(SemanticTokenModifiers.Declaration);
    boolean isStatic = modifiers.contains(SemanticTokenModifiers.Static);
    boolean isInstance = modifiers.contains("instance");
    // Custom modifier added in Dart SDK 3.15.0-67.0.dev (dart-lang/sdk#64621).
    boolean isTearOff = modifiers.contains("tearOff");

    if (modifiers.contains("annotation") || tokenType.equals("annotation")) {
      return DartSyntaxHighlighterColors.ANNOTATION;
    }

    switch (tokenType) {
      case SemanticTokenTypes.Class:
        if (modifiers.contains("constructor")) return DartSyntaxHighlighterColors.CONSTRUCTOR;
        if (modifiers.contains("mixin")) return DartSyntaxHighlighterColors.MIXIN;
        if (modifiers.contains("extensionType")) return DartSyntaxHighlighterColors.EXTENSION_TYPE;
        if (modifiers.contains("extension")) return DartSyntaxHighlighterColors.EXTENSION;
        return DartSyntaxHighlighterColors.CLASS;

      case SemanticTokenTypes.Enum:
        return DartSyntaxHighlighterColors.ENUM;
      case SemanticTokenTypes.EnumMember:
        return DartSyntaxHighlighterColors.ENUM_CONSTANT;
      case SemanticTokenTypes.TypeParameter:
        return DartSyntaxHighlighterColors.TYPE_PARAMETER;
      case SemanticTokenTypes.Type:
        if (modifiers.contains("functionTypeAlias")) return DartSyntaxHighlighterColors.FUNCTION_TYPE_ALIAS;
        if (modifiers.contains("typeAlias")) return DartSyntaxHighlighterColors.TYPE_ALIAS;
        // Since 3.15.0-67.0.dev every typedef carries an alias modifier, so a bare `type` is `dynamic` (TYPE_NAME_DYNAMIC).
        return DartSyntaxHighlighterColors.TYPE_NAME_DYNAMIC;

      case SemanticTokenTypes.Method:
        if (modifiers.contains("constructor")) {
          return isTearOff ? DartSyntaxHighlighterColors.CONSTRUCTOR_TEAR_OFF : DartSyntaxHighlighterColors.CONSTRUCTOR;
        }
        if (isStatic) {
          if (isTearOff) return DartSyntaxHighlighterColors.STATIC_METHOD_TEAR_OFF;
          if (isDecl) return DartSyntaxHighlighterColors.STATIC_METHOD_DECLARATION;
          return DartSyntaxHighlighterColors.STATIC_METHOD_REFERENCE;
        }
        if (isTearOff) return DartSyntaxHighlighterColors.INSTANCE_METHOD_TEAR_OFF;
        if (isDecl) return DartSyntaxHighlighterColors.INSTANCE_METHOD_DECLARATION;
        return DartSyntaxHighlighterColors.INSTANCE_METHOD_REFERENCE;

      // DAS marks top-level functions (declarations, references, and tear-offs) with `static`; local functions have no `static`.
      case SemanticTokenTypes.Function:
        if (isStatic) {
          if (isTearOff) return DartSyntaxHighlighterColors.TOP_LEVEL_FUNCTION_TEAR_OFF;
          if (isDecl) return DartSyntaxHighlighterColors.TOP_LEVEL_FUNCTION_DECLARATION;
          return DartSyntaxHighlighterColors.TOP_LEVEL_FUNCTION_REFERENCE;
        }
        if (isTearOff) return DartSyntaxHighlighterColors.LOCAL_FUNCTION_TEAR_OFF;
        if (isDecl) return DartSyntaxHighlighterColors.LOCAL_FUNCTION_DECLARATION;
        return DartSyntaxHighlighterColors.LOCAL_FUNCTION_REFERENCE;

      // Known gap: DAS emits identical type/modifiers for getters and setters, so setters use the getter keys.
      case SemanticTokenTypes.Property:
        if (isStatic) {
          return isDecl ? DartSyntaxHighlighterColors.STATIC_GETTER_DECLARATION : DartSyntaxHighlighterColors.STATIC_GETTER_REFERENCE;
        }
        if (isInstance) {
          return isDecl ? DartSyntaxHighlighterColors.INSTANCE_GETTER_DECLARATION : DartSyntaxHighlighterColors.INSTANCE_GETTER_REFERENCE;
        }
        return isDecl ? DartSyntaxHighlighterColors.TOP_LEVEL_GETTER_DECLARATION : DartSyntaxHighlighterColors.TOP_LEVEL_GETTER_REFERENCE;

      case SemanticTokenTypes.Variable:
        if (modifiers.contains("importPrefix")) return DartSyntaxHighlighterColors.IMPORT_PREFIX;
        // Known gap: static field and top-level variable declarations are both emitted as [declaration, static].
        if (isStatic) return DartSyntaxHighlighterColors.STATIC_FIELD_DECLARATION;
        if (isInstance) {
          return isDecl ? DartSyntaxHighlighterColors.INSTANCE_FIELD_DECLARATION : DartSyntaxHighlighterColors.INSTANCE_FIELD_REFERENCE;
        }
        // Known gap: dynamic local variables/parameters are not distinguished on the wire.
        if (isDecl) return DartSyntaxHighlighterColors.LOCAL_VARIABLE_DECLARATION;
        return DartSyntaxHighlighterColors.LOCAL_VARIABLE_REFERENCE;

      case SemanticTokenTypes.Parameter:
        return isDecl ? DartSyntaxHighlighterColors.PARAMETER_DECLARATION : DartSyntaxHighlighterColors.PARAMETER_REFERENCE;
      case "label":
        return DartSyntaxHighlighterColors.LABEL;
      case SemanticTokenTypes.Namespace:
        return DartSyntaxHighlighterColors.LIBRARY_NAME;
      case SemanticTokenTypes.Keyword:
        return DartSyntaxHighlighterColors.KEYWORD;
      case SemanticTokenTypes.String:
        return modifiers.contains("escape") ? DartSyntaxHighlighterColors.VALID_STRING_ESCAPE : null;
      // Covers IDENTIFIER_DEFAULT and UNRESOLVED_INSTANCE_MEMBER_REFERENCE (known gap: not distinguished on the wire).
      case "source":
        return DartSyntaxHighlighterColors.IDENTIFIER;
      // Comments, numbers, plain strings, booleans, and operators are left to DartSyntaxHighlighter (lexer), matching legacy behavior.
      default:
        return null;
    }
  }
}
