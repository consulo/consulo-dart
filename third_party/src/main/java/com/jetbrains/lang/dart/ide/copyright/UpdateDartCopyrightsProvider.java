package com.jetbrains.lang.dart.ide.copyright;

import consulo.virtualFileSystem.fileType.FileType;
import consulo.module.Module;
import consulo.project.Project;
import consulo.virtualFileSystem.VirtualFile;
import com.jetbrains.lang.dart.psi.DartFile;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.psi.UpdateCopyright;
import com.maddyhome.idea.copyright.psi.UpdateCopyrightsProvider;
import com.maddyhome.idea.copyright.psi.UpdateJavaScriptFileCopyright;

public final class UpdateDartCopyrightsProvider extends UpdateCopyrightsProvider {

  @Override
  public UpdateCopyright createInstance(final Project project,
                                        final Module module,
                                        final VirtualFile file,
                                        final FileType base,
                                        final CopyrightProfile options) {
    return new UpdateDartFileCopyright(project, module, file, options);
  }

  private static class UpdateDartFileCopyright extends UpdateJavaScriptFileCopyright {
    UpdateDartFileCopyright(final Project project,
                                   final Module module,
                                   final VirtualFile file,
                                   final CopyrightProfile options) {
      super(project, module, file, options);
    }

    @Override
    protected boolean accept() {
      return getFile() instanceof DartFile;
    }
  }
}

