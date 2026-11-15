// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.base;

import consulo.execution.configuration.ConfigurationFactory;
import consulo.execution.configuration.LocatableConfigurationBase;
import consulo.language.editor.refactoring.event.RefactoringListenerProvider;
import consulo.execution.RuntimeConfigurationException;
import consulo.project.Project;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiDirectory;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiFileSystemItem;
import consulo.language.editor.refactoring.event.RefactoringElementListener;
import consulo.language.editor.refactoring.event.UndoRefactoringElementAdapter;
import consulo.util.io.PathUtil;
import consulo.util.xml.serializer.SkipDefaultValuesSerializationFilters;
import consulo.util.xml.serializer.XmlSerializer;
import com.jetbrains.lang.dart.ide.runner.server.DartCommandLineRunnerParameters;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DartRunConfigurationBase extends LocatableConfigurationBase
  implements RefactoringListenerProvider, DartRunConfiguration {

  protected DartRunConfigurationBase(final Project project, final ConfigurationFactory factory, final String name) {
    super(project, factory, name);
  }

  @Override
  public abstract @NotNull DartCommandLineRunnerParameters getRunnerParameters();

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    getRunnerParameters().check(getProject());
  }

  @Override
  public void writeExternal(final @NotNull Element element) throws WriteExternalException {
    super.writeExternal(element);
    XmlSerializer.serializeInto(getRunnerParameters(), element, new SkipDefaultValuesSerializationFilters());
  }

  @Override
  public void readExternal(final @NotNull Element element) throws InvalidDataException {
    super.readExternal(element);
    XmlSerializer.deserializeInto(getRunnerParameters(), element);
  }

  @Override
  public @Nullable RefactoringElementListener getRefactoringElementListener(final PsiElement element) {
    if (!(element instanceof PsiFileSystemItem)) return null;

    final String filePath = getRunnerParameters().getFilePath();
    final VirtualFile file = filePath == null ? null : ((PsiFileSystemItem)element).getVirtualFile();
    if (file == null) return null;

    final String affectedPath = file.getPath();
    if (element instanceof PsiFile) {
      if (filePath.equals(affectedPath)) {
        return new RenameRefactoringListener(affectedPath);
      }
    }
    if (element instanceof PsiDirectory) {
      if (filePath.startsWith(affectedPath + "/")) {
        return new RenameRefactoringListener(affectedPath);
      }
    }

    return null;
  }

  private final class RenameRefactoringListener extends UndoRefactoringElementAdapter {
    private @NotNull String myAffectedPath;

    private RenameRefactoringListener(final @NotNull String affectedPath) {
      myAffectedPath = affectedPath;
    }

    private String getNewPathAndUpdateAffectedPath(final @NotNull PsiElement newElement) {
      final String oldPath = getRunnerParameters().getFilePath();

      final VirtualFile newFile = newElement instanceof PsiFileSystemItem ? ((PsiFileSystemItem)newElement).getVirtualFile() : null;
      if (newFile != null && oldPath != null && oldPath.startsWith(myAffectedPath)) {
        final String newPath = newFile.getPath() + oldPath.substring(myAffectedPath.length());
        myAffectedPath = newFile.getPath(); // needed if refactoring will be undone
        return newPath;
      }

      return oldPath;
    }

    @Override
    protected void refactored(final @NotNull PsiElement element, final @Nullable String oldQualifiedName) {
      final boolean generatedName = getName().equals(suggestedName());
      final String filePath = getRunnerParameters().getFilePath();
      final boolean updateWorkingDir = filePath != null &&
                                       PathUtil.getParentPath(filePath).equals(getRunnerParameters().getWorkingDirectory());

      final String newPath = getNewPathAndUpdateAffectedPath(element);
      getRunnerParameters().setFilePath(newPath);

      if (updateWorkingDir) {
        getRunnerParameters().setWorkingDirectory(PathUtil.getParentPath(newPath));
      }

      if (generatedName) {
        setGeneratedName();
      }
    }
  }
}

