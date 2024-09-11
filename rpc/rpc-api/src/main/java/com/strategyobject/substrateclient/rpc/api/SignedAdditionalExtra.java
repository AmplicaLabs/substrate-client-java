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
    private final MetadataHashMode mode;

    public SignedAdditionalExtra(long specVersion, long txVersion, @NonNull BlockHash genesis, @NonNull BlockHash eraBlock) {
        this.specVersion = (int) specVersion;
        this.txVersion = (int) txVersion;
        this.genesis = genesis;
        this.eraBlock = eraBlock;
        // Currently not supporting metadata hash
        // TODO: This should ONLY be here if this is hitting a chain that uses the CheckMetadataHash extension
        this.mode = MetadataHashMode.DISABLED;
    }
}
