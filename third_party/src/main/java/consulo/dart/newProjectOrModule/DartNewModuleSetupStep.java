/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.dart.newProjectOrModule;

import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.projectWizard.DartProjectTemplate;
import consulo.application.Application;
import consulo.content.bundle.Sdk;
import consulo.content.bundle.SdkTable;
import consulo.dart.sdk.DartSdkType;
import consulo.disposer.Disposable;
import consulo.localize.LocalizeValue;
import consulo.module.creation.ui.UnifiedProjectOrModuleNameStep;
import consulo.module.ui.BundleBox;
import consulo.module.ui.BundleBoxBuilder;
import consulo.ui.CheckBox;
import consulo.ui.ComboBox;
import consulo.ui.UIAccess;
import consulo.ui.annotation.RequiredUIAccess;
import consulo.ui.model.FlatDataModel;
import consulo.ui.model.MutableFlatDataModel;
import consulo.ui.util.FormBuilder;
import consulo.util.lang.StringUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * SDK and sample content selection, as upstream {@code DartGeneratorPeer}: templates come from {@code dart create} of the selected SDK.
 */
public class DartNewModuleSetupStep extends UnifiedProjectOrModuleNameStep<DartNewModuleContext> {
  private @Nullable BundleBox myBundleBox;
  private @Nullable CheckBox myCreateSampleProjectCheckBox;
  private @Nullable ComboBox<DartProjectTemplate> myTemplatesBox;
  private final MutableFlatDataModel<DartProjectTemplate> myTemplates = FlatDataModel.of(List.of());
  private @Nullable String myTemplatesSdkPath;

  public DartNewModuleSetupStep(DartNewModuleContext context) {
    super(context);
  }

  @RequiredUIAccess
  @Override
  protected void extend(FormBuilder builder, Disposable uiDisposable) {
    super.extend(builder, uiDisposable);

    BundleBox bundleBox = BundleBoxBuilder.create(uiDisposable).withSdkTypeFilter(sdkTypeId -> sdkTypeId instanceof DartSdkType).build();
    builder.addLabeled(DartBundle.messageValue("dart.sdk.path.label"), bundleBox.getComponent());
    myBundleBox = bundleBox;

    CheckBox createSample = CheckBox.create(DartBundle.messageValue("dart.project.generate.sample.content.label"));
    createSample.setEnabled(false);
    builder.addBottom(createSample);
    myCreateSampleProjectCheckBox = createSample;

    ComboBox<DartProjectTemplate> templatesBox = ComboBox.create(myTemplates);
    templatesBox.setTextRenderer(template -> template == null ? LocalizeValue.empty() : LocalizeValue.of(getPresentableText(template)));
    templatesBox.setEnabled(false);
    builder.addBottom(templatesBox);
    myTemplatesBox = templatesBox;

    createSample.addValueListener(event -> templatesBox.setEnabled(Boolean.TRUE.equals(event.getValue()) && myTemplates.getSize() > 0));
    bundleBox.getComponent().addValueListener(event -> loadTemplates(getSelectedSdkHomePath()));

    ComboBox<BundleBox.BundleBoxItem> component = bundleBox.getComponent();
    if (component.getDataModel().getSize() > 0) {
      component.setValueByIndex(0);
    }
    loadTemplates(getSelectedSdkHomePath());
  }

  private static String getPresentableText(DartProjectTemplate template) {
    // same as upstream DartGeneratorPeer
    return template.getDescription().isEmpty()
           ? template.getName()
           : template.getName() + " - " + StringUtil.decapitalize(template.getDescription());
  }

  @RequiredUIAccess
  private void loadTemplates(@Nullable String sdkHomePath) {
    if (sdkHomePath == null || sdkHomePath.equals(myTemplatesSdkPath)) {
      return;
    }
    myTemplatesSdkPath = sdkHomePath;
    myTemplates.removeAll();
    if (myCreateSampleProjectCheckBox != null) myCreateSampleProjectCheckBox.setEnabled(false);
    if (myTemplatesBox != null) myTemplatesBox.setEnabled(false);

    UIAccess uiAccess = UIAccess.current();
    Application.get().executeOnPooledThread(() -> DartProjectTemplate.loadTemplatesAsync(sdkHomePath, templates -> uiAccess.give(() -> {
      if (!sdkHomePath.equals(myTemplatesSdkPath)) return;
      myTemplates.removeAll();
      for (DartProjectTemplate template : templates) {
        myTemplates.add(template);
      }
      if (myCreateSampleProjectCheckBox != null) {
        myCreateSampleProjectCheckBox.setEnabled(true);
        if (myTemplatesBox != null) myTemplatesBox.setEnabled(Boolean.TRUE.equals(myCreateSampleProjectCheckBox.getValue()));
      }
      if (myTemplatesBox != null && myTemplates.getSize() > 0) {
        myTemplatesBox.setValueByIndex(0);
      }
    })));
  }

  private @Nullable Sdk getSelectedSdk() {
    BundleBox bundleBox = myBundleBox;
    String selectedBundleName = bundleBox != null ? bundleBox.getSelectedBundleName() : null;
    return selectedBundleName != null ? SdkTable.getInstance().findSdk(selectedBundleName) : null;
  }

  private @Nullable String getSelectedSdkHomePath() {
    Sdk sdk = getSelectedSdk();
    return sdk == null ? null : sdk.getHomePath();
  }

  @Override
  public void onStepLeave(DartNewModuleContext context) {
    super.onStepLeave(context);

    context.setSdk(getSelectedSdk());
    boolean createSample = myCreateSampleProjectCheckBox != null && Boolean.TRUE.equals(myCreateSampleProjectCheckBox.getValue());
    context.setTemplate(createSample && myTemplatesBox != null ? myTemplatesBox.getValue() : null);
  }
}
