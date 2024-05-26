package org.carbyte.set3;

import org.carbyte.cipher.AesCtr;
import org.carbyte.common.Challenge;

public class Challenge18 implements Challenge<byte[]> {

    private final byte[] cipherText;
    private final byte[] nonce;
    private final byte[] key;

    public Challenge18(final byte[] key,
                       final byte[] nonce,
                       final byte[] cipherText) {
        this.cipherText = cipherText;
        this.nonce = nonce;
        this.key = key;
    }

    @Override
    public byte[] solve() {
        return new AesCtr(key, nonce).decrypt(cipherText);
    }
}
