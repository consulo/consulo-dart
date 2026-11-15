package com.jetbrains.lang.dart.projectWizard;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.ProjectBuilder;
import com.intellij.ide.util.projectWizard.WizardContext;
import consulo.disposer.Disposable;
import consulo.configurable.ConfigurationException;
import consulo.disposer.Disposer;

import javax.swing.*;

public class DartModuleWizardStep extends ModuleWizardStep implements Disposable {
  private final WizardContext myContext;
  private final DartGeneratorPeer myPeer;

  public DartModuleWizardStep(final WizardContext context) {
    myContext = context;
    myPeer = new DartGeneratorPeer();
  }

  @Override
  public JComponent getComponent() {
    return myPeer.getMainPanel();
  }

  @Override
  public void updateDataModel() {
    final ProjectBuilder projectBuilder = myContext.getProjectBuilder();
    if (projectBuilder instanceof DartModuleBuilder) {
      ((DartModuleBuilder)projectBuilder).setWizardData(myPeer.getSettings());
    }
  }

  @Override
  public boolean validate() throws ConfigurationException {
    return myPeer.validateInIntelliJ();
  }

  @Override
  public void dispose() {
    Disposer.dispose(myPeer);
  }
}