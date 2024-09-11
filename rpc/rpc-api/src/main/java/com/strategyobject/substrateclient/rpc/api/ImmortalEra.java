package com.strategyobject.substrateclient.rpc.api;

import com.strategyobject.substrateclient.scale.ScaleType;
import com.strategyobject.substrateclient.scale.annotation.Scale;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;
import lombok.Getter;

@Getter
@ScaleWriter
public class ImmortalEra implements Era {
    @Scale(ScaleType.CompactInteger.class)
    private final int calPeriod = 0;

    @Scale(ScaleType.CompactInteger.class)
    private final int quantizedPhase = 0;
}
