// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart;

import consulo.annotation.component.ExtensionImpl;
import consulo.application.ApplicationManager;
import consulo.ui.ModalityState;
import consulo.application.ReadAction;
import consulo.application.WriteAction;
import consulo.logging.Logger;
import consulo.module.Module;
import consulo.module.ModuleManager;
import consulo.language.util.ModuleUtilCore;
import consulo.application.progress.ProgressManager;
import consulo.project.Project;
import consulo.project.ProjectManager;
import consulo.module.content.layer.orderEntry.LibraryOrderEntry;
import consulo.module.content.layer.ModifiableRootModel;
import consulo.module.content.ModuleRootManager;
import consulo.module.content.layer.orderEntry.OrderEntry;
import consulo.content.OrderRootType;
import consulo.module.content.ProjectFileIndex;
import consulo.module.content.ProjectRootManager;
import consulo.content.internal.LibraryEx;
import consulo.content.library.Library;
import consulo.content.library.LibraryProperties;
import consulo.content.library.LibraryTable;
import consulo.content.library.LibraryTablesRegistrar;
import consulo.util.lang.function.Condition;
import consulo.application.util.registry.Registry;
import consulo.language.spellchecker.editor.impl.Strings;
import consulo.virtualFileSystem.event.AsyncFileListener;
import consulo.virtualFileSystem.LocalFileSystem;
import consulo.virtualFileSystem.util.VirtualFileUtil;
import consulo.virtualFileSystem.VirtualFile;
import consulo.virtualFileSystem.event.VFileEvent;
import consulo.virtualFileSystem.event.VFileMoveEvent;
import consulo.virtualFileSystem.event.VFilePropertyChangeEvent;
import consulo.language.psi.search.FilenameIndex;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.util.io.PathUtil;
import consulo.util.collection.SmartList;
import consulo.application.util.concurrent.AppExecutorUtil;
import consulo.util.collection.ContainerUtil;
import com.jetbrains.lang.dart.analyzer.DartAnalysisServerService;
import com.jetbrains.lang.dart.logging.PluginLogger;
import com.jetbrains.lang.dart.sdk.DartPackagesLibraryProperties;
import com.jetbrains.lang.dart.sdk.DartPackagesLibraryType;
import com.jetbrains.lang.dart.sdk.DartSdk;
import com.jetbrains.lang.dart.sdk.DartSdkLibUtil;
import com.jetbrains.lang.dart.util.PackageConfigFileUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.jetbrains.lang.dart.util.PubspecYamlUtil.PUBSPEC_YAML;

/**
 * {@link DartFileListener} helps to keep "Dart Packages" library (based on Dart-specific pubspec.yaml and package_config.json files) up-to-date.
 * Also, it makes sure that Dart build output folder and related folders with Pub-specific cache are excluded.
 * Also, it updates the list of files visible for Analysis Server in case of move and rename.
 *
 * @see DartStartupActivity
 * @see DartWorkspaceModelChangeListener
 */
@ExtensionImpl
public final class DartFileListener implements AsyncFileListener {
  private static final Logger LOG = PluginLogger.INSTANCE.createLogger(DartFileListener.class);
  private static final Object DART_PACKAGE_ROOTS_UPDATE_COALESCE = new Object();

  @Override
  public @Nullable ChangeApplier prepareChange(@NotNull List<? extends @NotNull VFileEvent> events) {
    SmartList<VFileEvent> packagesFileEvents = new SmartList<>();
    SmartList<VFileEvent> moveOrRenameAnalyzableFileEvents = new SmartList<>();

    for (VFileEvent event : events) {
      if (event.getFileSystem() != LocalFileSystem.getInstance() && !ApplicationManager.getApplication().isUnitTestMode()) continue;

      if (event instanceof VFilePropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.isRename()) {
          if (PackageConfigFileUtil.PACKAGE_CONFIG_JSON.equals(propertyChangeEvent.getOldValue()) ||
              PackageConfigFileUtil.PACKAGE_CONFIG_JSON.equals(propertyChangeEvent.getNewValue())) {
            packagesFileEvents.add(propertyChangeEvent);
          }

          if ((propertyChangeEvent.getOldValue() instanceof String oldValue && DartAnalysisServerService.isFileNameRespectedByAnalysisServer(oldValue)) ||
                  (propertyChangeEvent.getNewValue() instanceof String newValue && DartAnalysisServerService.isFileNameRespectedByAnalysisServer(newValue))) {
            moveOrRenameAnalyzableFileEvents.add(propertyChangeEvent);
          }
        }
      }
      else {
        if (PackageConfigFileUtil.PACKAGE_CONFIG_JSON.equals(PathUtil.getFileName(event.getPath()))) {
          packagesFileEvents.add(event);
        }

        if (event instanceof VFileMoveEvent &&
            DartAnalysisServerService.isFileNameRespectedByAnalysisServer(PathUtil.getFileName(event.getPath()))) {
          moveOrRenameAnalyzableFileEvents.add(event);
        }
      }
    }

