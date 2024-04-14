package org.carbyte.set1;

import org.carbyte.cipher.Xor;
import org.carbyte.common.Challenge;

public class Challenge3 implements Challenge<byte[]> {

    private final byte[] input;

    public Challenge3(final byte[] input) {
        this.input = input;
    }

    @Override
    public byte[] solve() {
        return Xor.breakSingleByteXoredBlock(input);
    }
}
