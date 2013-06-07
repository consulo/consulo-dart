package com.jetbrains.lang.dart.validation.fixes;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.lang.dart.analyzer.AnalyzerMessage;
import com.jetbrains.lang.dart.util.DartPresentableUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jetbrains.lang.dart.validation.fixes.DartFixesUtil.findFixesForUnresolved;
import static com.jetbrains.lang.dart.validation.fixes.DartFixesUtil.isStaticContext;

public enum DartTypeErrorCode {
  UNDEFINED_FUNCTION {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // cannot resolve %s
      return findFixesForUnresolved(file, startOffset);
    }
  },
  NOT_A_MEMBER_OF {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // "%s" is not a member of %s
      return findFixesForUnresolved(file, startOffset);
    }
  }, CONCRETE_CLASS_WITH_UNIMPLEMENTED_MEMBERS {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // Concrete class %s has unimplemented member(s) %s
      return Arrays.asList(new ImplementMethodAction(startOffset));
    }
  }, EXTRA_ARGUMENT {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // todo: extra argument
      return Collections.emptyList();
    }
  }, UNDEFINED_GETTER {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // Field '%s' has no getter
      PsiElement elementAt = file.findElementAt(startOffset);
      return elementAt == null ? Collections.<IntentionAction>emptyList() : Arrays.asList(new CreateDartGetterSetterAction(
        elementAt.getText(),
        true,
        isStaticContext(file, startOffset)
      ));
    }
  }, UNDEFINED_SETTER {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // Field '%s' has no setter
      PsiElement elementAt = file.findElementAt(startOffset);
      return elementAt == null ? Collections.<IntentionAction>emptyList() : Arrays.asList(new CreateDartGetterSetterAction(
        elementAt.getText(),
        false,
        isStaticContext(file, startOffset)
      ));
    }
  }, UNDEFINED_METHOD {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // The method '%s' is not defined for the class '%s'
      final String messageText = message.getMessage();
      String functionName = DartPresentableUtil.findFirstQuotedWord(messageText);
      if (functionName == null) {
        return Collections.emptyList();
      }
      return Arrays.asList(new CreateDartMethodAction(functionName, isStaticContext(file, startOffset)));
    }
  }, NO_SUCH_NAMED_PARAMETER {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // todo:  no such named parameter %s defined
      return Collections.emptyList();
    }
  }, PLUS_CANNOT_BE_USED_FOR_STRING_CONCAT {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // todo:  '%s' cannot be used for string concatentation, use string interpolation or a StringBuffer instead
      return Collections.emptyList();
    }
  }, STATIC_MEMBER_ACCESSED_THROUGH_INSTANCE {
    @NotNull
    @Override
    public List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message) {
      // todo:  static member %s of %s cannot be accessed through an instance
      return Collections.emptyList();
    }
  };

  @NotNull
  public abstract List<? extends IntentionAction> getFixes(@NotNull PsiFile file, int startOffset, @NotNull AnalyzerMessage message);

  public static DartTypeErrorCode findError(String code) {
    try {
      return valueOf(code);
    }
    catch (IllegalArgumentException e) {
      return null;
    }
  }
}
