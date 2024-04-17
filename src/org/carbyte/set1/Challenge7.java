package org.carbyte.set1;

import org.carbyte.cipher.AesEcb;
import org.carbyte.common.Challenge;

public class Challenge7 implements Challenge<byte[]> {

    private final byte[] key;
    private final byte[] cipherText;

    public Challenge7(final byte[] key, final byte[] cipherText) {
        this.key = key;
        this.cipherText = cipherText;
    }

    @Override
    public byte[] solve() {
        return new AesEcb(key).decrypt(cipherText);
    }
}
