package org.carbyte.cipher;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.IntStream;

public class Xor {

    public static byte[] xorBlocks(final byte[] block1, final byte[] block2) {
        Preconditions.checkArgument(block1.length == block2.length);

        return Bytes.toArray(IntStream.range(0, block1.length)
                .boxed()
                .map(Integer::byteValue)
                .map(i -> (byte) (block1[i] ^ block2[i]))
                .toList());
    }

    public static byte[] singleByteXor(final byte[] block, final byte key) {
        return Bytes.toArray(Bytes.asList(block).stream()
                        .map(b -> (byte) (b ^ key))
                        .toList());
    }

    public static byte[] breakSingleByteXoredBlock(final byte[] block) {
        return IntStream.range(0, 256).boxed()
                .map(Integer::byteValue)
                .map(i -> Xor.singleByteXor(block, i))
                .max(Comparator.comparingInt(Heuristic::computeHeuristic))
                .orElseThrow();
    }

    public static byte[] findSingleByteXoredFromListAndBreak(final byte[][] listOf) {
        return Arrays.stream(listOf)
                .map(Xor::breakSingleByteXoredBlock)
                .max(Comparator.comparingInt(Heuristic::computeHeuristic))
                .orElseThrow();
    }
}
