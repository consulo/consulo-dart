/*
 * Copyright 2013-2026 consulo.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package consulo.dart.compat;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Data of {@code org.eclipse.lsp4j.SemanticTokensLegend}: the analysis server announces the legend in its capabilities,
 * upstream keeps it in lsp4j's type. LSP is not used in Consulo, only the data is kept.
 */
public final class SemanticTokensLegend {
  private final @NotNull List<String> myTokenTypes;
  private final @NotNull List<String> myTokenModifiers;

  public SemanticTokensLegend(@NotNull List<String> tokenTypes, @NotNull List<String> tokenModifiers) {
    myTokenTypes = tokenTypes;
    myTokenModifiers = tokenModifiers;
  }

  public @NotNull List<String> getTokenTypes() {
    return myTokenTypes;
  }

  public @NotNull List<String> getTokenModifiers() {
    return myTokenModifiers;
  }
}
