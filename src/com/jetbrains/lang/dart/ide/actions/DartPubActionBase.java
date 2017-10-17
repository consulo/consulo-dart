package com.jetbrains.lang.dart.ide.actions;

import java.io.File;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.DartBundle;
import com.jetbrains.lang.dart.DartProjectComponent;
import consulo.dart.module.extension.DartModuleExtension;
import icons.DartIcons;

abstract public class DartPubActionBase extends AnAction
{
	private static final Logger LOG = Logger.getInstance("#com.jetbrains.lang.dart.ide.actions.DartPubActionBase");
	private static final String GROUP_DISPLAY_ID = "Dart Pub Tool";

	public DartPubActionBase()
	{
		super(DartIcons.Dart);
	}

	@Override
	public void update(AnActionEvent e)
	{
		e.getPresentation().setText(getPresentableText());
		final boolean enabled = getModuleAndPubspecYamlFile(e) != null;
		e.getPresentation().setVisible(enabled);
		e.getPresentation().setEnabled(enabled);
	}

	@Nullable
	private static Pair<Module, VirtualFile> getModuleAndPubspecYamlFile(final AnActionEvent e)
	{
		final Module module = e.getData(LangDataKeys.MODULE);
		final PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);

		if(module != null && psiFile != null && psiFile.getName().equalsIgnoreCase("pubspec.yaml"))
		{
			final VirtualFile file = psiFile.getOriginalFile().getVirtualFile();
			return file != null ? Pair.create(module, file) : null;
		}
		return null;
	}

	@Nls
	protected abstract String getPresentableText();

	protected abstract String getPubCommand();

	protected abstract String getSuccessMessage();

	@Override
	public void actionPerformed(final AnActionEvent e)
	{
		final Pair<Module, VirtualFile> moduleAndPubspecYamlFile = getModuleAndPubspecYamlFile(e);
		if(moduleAndPubspecYamlFile == null)
		{
			return;
		}

		File sdkRoot = getSdkRoot(moduleAndPubspecYamlFile);
		if(sdkRoot == null)
		{
			final int answer = Messages.showDialog(moduleAndPubspecYamlFile.first.getProject(), "Dart SDK is not configured", getPresentableText(), new String[]{
					"Configure SDK",
					"Cancel"
			}, 0, Messages.getErrorIcon());
			if(answer != 0)
			{
				return;
			}

			ShowSettingsUtil.getInstance().showSettingsDialog(moduleAndPubspecYamlFile.first.getProject(), DartBundle.message("dart.title"));

			sdkRoot = getSdkRoot(moduleAndPubspecYamlFile);
			if(sdkRoot == null)
			{
				return;
			}
		}

		File pubFile = new File(sdkRoot, SystemInfo.isWindows ? "bin/pub.bat" : "bin/pub");
		if(!pubFile.isFile())
		{
			Messages.showInfoMessage(moduleAndPubspecYamlFile.first.getProject(), DartBundle.message("dart.sdk.bad.dartpub.path", pubFile.getPath()), getPresentableText());

			return;
		}

		doExecute(moduleAndPubspecYamlFile.first, moduleAndPubspecYamlFile.second, sdkRoot.getPath(), pubFile.getPath());
	}

	private void doExecute(final Module module, final VirtualFile pubspecYamlFile, final String sdkPath, final String pubPath)
	{
		final Task.Backgroundable task = new Task.Backgroundable(module.getProject(), getPresentableText(), true)
		{
			public void run(@NotNull ProgressIndicator indicator)
			{
				indicator.setText(DartBundle.message("dart.pub.0.in.progress", getPubCommand()));
				indicator.setIndeterminate(true);
				final GeneralCommandLine command = new GeneralCommandLine();
				command.setExePath(pubPath);
				command.setWorkDirectory(pubspecYamlFile.getParent().getPath());
				command.addParameter(getPubCommand());
				command.getEnvironment().put("DART_SDK", sdkPath);

				ApplicationManager.getApplication().invokeAndWait(new Runnable()
				{
					@Override
					public void run()
					{
						FileDocumentManager.getInstance().saveAllDocuments();
					}
				}, ModalityState.defaultModalityState());


				try
				{
					final ProcessOutput processOutput = new CapturingProcessHandler(command).runProcess();
					final String err = processOutput.getStderr().trim();

					LOG.debug("pub " + getPubCommand() + ", exit code: " + processOutput.getExitCode() + ", err:\n" +
							err + "\nout:\n" + processOutput.getStdout());

					if(err.isEmpty())
					{
						Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, getPresentableText(), getSuccessMessage(), NotificationType.INFORMATION));
					}
					else
					{
						Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, getPresentableText(), err, NotificationType.ERROR));
					}

					ApplicationManager.getApplication().invokeLater(new Runnable()
					{
						public void run()
						{
							DartProjectComponent.excludePackagesFolders(module, pubspecYamlFile);
						}
					});
				}
				catch(ExecutionException ex)
				{
					LOG.error(ex);
					Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, getPresentableText(), DartBundle.message("dart.pub.exception", ex.getMessage()), NotificationType.ERROR));
				}
			}
		};

		task.queue();
	}

	@Nullable
	private static File getSdkRoot(final Pair<Module, VirtualFile> moduleAndPubspecYamlFile)
	{
		final Sdk sdk = ModuleUtilCore.getSdk(moduleAndPubspecYamlFile.first, DartModuleExtension.class);
		final String sdkPath = sdk == null ? null : sdk.getHomePath();
		final File sdkRoot = sdkPath == null || StringUtil.isEmptyOrSpaces(sdkPath) ? null : new File(sdkPath);
		return sdkRoot == null || !sdkRoot.isDirectory() ? null : sdkRoot;
	}
}
