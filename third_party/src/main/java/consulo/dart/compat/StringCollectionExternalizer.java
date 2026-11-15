// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package consulo.dart.compat;

import consulo.index.io.KeyDescriptor;
import consulo.index.io.data.DataInputOutputUtil;
import consulo.index.io.data.IOUtil;
import org.jetbrains.annotations.NotNull;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.IntFunction;

public final class StringCollectionExternalizer<C extends Collection<String>> implements KeyDescriptor<C> {
  public static final @NotNull StringCollectionExternalizer<List<String>> STRING_LIST_EXTERNALIZER = new StringCollectionExternalizer<>(ArrayList::new);

  private final @NotNull IntFunction<? extends C> myGenerator;

  public StringCollectionExternalizer(@NotNull IntFunction<? extends C> generator) {myGenerator = generator;}

  @Override
  public int hashCode(C value) {
    return value.hashCode();
  }

  @Override
  public boolean equals(C val1, C val2) {
    return val1.equals(val2);
  }

  @Override
  public void save(@NotNull DataOutput out, C collection) throws IOException {
    IOUtil.writeStringList(out, collection);
  }

  @Override
  public C read(@NotNull DataInput in) throws IOException {
    // IOUtil.readStringCollection(in, myGenerator)
    int size = DataInputOutputUtil.readINT(in);
    C strings = myGenerator.apply(size);
    for (int i = 0; i < size; i++) {
      strings.add(IOUtil.readUTF(in));
    }
    return strings;
  }
}
