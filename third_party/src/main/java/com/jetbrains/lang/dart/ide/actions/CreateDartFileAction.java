// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.actions;

import consulo.annotation.component.ActionImpl;
import consulo.annotation.component.ActionParentRef;
import consulo.annotation.component.ActionRef;
import consulo.annotation.component.ActionRefAnchor;
import consulo.ide.action.CreateFileFromTemplateAction;
import consulo.localize.LocalizeValue;
import consulo.ide.action.CreateFileFromTemplateDialog;
import consulo.ui.ex.action.ActionManager;
import consulo.language.editor.CommonDataKeys;
import consulo.dataContext.DataContext;
import consulo.project.Project;
import consulo.language.psi.PsiDirectory;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.sdk.DartSdk;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;

@ActionImpl(id = "Dart.NewDartFile",
  parents = @ActionParentRef(value = @ActionRef(id = "NewGroup"), anchor = ActionRefAnchor.BEFORE, relatedToAction = @ActionRef(id = "NewFromTemplate")))
public class CreateDartFileAction extends CreateFileFromTemplateAction {
  public CreateDartFileAction() {
    super(DartBundle.messageValue("action.Dart.NewDartFile.text"), DartBundle.messageValue("action.Dart.NewDartFile.description"), DartIcons.Dart_file);
  }

  @Override
  protected boolean isAvailable(@NotNull DataContext dataContext) {
    Project project = dataContext.getData(CommonDataKeys.PROJECT);
    return super.isAvailable(dataContext) && project != null && DartSdk.getDartSdk(project) != null;
  }

  @Override
  protected @NotNull LocalizeValue getActionName(PsiDirectory directory, @NotNull String newName, String templateName) {
    return DartBundle.messageValue("title.create.dart.file.0", newName);
  }

  @Override
  protected void buildDialog(@NotNull Project project,
                             @NotNull PsiDirectory directory,
                             @NotNull CreateFileFromTemplateDialog.Builder builder) {
    builder
      .setTitle(DartBundle.messageValue("new.dart.file.title"))
      .addKind(DartBundle.messageValue("list.item.dart.file"), DartIcons.Dart_file, "Dart File");

    // Notice that this is an odd place to hook into action execution but the more
    // expected `actionPerformed` is marked final in super.

    // TODO (pq): see if there's some way to find this action's place (w/o event data).
  }
}
