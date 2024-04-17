package org.carbyte.set2;

import org.carbyte.common.Challenge;
import org.carbyte.padding.Pkcs7;

public class Challenge9 implements Challenge<byte[]> {

    private final byte[] unPaddedInput;
    private final int blockSize;

    public Challenge9(final byte[] unPaddedInput, final int blockSize) {
        this.unPaddedInput = unPaddedInput;
        this.blockSize = blockSize;
    }

    @Override
    public byte[] solve() {
        return Pkcs7.pad(unPaddedInput, blockSize);
    }
}
