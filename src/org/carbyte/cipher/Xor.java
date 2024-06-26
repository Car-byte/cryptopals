package org.carbyte.cipher;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class Xor {

    public static byte[] xorBlocks(final byte[] block1, final byte[] block2) {
        Preconditions.checkArgument(block1.length == block2.length);

        return Bytes.toArray(IntStream.range(0, block1.length)
                .boxed()
                .map(Integer::byteValue)
                .map(i -> block1[i] ^ block2[i])
                .map(Integer::byteValue)
                .toList());
    }

    public static byte[] singleByteXor(final byte[] block, final byte key) {
        return Bytes.toArray(Bytes.asList(block).stream()
                        .map(b -> b ^ key)
                        .map(Integer::byteValue)
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

    public static byte[] repeatKeyXor(final byte[] plainText, final byte[] key) {
        return Bytes.toArray(IntStream.range(0, plainText.length)
                .mapToObj(i -> plainText[i] ^ key[i % key.length])
                .map(Integer::byteValue)
                .toList());
    }

    public static byte[] breakRepeatXor(final byte[] cipherText) {
        final int keySize = IntStream.range(2, 41)
                .boxed()
                .min(Comparator.comparingDouble(i -> computeNormalizedHammingDistance(cipherText, i)))
                .orElseThrow();

        final List<List<Byte>> keySizePartitions = Lists.partition(Bytes.asList(cipherText), keySize);
        final List<List<Byte>> transposedPartitions = transposeBlocks(keySizePartitions);

        final List<byte[]> plainTextTransposedPartitions = transposedPartitions.stream()
                .map(Bytes::toArray)
                .map(Xor::breakSingleByteXoredBlock)
                .toList();

        final List<Byte> plainText = IntStream.range(0, plainTextTransposedPartitions.getFirst().length)
                .boxed()
                .flatMap(i -> plainTextTransposedPartitions.stream()
                        .filter(partition -> i < partition.length)
                        .map(partition -> partition[i]))
                .toList();

        return Bytes.toArray(plainText);
    }

    private static double computeNormalizedHammingDistance(final byte[] cipherText, final int blockSize) {
        Preconditions.checkArgument(blockSize * 2 <= cipherText.length);

        List<List<Byte>> partitions = Lists.partition(Bytes.asList(cipherText), blockSize);

        if (partitions.getLast().size() != blockSize) {
            partitions = partitions.subList(0, partitions.size() - 1);
        }

        if (partitions.size() % 2 != 0) {
            partitions = partitions.subList(0, partitions.size() - 1);
        }

        final List<List<Byte>> partitionCopyJavaRulesAreDumb = partitions;

        return IntStream.range(0, partitions.size() / 2)
                .map(i -> Xor.computeHammingDistance(partitionCopyJavaRulesAreDumb.get(i * 2), partitionCopyJavaRulesAreDumb.get(i * 2 + 1)))
                .average()
                .orElseThrow() / blockSize;
    }

    public static int computeHammingDistance(final byte[] block1, final byte[] block2) {
        Preconditions.checkArgument(block1.length == block2.length);

        return IntStream.range(0, block1.length)
                .map(i -> computeHammingDistance(block1[i], block2[i]))
                .sum();
    }

    private static int computeHammingDistance(final List<Byte> block1, final List<Byte> block2) {
        Preconditions.checkArgument(block1.size() == block2.size());

        return IntStream.range(0, block1.size())
                .map(i -> computeHammingDistance(block1.get(i), block2.get(i)))
                .sum();
    }

    private static int computeHammingDistance(final byte byte1, final byte byte2) {
        return Integer.bitCount(byte1 ^ byte2);
    }

    private static List<List<Byte>> transposeBlocks(final List<List<Byte>> partitions) {
        return IntStream.range(0, partitions.size())
                .mapToObj(i -> partitions.stream()
                        .filter(partition -> i < partition.size())
                        .map(partition -> partition.get(i))
                        .toList())
                .toList();
    }
}
