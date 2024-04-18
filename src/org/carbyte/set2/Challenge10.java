package org.carbyte.set2;

import org.carbyte.cipher.AesCbc;
import org.carbyte.common.Challenge;

public class Challenge10 implements Challenge<byte[]> {

    private final byte[] cipherText;
    private final byte[] key;
    private final byte[] iv;

    public Challenge10(final byte[] cipherText, final byte[] key, final byte[] iv) {
        this.cipherText = cipherText;
        this.key = key;
        this.iv = iv;
    }

    @Override
    public byte[] solve() {
        return new AesCbc(key, iv).decrypt(cipherText);
    }
}
