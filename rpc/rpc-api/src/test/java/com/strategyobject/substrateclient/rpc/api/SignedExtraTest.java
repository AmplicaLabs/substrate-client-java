package com.strategyobject.substrateclient.api.pallet.balances;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.strategyobject.substrateclient.common.convert.HexConverter;
import com.strategyobject.substrateclient.rpc.api.ImmortalEra;
import com.strategyobject.substrateclient.rpc.api.SignedAdditionalExtra;
import com.strategyobject.substrateclient.rpc.api.SignedExtra;
import com.strategyobject.substrateclient.rpc.api.primitives.BlockHash;
import com.strategyobject.substrateclient.rpc.api.primitives.Hash256;
import com.strategyobject.substrateclient.rpc.api.primitives.Index;
import com.strategyobject.substrateclient.rpc.api.section.TestsHelper;
import com.strategyobject.substrateclient.scale.ScaleUtils;
import com.strategyobject.substrateclient.tests.containers.FrequencyVersion;
import java.math.BigInteger;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SignedExtraTest {

  private static final long SPEC_VERSION = FrequencyVersion.SPEC;
  private static final int TX_VERSION = 1;
  private static final int TIP = 0;

  private BlockHash genesis;

  @BeforeEach
  void setUp() {
    genesis = Hash256.fromBytes(
      HexConverter.toBytes("0x7700000000000000000000000000000000000000000000000000000000000077")
    );
  }

  /**
   * Tests the serialization of SignedExtra with and without metadata hash.
   *
   * @param includeMetadataHash whether to include metadata hash in the SignedExtra
   * @param expectedHash the expected hex output of the SCALE data
   * @throws Exception if an error occurs during serialization
   */
  @ParameterizedTest
  @CsvSource({ "false,0x000000", "true,0x00000000" })
  void testSignedExtra(boolean includeMetadataHash, String expectedHash) throws Exception {
    val extra = new SignedExtra<>(
      SPEC_VERSION,
      TX_VERSION,
      genesis,
      genesis,
      new ImmortalEra(),
      Index.of(0),
      BigInteger.valueOf(TIP),
      includeMetadataHash
    );
    byte[] signedExtraBytes = ScaleUtils.toBytes(extra, TestsHelper.SCALE_WRITER_REGISTRY, SignedExtra.class);

    assertThat("Expected hex output", HexConverter.toHex(signedExtraBytes), equalTo(expectedHash));
  }

  /**
   * Tests the serialization of SignedExtraAdditional with and without metadata hash.
   *
   * @param includeMetadataHash whether to include metadata hash in the SignedExtra
   * @param expectedHash the expected hex output of the SCALE data
   * @throws Exception if an error occurs during serialization
   */
  @ParameterizedTest
  @CsvSource(
    {
      "false,0x8d0000000100000077000000000000000000000000000000000000000000000000000000000000777700000000000000000000000000000000000000000000000000000000000077",
      "true,0x8d000000010000007700000000000000000000000000000000000000000000000000000000000077770000000000000000000000000000000000000000000000000000000000007700",
    }
  )
  void testSignedExtraAdditional(boolean includeMetadataHash, String expectedHash) throws Exception {
    val extra = new SignedExtra<>(
      SPEC_VERSION,
      TX_VERSION,
      genesis,
      genesis,
      new ImmortalEra(),
      Index.of(0),
      BigInteger.valueOf(TIP),
      includeMetadataHash
    );
    byte[] signedExtraAdditionalBytes = ScaleUtils.toBytes(
      extra.getAdditionalExtra(),
      TestsHelper.SCALE_WRITER_REGISTRY,
      SignedAdditionalExtra.class
    );

    assertThat("Expected hex output", HexConverter.toHex(signedExtraAdditionalBytes), equalTo(expectedHash));
  }
}
