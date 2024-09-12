package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import com.strategyobject.substrateclient.scale.annotation.AutoRegister;
import com.strategyobject.substrateclient.scale.registries.ScaleWriterRegistry;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.SuppressWarnings;

@AutoRegister(
    types = {com.strategyobject.substrateclient.rpc.api.SignedAdditionalExtra.class}
)
public class SignedAdditionalExtraWriter implements ScaleWriter<SignedAdditionalExtra> {
  private final ScaleWriterRegistry registry;

  public SignedAdditionalExtraWriter(ScaleWriterRegistry registry) {
    if (registry == null) {
      throw new IllegalArgumentException("registry can't be null.");
    }
    this.registry = registry;
  }

  @Override
  @SuppressWarnings({"unchecked"})
  public void write(SignedAdditionalExtra value, OutputStream stream, ScaleWriter<?>... writers)
      throws IOException {
    if (stream == null) throw new IllegalArgumentException("stream is null");
    if (value == null) throw new IllegalArgumentException("value is null");
    if (writers != null && writers.length > 0) throw new IllegalArgumentException();
    try {
      ((ScaleWriter)registry.resolve(ScaleType.U32.class)).write(value.getSpecVersion(), stream);
      ((ScaleWriter)registry.resolve(ScaleType.U32.class)).write(value.getTxVersion(), stream);
      ((ScaleWriter)registry.resolve(BlockHash.class)).write(value.getGenesis(), stream);
      ((ScaleWriter)registry.resolve(BlockHash.class)).write(value.getEraBlock(), stream);
      if (value.isHasMetadataHashSupport()) {
        ((ScaleWriter)registry.resolve(ScaleType.Option.class).inject(registry.resolve(ScaleType.U8.class))).write(value.getMetadataHash(), stream);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
