// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.project.Project;
import consulo.ide.impl.idea.ui.HoverHyperlinkLabel;
import consulo.ui.ex.awt.HyperlinkAdapter;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.sdk.DartConfigurable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;

public class DartAnalysisServerSettingsForm {

  interface ServerSettingsListener {
    void settingsChanged();
  }

  private final Project myProject;

  private JPanel myMainPanel;
  private HoverHyperlinkLabel myDartSettingsHyperlink;
  private HoverHyperlinkLabel myAnalysisDiagnosticsHyperlink;

  DartAnalysisServerSettingsForm(final @NotNull Project project) {
    myProject = project;
  }

  private void createUIComponents() {
    myDartSettingsHyperlink = new HoverHyperlinkLabel(DartBundle.message("open.dart.plugin.settings"));
    myDartSettingsHyperlink.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      protected void hyperlinkActivated(final @NotNull HyperlinkEvent e) {
        DartConfigurable.openDartSettings(myProject);
      }
    });
    myAnalysisDiagnosticsHyperlink = new HoverHyperlinkLabel(DartBundle.message("analysis.server.settings.diagnostics"));
    myAnalysisDiagnosticsHyperlink.addHyperlinkListener(new HyperlinkAdapter() {
      @Override
      protected void hyperlinkActivated(final @NotNull HyperlinkEvent e) {
        new AnalysisServerDiagnosticsAction().run(myProject, null);
      }
    });
  }

  public JPanel getMainPanel() {
    return myMainPanel;
  }
}
