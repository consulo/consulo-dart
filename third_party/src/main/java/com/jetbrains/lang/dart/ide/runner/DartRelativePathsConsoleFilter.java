// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner;

import consulo.execution.ui.console.Filter;
import consulo.execution.ui.console.OpenFileHyperlinkInfo;
import consulo.application.ApplicationManager;
import consulo.project.Project;
import consulo.application.util.function.Computable;
import consulo.util.lang.Couple;
import consulo.util.lang.Trinity;
import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// resolves paths relative to working dir that are reported by package:test or 'pub build'
public class DartRelativePathsConsoleFilter implements Filter {
  private final @NotNull Project myProject;
  private final @NotNull String myBaseDirPath;

  public DartRelativePathsConsoleFilter(final @NotNull Project project, final @NotNull String baseDirPath) {
    myProject = project;
    myBaseDirPath = baseDirPath;
  }

  @Override
  public @Nullable Result applyFilter(final @NotNull String text, final int entireLength) {
    final String trimmedText = StringUtil.trimLeading(text);
    final Trinity<String, Integer, Integer> fileRelPathLineAndColumn = getFileRelPathLineAndColumn(trimmedText);
    if (fileRelPathLineAndColumn == null) return null;

    final String fileRelPath = fileRelPathLineAndColumn.first;
    final int line = fileRelPathLineAndColumn.second;
    final int column = fileRelPathLineAndColumn.third;

    return ApplicationManager.getApplication().runReadAction((Computable<Result>)() -> {
      final VirtualFile file =
        LocalFileSystem.getInstance().findFileByPath(myBaseDirPath + "/" + trimmedText.substring(0, fileRelPath.length()));
      if (file == null || file.isDirectory()) return null;

      return new Result(entireLength - trimmedText.length(),
                        entireLength - trimmedText.length() + fileRelPath.length(),
                        new OpenFileHyperlinkInfo(myProject, file, line, column));
    });
  }

  public static @Nullable Trinity<String, Integer, Integer> getFileRelPathLineAndColumn(final @NotNull String text) {
    // "web\anagram.dart:23:8:"
    // "  ../subdir/someFile.dart 73:29         main.<fn>"
    if (text.isEmpty()) return null;
    if (text.charAt(0) != '.' && !Character.isJavaIdentifierStart(text.charAt(0))) return null;

    int index = 0;
    while (text.length() > ++index) {
      final char ch = text.charAt(index);
      if (ch != '/' && ch != '\\' && ch != '.' && !Character.isJavaIdentifierPart(ch)) break;
    }

    if (text.length() <= index + 3) return null; // at least 4 symbols more: " 1:1" or ":1:1"

    final String relPath = text.substring(0, index);
    if (!relPath.endsWith(".dart") || (text.charAt(index) != ' ' && text.charAt(index) != ':')) return null;

    final Couple<Integer> lineAndColumn = DartPositionInfo.parseLineAndColumn(text.substring(index + 1));
    final int line = lineAndColumn == null ? -1 : lineAndColumn.first >= 0 ? lineAndColumn.first - 1 : lineAndColumn.first;
    final int column = lineAndColumn == null ? -1 : lineAndColumn.second >= 0 ? lineAndColumn.second - 1 : lineAndColumn.second;
    if (line < 0 || column < 0) return null;

    return Trinity.create(relPath, line, column);
  }
}
