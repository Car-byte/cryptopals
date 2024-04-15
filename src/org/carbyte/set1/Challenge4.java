package org.carbyte.set1;

import org.carbyte.cipher.Xor;
import org.carbyte.common.Challenge;

public class Challenge4 implements Challenge<byte[]> {

    private final byte[][] singleByteXorInputs;

    public Challenge4(final byte[][] singleByteXorInputs) {
        this.singleByteXorInputs = singleByteXorInputs;
    }

    @Override
    public byte[] solve() {
        return Xor.findSingleByteXoredFromListAndBreak(singleByteXorInputs);
    }
}
