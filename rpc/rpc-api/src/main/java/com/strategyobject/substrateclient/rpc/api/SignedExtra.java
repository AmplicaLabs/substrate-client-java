package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Ignore;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
@Getter
public class SignedExtra<E extends Era> implements Extra, SignedExtension {
    @Ignore
    private final long specVersion;
    @Ignore
    private final long txVersion;
    @Ignore
    private final BlockHash genesis;
    @Ignore
    private final BlockHash eraBlock;
    private final E era;
    private final Index nonce;
    @Scale(ScaleType.CompactBigInteger.class)
    private final BigInteger tip;
    @Ignore
    private final boolean hasMetadataHashSupport;
    // TODO: Support Metadata Hash
    private final MetadataHashMode mode = MetadataHashMode.DISABLED;

    @Override
    public AdditionalExtra getAdditionalExtra() {
        return new SignedAdditionalExtra(specVersion, txVersion, genesis, eraBlock, this.hasMetadataHashSupport);
    }
}
