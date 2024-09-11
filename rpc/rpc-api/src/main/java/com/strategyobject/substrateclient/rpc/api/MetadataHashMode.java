package com.strategyobject.substrateclient.rpc.api;
import com.strategyobject.substrateclient.scale.annotation.ScaleWriter;

@ScaleWriter
public enum MetadataHashMode {
  /**
   * / No Metadata Hash will be included
   */
  DISABLED,

  /**
   * / Expects a Metadata Hash
   */
  ENABLED,
}
