package com.jetbrains.lang.dart.ide;

import com.jetbrains.lang.dart.DartFileType;
import consulo.annotation.component.ExtensionImpl;
import consulo.codeEditor.Editor;
import consulo.language.editor.readerMode.ReaderModeMatcher;
import consulo.language.editor.readerMode.ReaderModeProvider;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.fileType.FileTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ExtensionImpl
public final class DartReaderModeMatcher implements ReaderModeMatcher {
  @Override
  public @Nullable Boolean matches(@NotNull Project project,
                                   @NotNull VirtualFile file,
                                   @Nullable Editor editor,
                                   @NotNull ReaderModeProvider.ReaderMode mode) {
    return FileTypeRegistry.getInstance().isFileOfType(file, DartFileType.INSTANCE) ? false : null;
  }
}
