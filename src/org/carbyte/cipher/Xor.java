package org.carbyte.cipher;

import com.google.common.base.Preconditions;

public class Xor {

    public static byte[] xorBlocks(final byte[] block1, final byte[] block2) {
        Preconditions.checkArgument(block1.length == block2.length);

        final byte[] xored = new byte[block1.length];
        for (int i = 0; i < block1.length; i++) {
            xored[i] = (byte) (block1[i] ^ block2[i]);
        }

        return xored;
    }
}
