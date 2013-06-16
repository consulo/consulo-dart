package com.jetbrains.lang.dart.ide.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ModifiableRootModel;
import org.consulo.module.extension.MutableModuleExtensionWithSdk;
import org.consulo.module.extension.MutableModuleInheritableNamedPointer;
import org.consulo.module.extension.ui.ModuleExtensionWithSdkPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author VISTALL
 * @since 17:50/08.06.13
 */
public class DartMutableModuleExtension extends DartModuleExtension implements MutableModuleExtensionWithSdk<DartModuleExtension> {
  @NotNull
  private final DartModuleExtension myModuleExtension;

  public DartMutableModuleExtension(@NotNull String id, @NotNull Module module, @NotNull DartModuleExtension moduleExtension) {
    super(id, module);
    myModuleExtension = moduleExtension;
    commit(moduleExtension);
  }

  @NotNull
  @Override
  public MutableModuleInheritableNamedPointer<Sdk> getInheritableSdk() {
    return (MutableModuleInheritableNamedPointer<Sdk>)super.getInheritableSdk();
  }

  @Nullable
  @Override
  public JComponent createConfigurablePanel(@NotNull ModifiableRootModel model, @Nullable Runnable runnable) {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new ModuleExtensionWithSdkPanel(this, runnable), BorderLayout.NORTH);
    return panel;
  }

  @Override
  public void setEnabled(boolean b) {
    myIsEnabled = b;
  }

  @Override
  public boolean isModified() {
    return isModifiedImpl(myModuleExtension);
  }

  @Override
  public void commit() {
    myModuleExtension.commit(this);
  }
}
