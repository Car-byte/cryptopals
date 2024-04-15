package org.carbyte.set1;

import org.carbyte.cipher.Xor;
import org.carbyte.common.Challenge;

public class Challenge5 implements Challenge<byte[]> {

    private final byte[] plainText;
    private final byte[] key;

    public Challenge5(final byte[] plainText, final byte[] key) {
        this.plainText = plainText;
        this.key = key;
    }

    @Override
    public byte[] solve() {
        return Xor.repeatKeyXor(plainText, key);
    }
}
