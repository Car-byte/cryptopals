package org.carbyte.set3;

import org.carbyte.breakers.AesCbcPaddingOracleBreaker;
import org.carbyte.common.Challenge;
import org.carbyte.oracle.CbcPaddingOracle;

public class Challenge17 implements Challenge<byte[]> {

    private final byte[] plainText;

    public Challenge17(final byte[] plainText) {
        this.plainText = plainText;
    }

    @Override
    public byte[] solve() {
        final CbcPaddingOracle cbcPaddingOracle = new CbcPaddingOracle();
        return new AesCbcPaddingOracleBreaker(cbcPaddingOracle::decrypt, cbcPaddingOracle.encrypt(plainText), cbcPaddingOracle.getIv()).doPaddingOracleAttack();
    }
}
