// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server.ui;

import consulo.html.language.HtmlFileType;
import consulo.language.editor.ui.TreeFileChooser;
import consulo.language.editor.ui.TreeFileChooserFactory;
import consulo.execution.configuration.ui.SettingsEditor;
import consulo.project.Project;
import consulo.ui.ex.awt.TextFieldWithBrowseButton;
import consulo.util.io.FileUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.language.psi.PsiFile;
import consulo.language.psi.PsiManager;
import consulo.ide.impl.idea.ui.PortField;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.runner.server.webdev.DartWebdevConfiguration;
import com.jetbrains.lang.dart.ide.runner.server.webdev.DartWebdevParameters;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class DartWebdevConfigurationEditorForm extends SettingsEditor<DartWebdevConfiguration> {
  private JPanel myMainPanel;
  private TextFieldWithBrowseButton myHtmlFileField;
  private PortField myWebdevPortField;

  public DartWebdevConfigurationEditorForm(final Project project) {
    myHtmlFileField.addActionListener(e -> {
      final String initialPath = FileUtil.toSystemIndependentName(myHtmlFileField.getText().trim());
      final VirtualFile initialFile = initialPath.isEmpty() ? null : LocalFileSystem.getInstance().findFileByPath(initialPath);
      final PsiFile initialPsiFile = initialFile == null ? null : PsiManager.getInstance(project).findFile(initialFile);

      final TreeFileChooser fileChooser = TreeFileChooserFactory.getInstance(project)
        .createFileChooser(DartBundle.message("choose.html.main.file"), initialPsiFile, HtmlFileType.INSTANCE, null, true, false);

      fileChooser.showDialog();

      final PsiFile selectedFile = fileChooser.getSelectedFile();
      final VirtualFile virtualFile = selectedFile == null ? null : selectedFile.getVirtualFile();
      if (virtualFile != null) {
        final String path = FileUtil.toSystemDependentName(virtualFile.getPath());
        myHtmlFileField.setText(path);
      }
    });
  }

  @Override
  protected void resetEditorFrom(final @NotNull DartWebdevConfiguration configuration) {
    final DartWebdevParameters parameters = configuration.getParameters();

    myHtmlFileField.setText(FileUtil.toSystemDependentName(parameters.getHtmlFilePath()));

    if (parameters.getWebdevPort() > 0) {
      myWebdevPortField.setNumber(parameters.getWebdevPort());
    }
  }

  @Override
  protected void applyEditorTo(final @NotNull DartWebdevConfiguration configuration) {
    final DartWebdevParameters parameters = configuration.getParameters();

    parameters.setHtmlFilePath(FileUtil.toSystemIndependentName(myHtmlFileField.getText().trim()));
    if (myWebdevPortField.getNumber() <= 0) {
      parameters.setWebdevPort(-1);
    }
    else {
      parameters.setWebdevPort(myWebdevPortField.getNumber());
    }
  }

  @Override
  protected @NotNull JComponent createEditor() {
    return myMainPanel;
  }
}
