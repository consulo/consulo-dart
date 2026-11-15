// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package consulo.dart.compat;

import consulo.language.editor.completion.lookup.LookupElement;
import consulo.language.editor.completion.lookup.LookupElementBuilder;
import consulo.language.editor.template.Expression;
import consulo.language.editor.template.ExpressionContext;
import consulo.language.editor.template.Result;
import consulo.language.editor.template.TextResult;
import consulo.util.collection.ContainerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * {@code com.intellij.codeInsight.template.impl.ConstantNode} of IntelliJ: Consulo's ConstantNode has no lookup items.
 * Lookup focus degree is not ported, Consulo's {@link Expression} has no such notion.
 */
public final class ConstantNode extends Expression {
  private final Result myValue;
  private final LookupElement[] myLookupElements;

  private final @Nullable String myPopupAdvertisement;

  public ConstantNode(@NotNull String value) {
    this(new TextResult(value));
  }

  public ConstantNode(@Nullable Result value) {
    this(value, LookupElement.EMPTY_ARRAY);
  }

  private ConstantNode(@Nullable Result value, LookupElement @NotNull ... lookupElements) {
    this(value, null, lookupElements);
  }

  private ConstantNode(@Nullable Result value, @Nullable String popupAdvertisement, LookupElement @NotNull ... lookupElements) {
    myValue = value;
    myPopupAdvertisement = popupAdvertisement;
    myLookupElements = lookupElements;
  }

  public ConstantNode withLookupItems(LookupElement @NotNull ... lookupElements) {
    return new ConstantNode(myValue, myPopupAdvertisement, lookupElements);
  }

  public ConstantNode withLookupItems(@NotNull Collection<? extends LookupElement> lookupElements) {
    return new ConstantNode(myValue, myPopupAdvertisement, lookupElements.toArray(LookupElement.EMPTY_ARRAY));
  }

  public ConstantNode withLookupStrings(String @NotNull ... lookupElements) {
    return new ConstantNode(myValue, myPopupAdvertisement,
                            ContainerUtil.map2Array(lookupElements, LookupElement.class, LookupElementBuilder::create));
  }

  public ConstantNode withLookupStrings(@NotNull Collection<String> lookupElements) {
    return new ConstantNode(myValue, myPopupAdvertisement,
                            ContainerUtil.map2Array(lookupElements, LookupElement.class, LookupElementBuilder::create));
  }

  public ConstantNode withPopupAdvertisement(@Nullable String popupAdvertisement) {
    return new ConstantNode(myValue, popupAdvertisement, myLookupElements);
  }

  @Override
  public Result calculateResult(ExpressionContext context) {
    return myValue;
  }

  @Override
  public boolean requiresCommittedPSI() {
    return false;
  }

  @Override
  public LookupElement[] calculateLookupItems(ExpressionContext context) {
    return myLookupElements;
  }

  @Override
  public @Nullable String getAdvertisingText() {
    return myPopupAdvertisement;
  }
}
