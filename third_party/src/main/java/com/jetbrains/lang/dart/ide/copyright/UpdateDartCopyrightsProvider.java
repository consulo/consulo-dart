package com.jetbrains.lang.dart.ide.copyright;

import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.psi.DartFile;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.copyright.UpdateCopyrightsProvider;
import consulo.language.copyright.UpdatePsiFileCopyright;
import consulo.language.copyright.config.CopyrightFileConfig;
import consulo.language.copyright.config.CopyrightProfile;
import consulo.language.copyright.ui.TemplateCommentPanel;
import consulo.language.psi.PsiComment;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiWhiteSpace;
import consulo.project.Project;
import consulo.virtualFileSystem.fileType.FileType;
import org.jetbrains.annotations.NotNull;

// Consulo: there is no UpdateJavaScriptFileCopyright, UpdateDartFileCopyright scans leading comments itself
@ExtensionImpl
public final class UpdateDartCopyrightsProvider extends UpdateCopyrightsProvider<CopyrightFileConfig> {
  @Override
  public @NotNull FileType getFileType() {
    return DartFileType.INSTANCE;
  }

  @Override
  public @NotNull UpdatePsiFileCopyright<CopyrightFileConfig> createInstance(@NotNull PsiFile file, @NotNull CopyrightProfile options) {
    return new UpdateDartFileCopyright(file, options);
  }

  @Override
  public @NotNull CopyrightFileConfig createDefaultOptions() {
    return new CopyrightFileConfig();
  }

  @Override
  public @NotNull TemplateCommentPanel createConfigurable(@NotNull Project project,
                                                          @NotNull TemplateCommentPanel parentPane,
                                                          @NotNull FileType fileType) {
    return new TemplateCommentPanel(fileType, parentPane, project);
  }

  private static class UpdateDartFileCopyright extends UpdatePsiFileCopyright<CopyrightFileConfig> {
    UpdateDartFileCopyright(@NotNull PsiFile file, @NotNull CopyrightProfile options) {
      super(file, options);
    }

    @Override
    protected boolean accept() {
      return getFile() instanceof DartFile;
    }

    @Override
    protected void scanFile() {
      PsiElement first = getFile().getFirstChild();
      PsiElement last = first;
      PsiElement next = first;
      while (next != null) {
        if (next instanceof PsiComment || next instanceof PsiWhiteSpace) {
          next = getNextSibling(next);
        }
        else {
          break;
        }
        last = next;
      }

      if (first != null) {
        checkComments(first, last, true);
      }
      else {
        checkComments(null, null, true);
      }
    }
  }
}
