package org.carbyte.set2;

import org.carbyte.breakers.AesEcbBreaker;
import org.carbyte.common.Challenge;
import org.carbyte.oracle.EcbEncryptionOracle;

public class Challenge12 implements Challenge<byte[]> {

    private final byte[] secretBytesToBreak;

    public Challenge12(final byte[] secretBytesToBreak) {
        this.secretBytesToBreak = secretBytesToBreak;
    }

    @Override
    public byte[] solve() {
        final EcbEncryptionOracle encryptionOracle = new EcbEncryptionOracle(secretBytesToBreak);
        return new AesEcbBreaker(encryptionOracle::encrypt).breakAesEcb();
    }
}
