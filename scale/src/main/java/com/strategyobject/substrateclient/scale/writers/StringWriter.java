package com.strategyobject.substrateclient.scale.writers;

import com.google.common.base.Preconditions;
import com.strategyobject.substrateclient.common.io.Streamer;
import com.strategyobject.substrateclient.scale.ScaleWriter;
import lombok.NonNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class StringWriter implements ScaleWriter<String> {
    @Override
    public void write(@NonNull String value, @NonNull OutputStream stream, ScaleWriter<?>... writers) throws IOException {
        Preconditions.checkArgument(writers == null || writers.length == 0);

        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        CompactIntegerWriter.writeInternal(bytes.length, stream);
        Streamer.writeBytes(value.getBytes(StandardCharsets.UTF_8), stream);
    }
}
