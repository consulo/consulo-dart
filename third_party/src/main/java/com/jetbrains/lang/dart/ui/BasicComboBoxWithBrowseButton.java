// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ui;

import consulo.application.Application;
import consulo.application.AllIcons;
import consulo.ui.ModalityState;
import consulo.application.ReadAction;
import consulo.fileChooser.FileChooser;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.project.Project;
import consulo.ui.ex.awt.ComboBox;

import consulo.util.io.FileUtil;
import com.intellij.ui.components.fields.ExtendableTextField;
import consulo.application.util.concurrent.AppExecutorUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicComboBoxWithBrowseButton<T> extends ComboBox<T> {
  private final ExtendableTextField myEditor = new ExtendableTextField();

  public BasicComboBoxWithBrowseButton() {
    setEditable(true);
    myEditor.setBorder(null);
    setEditor(new BasicComboBoxEditor() {
      @Override
      protected ExtendableTextField createEditorComponent() {
        return myEditor;
      }
    });
  }

  public <R> void addBrowseFolderListener(@Nullable String title,
                                          @Nullable Project project,
                                          @NotNull FileChooserDescriptor descriptor,
                                          @NotNull Function<String, R> backgroundResolver,
                                          @NotNull Consumer<R> edtConsumer) {
    myEditor.addExtension(
      ExtendableTextField.Extension.create(
        AllIcons.General.OpenDisk,
        AllIcons.General.OpenDiskHover,
        title,
        () -> {
          FileChooser.chooseFile(descriptor, project, this, null, file -> {
            String path = FileUtil.toSystemIndependentName(file.getPath());
            ReadAction.nonBlocking(() -> backgroundResolver.apply(path))
              .finishOnUiThread(Application::getAnyModalityState, edtConsumer)
              .submit(AppExecutorUtil.getAppExecutorService());
          });
        }));
  }

  @NotNull
  public ExtendableTextField getEditorComponent() {
    return myEditor;
  }
}
