// Copyright 2000-2021 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.jetbrains.lang.dart.ide.errorTreeView;

import consulo.navigation.OpenFileDescriptorFactory;
import consulo.platform.base.icon.PlatformIconGroup;
import consulo.localize.LocalizeValue;
import consulo.ui.image.OverlayPosition;
import consulo.ui.image.ImageEffects;
import consulo.ui.image.Image;
import consulo.dataContext.DataSink;
import consulo.dataContext.DataContext;
import consulo.ui.ex.action.AnActionWithSyncUpdate;
import com.google.common.annotations.VisibleForTesting;
import consulo.application.AllIcons;
import consulo.webBrowser.BrowserUtil;
import consulo.ui.ex.CopyProvider;
import consulo.language.psi.PsiNavigationSupport;
import consulo.ui.ex.action.ActionManager;
import consulo.ui.ex.action.ActionPlaces;
import consulo.ui.ex.action.ActionPopupMenu;
import consulo.ui.ex.action.ActionToolbar;
import consulo.ui.ex.action.AnAction;
import consulo.ui.ex.action.AnActionEvent;
import consulo.language.editor.CommonDataKeys;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.ex.action.IdeActions;
import consulo.language.editor.PlatformDataKeys;
import consulo.ui.ex.action.Toggleable;
import consulo.application.WriteAction;
import consulo.codeEditor.ScrollType;
import consulo.navigation.OpenFileDescriptor;
import consulo.ui.ex.awt.CopyPasteManager;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.ui.ex.action.DumbAwareToggleAction;
import consulo.project.Project;
import consulo.ui.ex.awt.SimpleToolWindowPanel;
import consulo.ui.ex.popup.JBPopupFactory;

