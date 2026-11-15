// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.application.ApplicationManager;
import consulo.disposer.Disposable;
import consulo.execution.ui.console.ConsoleViewContentType;
import consulo.execution.ui.console.TextConsoleBuilder;
import consulo.ide.impl.idea.util.net.NetUtils;
import consulo.logging.Logger;
import consulo.process.ExecutionException;
import consulo.process.ProcessHandler;
import consulo.process.event.ProcessEvent;
import consulo.process.event.ProcessListener;
import consulo.project.Project;
import consulo.ui.ex.action.ActionGroup;
import consulo.ui.ex.action.DefaultActionGroup;
import consulo.ui.image.Image;
import consulo.util.dataholder.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * {@code org.jetbrains.builtInWebServer.NetService} of IntelliJ: a process serving files for the built-in web server, with its console tool window.
 * Consulo: {@code AsyncValueLoader} / {@code AsyncPromise} are replaced by {@link ProcessHandlerLoader} / {@link CompletableFuture}.
 */
public abstract class NetService implements Disposable {
  private static final Logger LOG = Logger.getInstance(NetService.class);

  protected final @NotNull Project project;
  private final @NotNull ConsoleManager consoleManager;

  protected final ProcessHandlerLoader processHandler = new ProcessHandlerLoader();

  protected NetService(@NotNull Project project) {
    this(project, new ConsoleManager());
  }

  protected NetService(@NotNull Project project, @NotNull ConsoleManager consoleManager) {
    this.project = project;
    this.consoleManager = consoleManager;
  }

  public @NotNull Project getProject() {
    return project;
  }

  public @NotNull ProcessHandlerLoader getProcessHandler() {
    return processHandler;
  }

  /**
   * Starts the process on first {@link #get()}, keeps it until {@link #reset()}.
   */
  public final class ProcessHandlerLoader {
    private final AtomicReference<CompletableFuture<ProcessHandler>> ref = new AtomicReference<>();

    public @NotNull CompletableFuture<ProcessHandler> get() {
      CompletableFuture<ProcessHandler> current = ref.get();
      if (current != null) return current;

      CompletableFuture<ProcessHandler> promise = new CompletableFuture<>();
      if (!ref.compareAndSet(null, promise)) {
        return ref.get();
      }
      // cancel on reject
      promise.whenComplete((r, e) -> {
        if (e != null) ref.compareAndSet(promise, null);
      });
      load(promise);
      return promise;
    }

    public @Nullable ProcessHandler getResultIfFullFilled() {
      CompletableFuture<ProcessHandler> result = ref.get();
      return result != null && result.isDone() && !result.isCompletedExceptionally() ? result.getNow(null) : null;
    }

    public void reset() {
      CompletableFuture<ProcessHandler> oldValue = ref.getAndSet(null);
      if (oldValue == null) return;
      if (oldValue.completeExceptionally(new IllegalStateException("rejected"))) return;
      ProcessHandler result = oldValue.getNow(null);
      if (result != null) {
        disposeResult(result);
      }
    }

    private void load(@NotNull CompletableFuture<ProcessHandler> promise) {
      int port = getAvailableSocketPort();
      ProcessHandler handler;
      try {
        handler = createProcessHandler(project, port);
      }
      catch (ExecutionException e) {
        LOG.error(e);
        handler = null;
      }
      if (handler == null) {
        promise.completeExceptionally(new IllegalStateException("rejected"));
        return;
      }

      final ProcessHandler processHandler = handler;
      promise.whenComplete((r, e) -> {
        if (e != null) {
          processHandler.destroyProcess();
        }
      });

      MyProcessAdapter processListener = new MyProcessAdapter(processHandler);
      processHandler.addProcessListener(processListener);
      processHandler.startNotify();

      if (promise.isCompletedExceptionally()) {
        return;
      }

      ApplicationManager.getApplication().executeOnPooledThread(() -> {
        if (!promise.isCompletedExceptionally()) {
          try {
            connectToProcess(promise, port, processHandler, processListener);
          }
          catch (Throwable e) {
            if (!promise.completeExceptionally(e)) {
              LOG.error(e);
            }
          }
        }
      });
    }

    private void disposeResult(@NotNull ProcessHandler processHandler) {
      try {
        closeProcessConnections();
      }
      finally {
        processHandler.destroyProcess();
      }
    }
  }

  protected int getAvailableSocketPort() {
    try {
      return NetUtils.findAvailableSocketPort();
    }
    catch (java.io.IOException e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract @Nullable ProcessHandler createProcessHandler(@NotNull Project project, int port) throws ExecutionException;

  protected void connectToProcess(@NotNull CompletableFuture<ProcessHandler> promise,
                                  int port,
                                  @NotNull ProcessHandler processHandler,
                                  @NotNull Consumer<String> errorOutputConsumer) {
    promise.complete(processHandler);
  }

  protected abstract void closeProcessConnections();

  @Override
  public void dispose() {
    processHandler.reset();
  }

  protected void configureConsole(@NotNull TextConsoleBuilder consoleBuilder) {
  }

  protected abstract @NotNull String getConsoleToolWindowId();

  protected abstract @NotNull Image getConsoleToolWindowIcon();

  public @NotNull ActionGroup getConsoleToolWindowActions() {
    return new DefaultActionGroup();
  }

  private final class MyProcessAdapter implements ProcessListener, Consumer<String> {
    private final @Nullable ProcessHandler osProcessHandler;

    private MyProcessAdapter(@Nullable ProcessHandler osProcessHandler) {
      this.osProcessHandler = osProcessHandler;
    }

    @Override
    public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
      print(event.getText(), ConsoleViewContentType.getConsoleViewType(outputType));
    }

    private void print(String text, ConsoleViewContentType contentType) {
      consoleManager.getConsole(NetService.this).print(text, contentType);
    }

    @Override
    public void processTerminated(@NotNull ProcessEvent event) {
      ProcessHandler result = processHandler.getResultIfFullFilled();
      if (result != null && result == osProcessHandler) {
        processHandler.reset();
      }
      print(getConsoleToolWindowId() + " terminated\n", ConsoleViewContentType.SYSTEM_OUTPUT);
    }

    @Override
    public void accept(String message) {
      print(message, ConsoleViewContentType.ERROR_OUTPUT);
    }
  }
}
