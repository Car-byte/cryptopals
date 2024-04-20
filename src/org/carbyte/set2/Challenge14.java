package org.carbyte.set2;

import org.carbyte.breakers.AesEcbBreaker;
import org.carbyte.common.Challenge;
import org.carbyte.oracle.EcbEncryptionOracle;

import java.security.SecureRandom;

public class Challenge14 implements Challenge<byte[]> {

    private final byte[] secretBytes;

    public Challenge14(final byte[] secretBytes) {
        this.secretBytes = secretBytes;
    }

    @Override
    public byte[] solve() {
        final int numberOfByteToPrepend = new SecureRandom().nextInt(0, 100);
        final byte[] randomBytes = new byte[numberOfByteToPrepend];
        new SecureRandom().nextBytes(randomBytes);
        final EcbEncryptionOracle encryptionOracle = new EcbEncryptionOracle(randomBytes, secretBytes);
        return new AesEcbBreaker(encryptionOracle::encrypt).breakAesEcb();
    }
}
