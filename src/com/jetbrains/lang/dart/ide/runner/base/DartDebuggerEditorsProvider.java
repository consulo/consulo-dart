package com.jetbrains.lang.dart.ide.runner.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProviderBase;
import com.jetbrains.lang.dart.DartFileType;
import com.jetbrains.lang.dart.util.DartElementGenerator;

public class DartDebuggerEditorsProvider extends XDebuggerEditorsProviderBase
{
	@Override
	@NotNull
	public FileType getFileType()
	{
		return DartFileType.INSTANCE;
	}

	@Override
	protected PsiFile createExpressionCodeFragment(@NotNull Project project, @NotNull String text, @Nullable PsiElement context, boolean isPhysical)
	{
		return DartElementGenerator.createExpressionCodeFragment(project, text, context, true);
	}
}