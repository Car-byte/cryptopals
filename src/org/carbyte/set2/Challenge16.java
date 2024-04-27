package org.carbyte.set2;

import org.carbyte.breakers.AesCbcBitFlipBreaker;
import org.carbyte.common.Challenge;
import org.carbyte.oracle.CbcEncryptionOracle;

public class Challenge16 implements Challenge<byte[]> {

    private final byte[] dataToPrepend;
    private final byte[] dataToAppend;

    public Challenge16(final byte[] dataToPrepend,
                       final byte[] dataToAppend) {
        this.dataToPrepend = dataToPrepend;
        this.dataToAppend = dataToAppend;
    }

    @Override
    public byte[] solve() {
        final CbcEncryptionOracle cbcEncryptionOracle = new CbcEncryptionOracle(dataToPrepend, dataToAppend);
        final byte[] modifiedCipherText = new AesCbcBitFlipBreaker(cbcEncryptionOracle).breakCbc();
        return cbcEncryptionOracle.decrypt(modifiedCipherText);
    }
}
