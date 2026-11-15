// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.jetbrains.lang.dart.psi.impl;

import static consulo.language.impl.parser.GeneratedParserUtilBase.*;
import consulo.language.ast.ASTNode;
import consulo.language.Language;
import consulo.language.parser.PsiBuilder;
import consulo.language.parser.PsiBuilderFactory;
import consulo.language.file.FileTypeManager;
import consulo.project.Project;
import consulo.language.file.FileViewProvider;
import consulo.language.psi.PsiElement;
import consulo.language.psi.PsiManager;
import consulo.language.impl.file.SingleRootFileViewProvider;
import consulo.language.version.LanguageVersionUtil;
import consulo.language.impl.ast.FileElement;
import consulo.language.impl.ast.TreeElement;
import consulo.language.psi.scope.GlobalSearchScope;
import consulo.language.ast.IFileElementType;
import consulo.language.file.light.LightVirtualFile;
import com.jetbrains.lang.dart.DartLanguage;
import com.jetbrains.lang.dart.DartParser;
import com.jetbrains.lang.dart.DartTokenTypes;
import com.jetbrains.lang.dart.psi.DartExpressionCodeFragment;
import com.jetbrains.lang.dart.psi.DartFile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class DartExpressionCodeFragmentImpl extends DartFile implements DartExpressionCodeFragment {
  private PsiElement myContext;
  private boolean myPhysical;
  private FileViewProvider myViewProvider;
  private GlobalSearchScope myScope = null;

  public DartExpressionCodeFragmentImpl(Project project,
                                        String name,
                                        CharSequence text,
                                        boolean isPhysical) {
    super(new SingleRootFileViewProvider(PsiManager.getInstance(project),
                                         new LightVirtualFile(name, FileTypeManager.getInstance().getFileTypeByFileName(name), text),
                                         isPhysical) {
      @Override
      public boolean supportsIncrementalReparse(@NotNull Language rootLanguage) {
        return false;
      }
    });

    myPhysical = isPhysical;
    ((SingleRootFileViewProvider)getViewProvider()).forceCachedPsi(this);
    final DartFragmentElementType type = new DartFragmentElementType();
    init(type, type);
  }


  @Override
  public PsiElement getContext() {
    return myContext != null && myContext.isValid() ? myContext : super.getContext();
  }

  @Override
  public @NotNull FileViewProvider getViewProvider() {
    if (myViewProvider != null) return myViewProvider;
    return super.getViewProvider();
  }

  @Override
  protected DartExpressionCodeFragmentImpl clone() {
    final DartExpressionCodeFragmentImpl clone = (DartExpressionCodeFragmentImpl)cloneImpl((FileElement)calcTreeElement().clone());
    clone.myPhysical = false;
    clone.myOriginalFile = this;
    final SingleRootFileViewProvider cloneViewProvider =
      // Consulo: FileManager is internal
      new SingleRootFileViewProvider(getManager(), new LightVirtualFile(getName(), getLanguage(), getText()), false);
    clone.myViewProvider = cloneViewProvider;
    cloneViewProvider.forceCachedPsi(clone);
    clone.init(getContentElementType(), getContentElementType());
    return clone;
  }

  @Override
  public boolean isPhysical() {
    return myPhysical;
  }

  public void setContext(PsiElement context) {
    myContext = context;
  }

  @Override
  public void forceResolveScope(GlobalSearchScope scope) {
    myScope = scope;
  }

  @Override
  public GlobalSearchScope getForcedResolveScope() {
    return myScope;
  }

  private static class DartFragmentElementType extends IFileElementType {
    DartFragmentElementType() {
      super("DART_CODE_FRAGMENT", DartLanguage.INSTANCE);
    }

    @Override
    public @Nullable ASTNode parseContents(final @NotNull ASTNode chameleon) {
      final PsiElement psi = new DartPsiCompositeElementImpl(chameleon);
      return doParseContents(chameleon, psi);
    }

    @Override
    protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
      final PsiBuilderFactory factory = PsiBuilderFactory.getInstance();
      final PsiBuilder psiBuilder = factory.createBuilder(((TreeElement)chameleon).getManager().getProject(), chameleon,
                                                          LanguageVersionUtil.findDefaultVersion(getLanguage()));
//      var startTime = System.nanoTime();
      final PsiBuilder builder = adapt_builder_(DartTokenTypes.STATEMENTS, psiBuilder, new DartParser(), DartParser.EXTENDS_SETS_);

      PsiBuilder.Marker marker = enter_section_(builder, 0, _COLLAPSE_, "<code fragment>");
      boolean result = DartParser.expression(builder, 0);
      exit_section_(builder, 0, marker, DartTokenTypes.STATEMENTS, result, true, TRUE_CONDITION);
      return builder.getTreeBuilt();
      // The following is removed as it is experimental and does not provide any functionality in the IDE for Dart developers:
      // ParsingDiagnostics.registerParse(builder, getLanguage(), System.nanoTime() - startTime);
      // return treeBuilt; (set by builder.getTreeBuilt())
    }
  }
}
