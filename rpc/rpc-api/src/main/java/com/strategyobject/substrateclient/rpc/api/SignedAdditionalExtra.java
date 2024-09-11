package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.NonNull;

@Getter
@ScaleWriter
public class SignedAdditionalExtra implements AdditionalExtra {
    @Scale(ScaleType.U32.class)
    private final long specVersion;
    @Scale(ScaleType.U32.class)
    private final long txVersion;
    private final BlockHash genesis;
    private final BlockHash eraBlock;
    // TODO: Convert to a real Optional and support the actual MetadataHash
    @Scale(ScaleType.U8.class)
    private final int metadataHash = 0;

    public SignedAdditionalExtra(long specVersion, long txVersion, @NonNull BlockHash genesis, @NonNull BlockHash eraBlock) {
        this.specVersion = (int) specVersion;
        this.txVersion = (int) txVersion;
        this.genesis = genesis;
        this.eraBlock = eraBlock;
        // Set the metadata hash if we use it
        // this.metadataHash = 0;
    }
}
