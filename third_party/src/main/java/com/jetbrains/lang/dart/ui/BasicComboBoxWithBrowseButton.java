// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ui;

import consulo.application.Application;
import consulo.application.ReadAction;
import consulo.application.util.concurrent.AppExecutorUtil;
import consulo.fileChooser.FileChooser;
import consulo.fileChooser.FileChooserDescriptor;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.project.Project;
import consulo.ui.ex.awt.ComboBox;
import consulo.ui.ex.awt.FixedSizeButton;
import consulo.ui.ex.awt.JBTextField;
import consulo.ui.ex.awtUnsafe.TargetAWT;
import consulo.util.io.FileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.plaf.basic.BasicComboBoxEditor;
import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class BasicComboBoxWithBrowseButton<T> extends ComboBox<T> {
  // Consulo: ExtendableTextField is not an API, the browse button is placed inside the editor text field instead
  private final JBTextField myEditor = new JBTextField();

  public BasicComboBoxWithBrowseButton() {
    setEditable(true);
    myEditor.setBorder(null);
    setEditor(new BasicComboBoxEditor() {
      @Override
      protected JBTextField createEditorComponent() {
        return myEditor;
      }
    });
  }

  public <R> void addBrowseFolderListener(@Nullable String title,
                                          @Nullable Project project,
                                          @NotNull FileChooserDescriptor descriptor,
                                          @NotNull Function<String, R> backgroundResolver,
                                          @NotNull Consumer<R> edtConsumer) {
    FixedSizeButton browseButton = new FixedSizeButton(myEditor);
    browseButton.setIcon(TargetAWT.to(PlatformIconGroup.nodesFolder()));
    browseButton.setToolTipText(title);
    browseButton.addActionListener(e -> FileChooser.chooseFile(descriptor, this, project, null).thenAccept(file -> {
      if (file == null) return;
      String path = FileUtil.toSystemIndependentName(file.getPath());
      ReadAction.nonBlocking(() -> backgroundResolver.apply(path))
        .finishOnUiThread(Application::getAnyModalityState, edtConsumer)
        .submit(AppExecutorUtil.getAppExecutorService());
    }));

    myEditor.setLayout(new BorderLayout());
    myEditor.add(browseButton, BorderLayout.EAST);
  }

  public @NotNull JBTextField getEditorComponent() {
    return myEditor;
  }
}
