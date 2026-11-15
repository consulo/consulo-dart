// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.generation;

import consulo.ide.impl.idea.ide.util.MemberChooser;
import consulo.language.editor.action.LanguageCodeInsightActionHandler;
import consulo.application.ApplicationManager;
import consulo.undoRedo.CommandProcessor;
import consulo.logging.Logger;
import consulo.codeEditor.Editor;
import consulo.project.Project;
import consulo.ui.ex.awt.DialogWrapper;
import consulo.util.lang.function.Condition;

import consulo.util.lang.Pair;
import consulo.language.psi.PsiFile;
import consulo.language.psi.util.PsiTreeUtil;
import consulo.language.util.IncorrectOperationException;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.DartComponentType;
import com.jetbrains.lang.dart.ide.DartNamedElementNode;
import com.jetbrains.lang.dart.psi.DartClass;
import com.jetbrains.lang.dart.psi.DartClassDefinition;
import com.jetbrains.lang.dart.psi.DartComponent;
import com.jetbrains.lang.dart.psi.DartFile;
import com.jetbrains.lang.dart.util.DartResolveUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class BaseDartGenerateHandler implements LanguageCodeInsightActionHandler {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(BaseDartGenerateHandler.class);

  @Override
  public boolean isValidFor(final @NotNull Editor editor, final @NotNull PsiFile file) {
    return file instanceof DartFile &&
           PsiTreeUtil.getParentOfType(file.findElementAt(editor.getCaretModel().getOffset()), DartClass.class) != null;
  }

  @Override
  public void invoke(final @NotNull Project project, final @NotNull Editor editor, final @NotNull PsiFile psiFile) {
    invoke(project, editor, psiFile, editor.getCaretModel().getOffset());
  }

  public void invoke(final @NotNull Project project, final @NotNull Editor editor, final @NotNull PsiFile file, final int offset) {
    final DartClass dartClass = PsiTreeUtil.getParentOfType(file.findElementAt(offset), DartClassDefinition.class);
    if (dartClass == null) return;

    final List<DartComponent> candidates = new ArrayList<>();
    collectCandidates(dartClass, candidates);

    List<DartNamedElementNode> selectedElements = Collections.emptyList();
    if (ApplicationManager.getApplication().isUnitTestMode()) {
      selectedElements = ContainerUtil.map(candidates, DartNamedElementNode::new);
    }
    else if (!candidates.isEmpty()) {
      final MemberChooser<DartNamedElementNode> chooser = createMemberChooserDialog(project, dartClass, candidates, getTitle());
      chooser.show();
      if (chooser.getExitCode() != DialogWrapper.OK_EXIT_CODE) return;

      selectedElements = chooser.getSelectedElements();
    }

    doInvoke(project, editor, file, selectedElements, createFix(dartClass));
  }

  protected void doInvoke(final @NotNull Project project,
                          final @NotNull Editor editor,
                          final @NotNull PsiFile file,
                          final @NotNull Collection<DartNamedElementNode> selectedElements,
                          final @NotNull BaseCreateMethodsFix createMethodsFix) {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        createMethodsFix.addElementsToProcessFrom(selectedElements);
        createMethodsFix.beforeInvoke(project, editor, file);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
          @Override
          public void run() {
            try {
              createMethodsFix.invoke(project, editor, file);
            }
            catch (IncorrectOperationException ex) {
              LOG.error(ex);
            }
          }
        });
      }
    };

    if (CommandProcessor.getInstance().getCurrentCommand() == null) {
      CommandProcessor.getInstance().executeCommand(project, runnable, createMethodsFix.getCommandName(), null);
    }
    else {
      runnable.run();
    }
  }

  protected abstract @NotNull BaseCreateMethodsFix createFix(final @NotNull DartClass dartClass);

  protected abstract @NotNull String getTitle();

  protected abstract void collectCandidates(final @NotNull DartClass dartClass, final @NotNull List<DartComponent> candidates);

  private static final Condition<DartComponent> NOT_CONSTRUCTOR_CONDITION =
    component -> DartComponentType.typeOf(component) != DartComponentType.CONSTRUCTOR;

  private static final Condition<DartComponent> NOT_STATIC_CONDITION = component -> !component.isStatic();

  protected final @NotNull Map<Pair<String, Boolean>, DartComponent> computeClassMembersMap(final @NotNull DartClass dartClass,
                                                                                            final boolean doIncludeStatics) {
    List<DartComponent> classMembers = DartResolveUtil.getNamedSubComponents(dartClass);
    classMembers = ContainerUtil.filter(classMembers, NOT_CONSTRUCTOR_CONDITION);
    if (!doIncludeStatics) {
      classMembers = ContainerUtil.filter(classMembers, NOT_STATIC_CONDITION);
    }
    return DartResolveUtil.namedComponentToMap(classMembers);
  }

  protected final @NotNull Map<Pair<String, Boolean>, DartComponent> computeSuperClassesMemberMap(final @NotNull DartClass dartClass) {
    final List<DartClass> superClasses = new ArrayList<>();
    final List<DartClass> superInterfaces = new ArrayList<>();

    DartResolveUtil.collectSupers(superClasses, superInterfaces, dartClass);

    List<DartComponent> superClassesMembers = new ArrayList<>();
    for (DartClass superClass : superClasses) {
      superClassesMembers.addAll(DartResolveUtil.getNamedSubComponents(superClass));
    }

    superClassesMembers = ContainerUtil.filter(superClassesMembers, NOT_CONSTRUCTOR_CONDITION);
    superClassesMembers = ContainerUtil.filter(superClassesMembers, NOT_STATIC_CONDITION);

    return DartResolveUtil.namedComponentToMap(superClassesMembers);
  }

  protected final @NotNull Map<Pair<String, Boolean>, DartComponent> computeSuperInterfacesMembersMap(final @NotNull DartClass dartClass) {
    final List<DartClass> superClasses = new ArrayList<>();
    final List<DartClass> superInterfaces = new ArrayList<>();

    DartResolveUtil.collectSupers(superClasses, superInterfaces, dartClass);

    List<DartComponent> superInterfacesMembers = new ArrayList<>();
    for (DartClass superInterface : superInterfaces) {
      superInterfacesMembers.addAll(DartResolveUtil.getNamedSubComponents(superInterface));
    }

    superInterfacesMembers = ContainerUtil.filter(superInterfacesMembers, NOT_CONSTRUCTOR_CONDITION);
    superInterfacesMembers = ContainerUtil.filter(superInterfacesMembers, NOT_STATIC_CONDITION);

    return DartResolveUtil.namedComponentToMap(superInterfacesMembers);
  }

  protected @Nullable JComponent getOptionsComponent(DartClass jsClass, final Collection<DartComponent> candidates) {
    return null;
  }

  protected MemberChooser<DartNamedElementNode> createMemberChooserDialog(final @NotNull Project project,
                                                                          final @NotNull DartClass dartClass,
                                                                          final @NotNull Collection<DartComponent> candidates,
                                                                          @NotNull String title) {
    final List<DartNamedElementNode> nodes = ContainerUtil.map(candidates, DartNamedElementNode::new);
    final MemberChooser<DartNamedElementNode> chooser =
      new MemberChooser<>(nodes.toArray(new DartNamedElementNode[0]), doAllowEmptySelection(), true, project, false) {

        @Override
        protected JComponent createCenterPanel() {
          final JComponent superComponent = super.createCenterPanel();
          final JComponent optionsComponent = getOptionsComponent(dartClass, candidates);
          if (optionsComponent == null) {
            return superComponent;
          }
          else {
            final JPanel panel = new JPanel(new BorderLayout());
            panel.add(superComponent, BorderLayout.CENTER);
            panel.add(optionsComponent, BorderLayout.SOUTH);
            return panel;
          }
        }
      };

    chooser.setTitle(title);
    chooser.setCopyJavadocVisible(false);
    return chooser;
  }

  protected boolean doAllowEmptySelection() {
    return false;
  }
}
