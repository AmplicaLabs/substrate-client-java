package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleGeneric;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;

@Getter
@ScaleWriter
public class SignedAdditionalExtra implements AdditionalExtra {
    @Scale(ScaleType.U32.class)
    private final long specVersion;
    @Scale(ScaleType.U32.class)
    private final long txVersion;
    private final BlockHash genesis;
    private final BlockHash eraBlock;
    @ScaleGeneric(
        template = "Option<U8>",
        types = {
            @Scale(ScaleType.Option.class),
            @Scale(ScaleType.U8.class)
        }
    )
    private final Optional<Integer> metadataHash;

    public SignedAdditionalExtra(long specVersion, long txVersion, @NonNull BlockHash genesis, @NonNull BlockHash eraBlock) {
        this.specVersion = (int) specVersion;
        this.txVersion = (int) txVersion;
        this.genesis = genesis;
        this.eraBlock = eraBlock;
        metadataHash = Optional.empty();
        // Set the metadata hash if we use it
        // this.metadataHash = 0;
    }
}
