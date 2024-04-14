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

    public static byte[] singleByteXor(final byte[] block, final byte key) {
        final byte[] xored = new byte[block.length];
        for (int i = 0; i < block.length; i++) {
            xored[i] = (byte) (block[i] ^ key);
        }

        return xored;
    }

    public static byte[] breakSingleByteXoredBlock(final byte[] block) {
        byte[] bestBlockByHeuristic = null;
        int bestBlockHeuristic = Integer.MIN_VALUE;

        for (int i = 0; i <= 255; i++) {
            final byte[] currentBlock = Xor.singleByteXor(block, (byte) i);
            final int currentBlockHeuristic = Heuristic.computeHeuristic(currentBlock);

            if (currentBlockHeuristic > bestBlockHeuristic) {
                bestBlockHeuristic = currentBlockHeuristic;
                bestBlockByHeuristic = currentBlock;
            }
        }

        return bestBlockByHeuristic;
    }
}