import consulo.util.lang.StringUtil;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.VirtualFile;
import consulo.navigation.Navigatable;
import consulo.ui.ex.awt.AutoScrollToSourceHandler;
import consulo.ui.ex.awt.PopupHandler;
import consulo.ui.ex.awt.ScrollPaneFactory;
import consulo.ui.ex.awt.speedSearch.TableSpeedSearch;
import consulo.ui.ex.RelativePoint;
import consulo.ui.ex.awt.JBUIScale;
import consulo.ui.ex.awt.table.TableView;
import consulo.ui.ex.awt.EditSourceOnDoubleClickHandler;
import consulo.ui.ex.awt.tree.EditSourceOnEnterKeyHandler;
import consulo.ui.ex.OpenSourceUtil;
import consulo.util.collection.SmartList;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.analyzer.DartFileInfo;
import com.jetbrains.lang.dart.analyzer.DartFileInfoKt;
import com.jetbrains.lang.dart.assists.AssistUtils;
import com.jetbrains.lang.dart.assists.DartSourceEditException;
import icons.DartIcons;
import org.dartlang.analysis.server.protocol.AnalysisErrorFixes;
import org.dartlang.analysis.server.protocol.DiagnosticMessage;
import org.dartlang.analysis.server.protocol.Location;
import org.dartlang.analysis.server.protocol.SourceChange;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DartProblemsViewPanel extends SimpleToolWindowPanel implements CopyProvider {
  private static final String ID_PREFIX = "dart.problems.view";

  public static final String FILTER_PROBLEMS_ACTION_ID = ID_PREFIX + ".filterProblems";
  public static final String JUMP_TO_SOURCE_ACTION_ID = ID_PREFIX + ".jumpToSource";
  public static final String OPEN_DOCUMENTATION_ACTION_ID = ID_PREFIX + ".openDocumentation";
  public static final String OPEN_SERVER_SETTINGS_ACTION_ID = ID_PREFIX + ".openServerSettings";

  private static final Image DART_ERRORS_ICON;
  private static final Image DART_WARNINGS_ICON;

  static {
    // Consulo: LayeredIcon is replaced by ImageEffects.overlay
    DART_ERRORS_ICON = ImageEffects.overlay(DartIcons.Dart_13, AllIcons.Ide.ErrorPoint, OverlayPosition.BOTTOM_LEFT);
    DART_WARNINGS_ICON = ImageEffects.overlay(DartIcons.Dart_13, DartIcons.Warning_point, OverlayPosition.BOTTOM_LEFT);
  }

  private final @NotNull Project myProject;
  private final @NotNull TableView<DartProblem> myTable;

  private final @NotNull DartProblemsPresentationHelper myPresentationHelper;

  @VisibleForTesting
  public DartProblemsViewPanel(@NotNull Project project,
                               @NotNull DartProblemsPresentationHelper presentationHelper) {
    super(false, true);
    myProject = project;
    myPresentationHelper = presentationHelper;

    myTable = createTable();
    setToolbar(createToolbar());
    setContent(createCenterPanel());
  }

  private @NotNull TableView<DartProblem> createTable() {
    TableView<DartProblem> table = new TableView<>(new DartProblemsTableModel(myPresentationHelper));

    EditSourceOnDoubleClickHandler.install(table);
    EditSourceOnEnterKeyHandler.install(table, null);

    table.addMouseListener(new PopupHandler() {
      @Override
      public void invokePopup(Component comp, int x, int y) {
        popupInvoked(comp, x, y);
      }
    });

    //noinspection unchecked
    ((DefaultRowSorter<DartProblemsTableModel, Integer>)table.getRowSorter()).setRowFilter(myPresentationHelper.getRowFilter());

    table.getRowSorter().addRowSorterListener(e -> {
      List<? extends RowSorter.SortKey> sortKeys = myTable.getRowSorter().getSortKeys();
      assert sortKeys.size() == 1 : sortKeys;
      ((DartProblemsTableModel)myTable.getModel()).setSortKey(sortKeys.getFirst());
    });

    new TableSpeedSearch(table, object -> object instanceof DartProblem
                                          ? ((DartProblem)object).getErrorMessage() + " " + ((DartProblem)object).getPresentableLocation()
                                          : "");

    table.setShowGrid(false);
    table.setRowHeight(table.getRowHeight() + JBUIScale.scale(4));

    JTableHeader tableHeader = table.getTableHeader();
    tableHeader.setPreferredSize(new Dimension(0, table.getRowHeight()));

    return table;
  }

  private void popupInvoked(Component component, int x, int y) {
    DefaultActionGroup group = new DefaultActionGroup();
    if (myTable.getSelectedObject() != null) {
      group.add(ActionManager.getInstance().getAction(IdeActions.ACTION_EDIT_SOURCE));
    }

    group.add(ActionManager.getInstance().getAction(IdeActions.ACTION_COPY));

    List<DartProblem> selectedProblems = myTable.getSelectedObjects();
    DartProblem selectedProblem = selectedProblems.size() == 1 ? selectedProblems.getFirst() : null;

    addQuickFixActions(group, selectedProblem);
    addDiagnosticMessageActions(group, selectedProblem);
    addDocumentationAction(group, selectedProblem);

    ActionPopupMenu menu = ActionManager.getInstance().createActionPopupMenu(ActionPlaces.TOOLBAR, group);
    menu.getComponent().show(component, x, y);
  }

  private void addQuickFixActions(@NotNull DefaultActionGroup group, @Nullable DartProblem problem) {
    VirtualFile selectedVFile = problem != null ? problem.getFile() : null;
    if (selectedVFile == null) return;

    List<SourceChange> selectedProblemSourceChangeFixes = new SmartList<>();
    DartAnalysisServerService.getInstance(myProject)
      .askForFixesAndWaitABitIfReceivedQuickly(selectedVFile, problem.getOffset(), fixes -> {
        for (AnalysisErrorFixes fix : fixes) {
          if (fix.getError().getCode().equals(problem.getCode())) {
            selectedProblemSourceChangeFixes.addAll(fix.getFixes());
          }
        }
      });

    if (selectedProblemSourceChangeFixes.isEmpty()) return;

    group.addSeparator();

    for (SourceChange sourceChangeFix : selectedProblemSourceChangeFixes) {
      if (sourceChangeFix == null) continue;
      String fixMessage = sourceChangeFix.getMessage();
      group.add(new AnAction(fixMessage, null, AllIcons.Actions.QuickfixBulb) {

        @Override
        public void actionPerformed(@NotNull AnActionEvent event) {
          OpenSourceUtil.navigate(PsiNavigationSupport.getInstance().createNavigatable(myProject, selectedVFile, problem.getOffset()));
          try {
            WriteAction.run(() -> AssistUtils.applySourceChange(myProject, sourceChangeFix, true));
          }
          catch (DartSourceEditException ignored) {/**/}

        }
      });
    }
  }

  private void addDiagnosticMessageActions(@NotNull DefaultActionGroup group, @Nullable DartProblem problem) {
    List<DiagnosticMessage> diagnosticMessages = problem != null ? problem.getContextMessages() : null;
    if (diagnosticMessages == null || diagnosticMessages.isEmpty()) return;

    group.addSeparator();
    // Reference the icon for "Jump to Source", higher in this menu group, to indicate that the action will have the same behavior
    Image jumpToSourceIcon = ActionManager.getInstance().getAction(IdeActions.ACTION_EDIT_SOURCE).getTemplatePresentation().getIcon();
    for (DiagnosticMessage diagnosticMessage : diagnosticMessages) {
      // Reference the message, trim, non-nullize, and remove a trailing period, if one exists
      String message = StringUtil.notNullize(diagnosticMessage.getMessage());
      message = StringUtil.trimEnd(StringUtil.trim(message), ".");

      // Reference the Location, compute the VirtualFile
      Location location = diagnosticMessage.getLocation();
      String filePathOrUri = location.getFile();
      DartFileInfo fileInfo = DartFileInfoKt.getDartFileInfo(myProject, filePathOrUri);
      VirtualFile vFile = fileInfo.findFile();

      // Create the action for this DiagnosticMessage
      if (StringUtil.isNotEmpty(message) && vFile != null) {
        group.add(new DumbAwareAction(LocalizeValue.of(message), LocalizeValue.empty(), jumpToSourceIcon) {

          @Override
          public void actionPerformed(@NotNull AnActionEvent e) {
            int offset = DartAnalysisServerService.getInstance(myProject).getConvertedOffset(vFile, location.getOffset());
            OpenSourceUtil.navigate(PsiNavigationSupport.getInstance().createNavigatable(myProject, vFile, offset));
          }
        });
      }
    }
  }

  private static void addDocumentationAction(@NotNull DefaultActionGroup group, @Nullable DartProblem problem) {
    String url = problem != null ? problem.getUrl() : null;
    if (url == null) return;

    group.addSeparator();
    group.add(new DumbAwareAction(DartBundle.messagePointer("action.DartProblemsViewPanel.open.documentation.text"),
                                  DartBundle.messagePointer("action.DartProblemsViewPanel.open.documentation.description"),
                                  PlatformIconGroup.ideExternallink()) {

      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        BrowserUtil.browse(url);
      }
    });
  }

  private @NotNull JComponent createToolbar() {
    DefaultActionGroup group = new DefaultActionGroup();

    addReanalyzeActions(group);
    group.addAction(new AnalysisServerSettingsAction());
    group.addSeparator();

    addAutoScrollToSourceAction(group);
    addGroupBySeverityAction(group);
    group.addAction(new FilterProblemsAction());
    group.addSeparator();

    ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("DartProblemsView", group, false);
    toolbar.setTargetComponent(myTable);
    return toolbar.getComponent();
  }

  private @NotNull JPanel createCenterPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(ScrollPaneFactory.createScrollPane(myTable), BorderLayout.CENTER);
    return panel;
  }

  private void updateStatusDescription() {
    DartProblemsTableModel model = (DartProblemsTableModel)myTable.getModel();
    DartProblemsView problemsView = DartProblemsView.getInstance(myProject);
    problemsView.setTabTitle(model.getTabTitleText());
    problemsView.setToolWindowIcon(model.hasErrors() ? DART_ERRORS_ICON : model.hasWarnings() ? DART_WARNINGS_ICON : DartIcons.Dart_13);
  }

  private static void addReanalyzeActions(@NotNull DefaultActionGroup group) {
    AnAction restartAction = ActionManager.getInstance().getAction("Dart.Restart.Analysis.Server");
    if (restartAction != null) {
      group.add(restartAction);
    }
  }

  private void addAutoScrollToSourceAction(@NotNull DefaultActionGroup group) {
    AutoScrollToSourceHandler autoScrollToSourceHandler = new AutoScrollToSourceHandler() {
      @Override
      protected boolean isAutoScrollMode() {
        return myPresentationHelper.isAutoScrollToSource();
      }

      @Override
      protected void setAutoScrollMode(boolean autoScrollToSource) {
        myPresentationHelper.setAutoScrollToSource(autoScrollToSource);
      }
    };

    autoScrollToSourceHandler.install(myTable);
    group.addAction(autoScrollToSourceHandler.createToggleAction());
  }

  private void addGroupBySeverityAction(@NotNull DefaultActionGroup group) {
    AnAction action = new DumbAwareToggleAction(DartBundle.message("group.by.severity"),
                                                DartBundle.message("group.by.severity.description"),
                                                AllIcons.Nodes.SortBySeverity) {

      @Override
      public boolean isSelected(@NotNull AnActionEvent e) {
        return myPresentationHelper.isGroupBySeverity();
      }

      @Override
      public void setSelected(@NotNull AnActionEvent e, boolean groupBySeverity) {
        myPresentationHelper.setGroupBySeverity(groupBySeverity);
        fireGroupingOrFilterChanged();
      }
    };

    group.addAction(action);
  }

  void fireGroupingOrFilterChanged() {
    myTable.getRowSorter().allRowsChanged();
    ((DartProblemsTableModel)myTable.getModel()).onFilterChanged();
    updateStatusDescription();
  }

  private void showFiltersPopup() {
    DartProblemsFilterForm filterForm = new DartProblemsFilterForm();
    filterForm.reset(myPresentationHelper);
    filterForm.addListener(new DartProblemsFilterForm.FilterListener() {
      @Override
      public void filtersChanged() {
        myPresentationHelper.updateFromFilterSettingsUI(filterForm);
        fireGroupingOrFilterChanged();
      }

      @Override
      public void filtersResetRequested() {
        myPresentationHelper.resetAllFilters();
        filterForm.reset(myPresentationHelper);
        fireGroupingOrFilterChanged();
      }
    });

    createAndShowPopup(DartBundle.message("dart.problems.view.popup.title.dart.problems.filter"), filterForm.getMainPanel());
  }

  private void showAnalysisServerSettingsPopup() {
    DartAnalysisServerSettingsForm serverSettingsForm = new DartAnalysisServerSettingsForm(myProject);
    createAndShowPopup(DartBundle.message("dart.problems.view.popup.title.analysis.server.settings"), serverSettingsForm.getMainPanel());
  }

  private void createAndShowPopup(@NotNull String title, @NotNull JPanel jPanel) {
    Rectangle visibleRect = myTable.getVisibleRect();
    Point tableTopLeft = new Point(myTable.getLocationOnScreen().x + visibleRect.x, myTable.getLocationOnScreen().y + visibleRect.y);

    JBPopupFactory.getInstance()
      .createComponentPopupBuilder(jPanel, null)
      .setProject(myProject)
      .setTitle(title)
      .setMovable(true)
      .setRequestFocus(true)
      .createPopup().show(RelativePoint.fromScreen(tableTopLeft));
  }


  @Override
  public boolean isCopyVisible(@NotNull DataContext dataContext) {
    return true;
  }

  @Override
  public boolean isCopyEnabled(@NotNull DataContext dataContext) {
    return myTable.getSelectedObject() != null;
  }

  @Override
  public void performCopy(@NotNull DataContext dataContext) {
    List<DartProblem> selectedObjects = myTable.getSelectedObjects();
    String s = StringUtil.join(selectedObjects, problem -> StringUtil.toLowerCase(problem.getSeverity()) +
                                                           ": " +
                                                           problem.getErrorMessage() +
                                                           " (" +
                                                           problem.getCode() + " at " + problem.getPresentableLocation() +
                                                           ")", "\n");

    if (!s.isEmpty()) {
      CopyPasteManager.getInstance().setContents(new StringSelection(s));
    }
  }

  @Override
  public void uiDataSnapshot(@NotNull DataSink sink) {
    super.uiDataSnapshot(sink);
    sink.set(PlatformDataKeys.COPY_PROVIDER, this);
    sink.lazy(CommonDataKeys.NAVIGATABLE, this::createNavigatable);
  }

  private @Nullable Navigatable createNavigatable() {
    DartProblem problem = myTable.getSelectedObject();
    if (problem != null) {
      VirtualFile file = LocalFileSystem.getInstance().findFileByPath(problem.getSystemIndependentPath());
      if (file != null) {
        // Consulo: no scroll type in OpenFileDescriptor, navigation scrolls to make the offset visible
        return OpenFileDescriptorFactory.getInstance(myProject).newBuilder(file).offset(problem.getOffset()).build();
      }
    }

    return null;
  }

  public void setErrors(@NotNull Map<String, List<DartProblem>> filePathToDartProblems) {
    DartProblemsTableModel model = (DartProblemsTableModel)myTable.getModel();
    DartProblem oldSelectedProblem = myTable.getSelectedObject();

    DartProblem updatedSelectedProblem = model.setProblemsAndReturnReplacementForSelection(filePathToDartProblems, oldSelectedProblem);

    if (updatedSelectedProblem != null) {
      myTable.setSelection(Collections.singletonList(updatedSelectedProblem));
    }

    updateStatusDescription();
  }

  void clearAll() {
    ((DartProblemsTableModel)myTable.getModel()).removeAll();
    updateStatusDescription();
  }

  @VisibleForTesting
  public int getRowCount() {
    return myTable.getRowCount();
  }

  private class FilterProblemsAction extends DumbAwareAction implements Toggleable, AnActionWithSyncUpdate {
    FilterProblemsAction() {
      super(DartBundle.messagePointer("dart.problems.view.action.name.filter.problems"),
            DartBundle.messagePointer("dart.problems.view.action.description.filter.problems"),
            AllIcons.General.Filter);
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
      // show icon as toggled on if any filter is active
      Toggleable.setSelected(e.getPresentation(), myPresentationHelper.areFiltersApplied());
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      showFiltersPopup();
    }
  }

  private class AnalysisServerSettingsAction extends DumbAwareAction implements Toggleable, AnActionWithSyncUpdate {
    AnalysisServerSettingsAction() {
      //noinspection DialogTitleCapitalization
      super(DartBundle.messagePointer("dart.problems.view.action.name.analysis.server.settings"),
            DartBundle.messagePointer("dart.problems.view.action.description.analysis.server.settings"),
            AllIcons.General.GearPlain);
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
      showAnalysisServerSettingsPopup();
    }
  }
}