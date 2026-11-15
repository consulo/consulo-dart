// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.ide.runner.server;

import consulo.ui.ex.action.AnActionWithSyncUpdate;
import consulo.webBrowser.BrowserFamily;
import consulo.webBrowser.BrowserLauncher;
import consulo.webBrowser.WebBrowser;
import consulo.webBrowser.WebBrowserManager;
import consulo.ui.ex.action.AnActionEvent;
import consulo.ui.ex.action.DumbAwareAction;
import consulo.project.Project;
import consulo.application.util.function.Computable;
import consulo.util.lang.StringUtil;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.ide.devtools.DartDevToolsService;
import icons.DartIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OpenDartObservatoryUrlAction extends DumbAwareAction implements AnActionWithSyncUpdate {
  private @Nullable String myUrl;
  private final Computable<Boolean> myIsApplicable;

  /**
   * @param url {@code null} if URL is not known at the moment of the action instantiation; use {@link #setUrl(String)} afterwards
   */
  public OpenDartObservatoryUrlAction(final @Nullable String url, final @NotNull Computable<Boolean> isApplicable) {
    super(DartBundle.message("open.observatory.action.text"),
          DartBundle.message("open.observatory.action.description"),
          DartIcons.Dart_16);
    myUrl = url;
    myIsApplicable = isApplicable;
  }

  public void setUrl(final @NotNull String url) {
    myUrl = url;
  }


  @Override
  public void update(final @NotNull AnActionEvent e) {
    e.getPresentation().setEnabled(myUrl != null && myIsApplicable.compute());
  }

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
    Project project = e.getData(Project.KEY);
    if (project == null || myUrl == null) return;

    String urlToOpen;
    String devToolsHostAndPort = DartDevToolsService.getInstance(project).getDevToolsHostAndPort();
    if (devToolsHostAndPort != null) {
      int colonIndex = myUrl.indexOf("://");
      String webSocketUri = "ws" + StringUtil.trimEnd(myUrl.substring(colonIndex), "/") + "/ws";
      urlToOpen = "http://" + devToolsHostAndPort + "/?uri=" + webSocketUri;
    }
    else {
      urlToOpen = myUrl;
    }

    openUrlInChromeFamilyBrowser(urlToOpen);
  }

  /**
   * Opens new tab in any already open Chrome-family browser, if none found - start any new Chrome-family browser
   */
  public static void openUrlInChromeFamilyBrowser(final @NotNull String url) {
    openInAnyChromeFamilyBrowser(url);
  }

  private static void openInAnyChromeFamilyBrowser(final @NotNull String url) {
    final List<WebBrowser> chromeBrowsers = WebBrowserManager.getInstance().getBrowsers(
      browser -> browser.getFamily() == BrowserFamily.CHROME);

    BrowserLauncher.getInstance().browse(url, chromeBrowsers.isEmpty() ? null : chromeBrowsers.getFirst());
  }
}
