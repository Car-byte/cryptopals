package org.carbyte.set1;

import org.carbyte.cipher.Xor;
import org.carbyte.common.Challenge;

public class Challenge6 implements Challenge<byte[]> {

    private final byte[] cipherText;

    public Challenge6(final byte[] cipherText) {
        this.cipherText = cipherText;
    }

    @Override
    public byte[] solve() {
        return Xor.breakRepeatXor(cipherText);
    }
}
