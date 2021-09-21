package com.strategyobject.substrateclient.scale;

import com.strategyobject.substrateclient.scale.registry.ScaleWriterNotFoundException;
import com.strategyobject.substrateclient.scale.registry.ScaleWriterRegistry;
import lombok.NonNull;
import lombok.val;

import java.io.IOException;
import java.io.OutputStream;

public interface ScaleSelfWritable<T extends ScaleSelfWritable<T>> {
    @SuppressWarnings("unchecked")
    default void write(@NonNull OutputStream stream) throws ScaleWriterNotFoundException, IOException {
        val writer = (ScaleWriter<T>) ScaleWriterRegistry
                .getInstance()
                .resolve(this.getClass());

        writer.write((T) this, stream);
    }
}