    if (packagesFileEvents.isEmpty() && moveOrRenameAnalyzableFileEvents.isEmpty()) {
      return null;
    }

    return new DartFileChangeApplier(packagesFileEvents, moveOrRenameAnalyzableFileEvents);
  }

  public static void scheduleDartPackageRootsUpdate(final @NotNull Project project) {
    if (Registry.is("dart.projects.without.pubspec", false)) return;
    if (!project.isInitialized()) {
      LOG.error("Method is called too early for project " + project);
      return;
    }

    ReadAction.nonBlocking(() -> collectPackagesLibraryRoots(project))
      .coalesceBy(DART_PACKAGE_ROOTS_UPDATE_COALESCE)
      .expireWith(DartAnalysisServerService.getInstance(project))
      .finishOnUiThread(ModalityState.nonModal(), libInfo -> {
        if (libInfo.getLibRootUrls().isEmpty()) {
          removeDartPackagesLibraryAndDependencies(project);
          return;
        }

        Library library = updatePackagesLibraryRoots(project, libInfo);
        Condition<Module> moduleFilter = DartSdkLibUtil::isDartSdkEnabled;
        updateDependenciesOnDartPackagesLibrary(project, moduleFilter, library);
      })
      .submit(AppExecutorUtil.getAppExecutorService());
  }

  private static @NotNull DartLibInfo collectPackagesLibraryRoots(final @NotNull Project project) {
    final DartLibInfo libInfo = new DartLibInfo();

    final Collection<VirtualFile> pubspecYamlFiles =
      FilenameIndex.getVirtualFilesByName(PUBSPEC_YAML, GlobalSearchScope.projectScope(project));
    final ProjectFileIndex fileIndex = ProjectRootManager.getInstance(project).getFileIndex();

    for (VirtualFile pubspecFile : pubspecYamlFiles) {
      ProgressManager.checkCanceled();
      final Module module = fileIndex.getModuleForFile(pubspecFile);
      if (module == null || !DartSdkLibUtil.isDartSdkEnabled(module)) continue;

      Map<String, String> packagesMap = null;
      VirtualFile packagesFile = PackageConfigFileUtil.findPackageConfigJsonFile(pubspecFile.getParent());
      if (packagesFile != null) {
        packagesMap = PackageConfigFileUtil.getPackagesMapFromPackageConfigJsonFile(packagesFile);
      }

      if (packagesMap != null) {
        for (Map.Entry<String, String> entry : packagesMap.entrySet()) {
          final String packageName = entry.getKey();
          final String packagePath = entry.getValue();
          if (isPathOutsideProjectContent(fileIndex, packagePath)) {
            libInfo.addPackage(packageName, packagePath);
          }
        }
      }
    }

    return libInfo;
  }

  private static @NotNull Library updatePackagesLibraryRoots(final @NotNull Project project, final @NotNull DartLibInfo libInfo) {
    final LibraryTable projectLibraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project);
    final Library existingLibrary = projectLibraryTable.getLibraryByName(DartPackagesLibraryType.DART_PACKAGES_LIBRARY_NAME);
    final Library library =
      existingLibrary != null ? existingLibrary
                              : WriteAction.compute(() -> {
                                final LibraryTable.ModifiableModel libTableModel =
                                  LibraryTablesRegistrar.getInstance().getLibraryTable(project).getModifiableModel();
                                final Library lib = libTableModel
                                  .createLibrary(DartPackagesLibraryType.DART_PACKAGES_LIBRARY_NAME, DartPackagesLibraryType.LIBRARY_KIND);
                                libTableModel.commit();
                                return lib;
                              });

    final String[] existingUrls = library.getUrls(OrderRootType.CLASSES);

    final Collection<String> libRootUrls = libInfo.getLibRootUrls();

    if ((isBrokenPackageMap(((LibraryEx)library).getProperties())) ||
        existingUrls.length != libRootUrls.size() ||
        !libRootUrls.containsAll(Arrays.asList(existingUrls))) {
      ApplicationManager.getApplication().runWriteAction(() -> {
        final LibraryEx.ModifiableModelEx model = (LibraryEx.ModifiableModelEx)library.getModifiableModel();
        for (String url : existingUrls) {
          model.removeRoot(url, OrderRootType.CLASSES);
        }

        for (String url : libRootUrls) {
          model.addRoot(url, OrderRootType.CLASSES);
        }

        final DartPackagesLibraryProperties libraryProperties = new DartPackagesLibraryProperties();
        libraryProperties.setPackageNameToDirsMap(libInfo.getPackagesMap());
        model.setProperties(libraryProperties);

        model.commit();
      });
    }

    return library;
  }

  private static boolean isBrokenPackageMap(final @Nullable LibraryProperties properties) {
    if (!(properties instanceof DartPackagesLibraryProperties)) return true;

    for (Map.Entry<String, List<String>> entry : ((DartPackagesLibraryProperties)properties).getPackageNameToDirsMap().entrySet()) {
      if (entry == null || entry.getKey() == null || entry.getValue() == null) {
        return true;
      }
    }

    return false;
  }

  private static void removeDartPackagesLibraryAndDependencies(final @NotNull Project project) {
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      removeDependencyOnDartPackagesLibrary(module);
    }

    final Library library =
      LibraryTablesRegistrar.getInstance().getLibraryTable(project).getLibraryByName(DartPackagesLibraryType.DART_PACKAGES_LIBRARY_NAME);
    if (library != null) {
      ApplicationManager.getApplication()
        .runWriteAction(() -> LibraryTablesRegistrar.getInstance().getLibraryTable(project).removeLibrary(library));
    }
  }

  public static void updateDependenciesOnDartPackagesLibrary(final @NotNull Project project,
                                                             final @NotNull Condition<? super Module> moduleFilter,
                                                             final @NotNull Library library) {
    for (Module module : ModuleManager.getInstance(project).getModules()) {
      if (moduleFilter.value(module)) {
        addDependencyOnDartPackagesLibrary(module, library);
      }
      else {
        removeDependencyOnDartPackagesLibrary(module);
      }
    }
  }

  private static void removeDependencyOnDartPackagesLibrary(final @NotNull Module module) {
    if (!hasDartPackageLibrary(module)) return;

    final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
    try {
      for (final OrderEntry orderEntry : modifiableModel.getOrderEntries()) {
        if (isDartPackageLibrary(orderEntry)) {
          modifiableModel.removeOrderEntry(orderEntry);
        }
      }

      if (modifiableModel.isChanged()) {
        ApplicationManager.getApplication().runWriteAction(modifiableModel::commit);
      }
    }
    finally {
      if (!modifiableModel.isDisposed()) {
        modifiableModel.dispose();
      }
    }
  }

  private static boolean isDartPackageLibrary(OrderEntry orderEntry) {
    return orderEntry instanceof LibraryOrderEntry &&
           LibraryTablesRegistrar.PROJECT_LEVEL.equals(((LibraryOrderEntry)orderEntry).getLibraryLevel()) &&
           DartPackagesLibraryType.DART_PACKAGES_LIBRARY_NAME.equals(((LibraryOrderEntry)orderEntry).getLibraryName());
  }

  private static void addDependencyOnDartPackagesLibrary(final @NotNull Module module, final @NotNull Library library) {
    if (hasDartPackageLibrary(module)) return;

    final ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
    try {
      modifiableModel.addLibraryEntry(library);

      ApplicationManager.getApplication().runWriteAction(modifiableModel::commit);
    }
    finally {
      if (!modifiableModel.isDisposed()) {
        modifiableModel.dispose();
      }
    }
  }

  private static boolean hasDartPackageLibrary(@NotNull Module module) {
    return ContainerUtil.exists(ModuleRootManager.getInstance(module).getOrderEntries(), DartFileListener::isDartPackageLibrary);
  }

  private static boolean isPathOutsideProjectContent(final @NotNull ProjectFileIndex fileIndex, @NotNull String path) {
    if (ApplicationManager.getApplication().isUnitTestMode() && path.contains("/pub/global/cache/")) {
      return true;
    }

    while (Strings.isNotEmpty(path)) {
      final VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
      if (file == null) {
        path = PathUtil.getParentPath(path);
      }
      else {
        return !fileIndex.isInContent(file);
      }
    }

    return false;
  }

  private static class DartLibInfo {
    private final Set<String> myLibRootUrls = new TreeSet<>();
    private final Map<String, List<String>> myPackagesMap = new TreeMap<>();

    private void addPackage(final @NotNull String packageName, final @NotNull String packagePath) {
      myLibRootUrls.add(VirtualFileUtil.pathToUrl(packagePath));

      List<String> paths = myPackagesMap.get((packageName));
      if (paths == null) {
        paths = new SmartList<>();
        myPackagesMap.put(packageName, paths);
      }

      if (!paths.contains(packagePath)) {
        paths.add(packagePath);
      }
    }

    private Set<String> getLibRootUrls() {
      return myLibRootUrls;
    }

    private Map<String, List<String>> getPackagesMap() {
      return myPackagesMap;
    }
  }

  private static final class DartFileChangeApplier implements ChangeApplier {
    private final List<? extends VFileEvent> myPackagesFileEvents;
    private final List<? extends VFileEvent> myMoveOrRenameAnalyzableFileEvents;

    private DartFileChangeApplier(List<? extends VFileEvent> packagesFileEvents,
                                  List<? extends VFileEvent> moveOrRenameAnalyzableFileEvents) {
      myPackagesFileEvents = packagesFileEvents;
      myMoveOrRenameAnalyzableFileEvents = moveOrRenameAnalyzableFileEvents;
    }

    @Override
    public void afterVfsChange() {
      Set<Project> projectsToUpdate = new HashSet<>();
      Set<Project> projectsToUpdateVisibleFiles = new HashSet<>();

      for (Project project : ProjectManager.getInstance().getOpenProjects()) {
        if (DartSdk.getDartSdk(project) == null) continue;

        for (VFileEvent event : myPackagesFileEvents) {
          VirtualFile file = event.getFile();
          if (file == null) continue;

          VirtualFile dartRoot = file.getParent();
          if (dartRoot != null && file.getName().equals(PackageConfigFileUtil.PACKAGE_CONFIG_JSON)) {
            dartRoot = dartRoot.getParent();
          }
          VirtualFile pubspec = dartRoot == null ? null : dartRoot.findChild(PUBSPEC_YAML);
          if (pubspec == null) continue;

          if (ProjectFileIndex.getInstance(project).isInContent(pubspec)) {
            projectsToUpdate.add(project);

            final Module module = ModuleUtilCore.findModuleForFile(pubspec, project);
            if (module != null) {
              DartStartupActivityKt.excludeBuildAndToolCacheFolders(module, pubspec);
            }
          }
        }

        for (VFileEvent event : myMoveOrRenameAnalyzableFileEvents) {
          VirtualFile file = event.getFile();
          if (file == null) continue;

          if (ProjectFileIndex.getInstance(project).isInContent(file)) {
            projectsToUpdateVisibleFiles.add(project);
            break;
          }
        }
      }

      for (Project project : projectsToUpdate) {
        scheduleDartPackageRootsUpdate(project);
      }

      for (Project project : projectsToUpdateVisibleFiles) {
        // this fixes WEB-39785
        DartAnalysisServerService.getInstance(project).updateVisibleFiles();
      }
    }
  }
}
