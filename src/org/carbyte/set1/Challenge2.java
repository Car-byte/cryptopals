package org.carbyte.set1;

import org.carbyte.cipher.Xor;
import org.carbyte.common.Challenge;

public class Challenge2 implements Challenge<byte[]> {

    private final byte[] block1;
    private final byte[] block2;

    public Challenge2(final byte[] block1, final byte[] block2) {
        this.block1 = block1;
        this.block2 = block2;
    }

    @Override
    public byte[] solve() {
        return Xor.xorBlocks(block1, block2);
    }
}
