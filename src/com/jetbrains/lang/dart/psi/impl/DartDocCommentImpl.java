package com.jetbrains.lang.dart.psi.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.jetbrains.lang.dart.psi.DartDocComment;

public class DartDocCommentImpl extends ASTWrapperPsiElement implements DartDocComment
{

	public DartDocCommentImpl(@NotNull final ASTNode node)
	{
		super(node);
	}

	@Nullable
	public PsiElement getOwner()
	{
		return null; // todo
	}

	public IElementType getTokenType()
	{
		return getNode().getElementType();
	}
}
