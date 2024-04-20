package org.carbyte.breakers;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.carbyte.detection.AesDetector;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AesEcbBreaker {

    private final Function<byte[], byte[]> encryptionFunction;
    private int numberOfKnownBytes;
    private final int secretSize;
    private final int blockSize;
    private final int baselinePaddingNeeded;
    private final int blockIndexForSecretText;
    private byte[] lastKnowBlockMinusOne;

    public AesEcbBreaker(final Function<byte[], byte[]> encryptionFunction) {
        this.encryptionFunction = encryptionFunction;
        blockSize = bruteForceBlockSize();
        verifyEcbMode();
        baselinePaddingNeeded = calculateBaselinePaddingNeeded();
        blockIndexForSecretText = getBlockIndexForSecretStart();
        secretSize = getSecretSize();
        numberOfKnownBytes = 0;
        lastKnowBlockMinusOne = "A".repeat(blockSize - 1).getBytes();
    }

    public byte[] breakAesEcb() {
        final int numberOfBlocks = encrypt(new byte[]{}).length / blockSize;

        return Bytes.toArray(
                IntStream.range(blockIndexForSecretText, numberOfBlocks).boxed()
                        .map(this::breakBlock)
                        .peek(Bytes::reverse)
                        .flatMap(block -> Bytes.asList(block).stream())
                        .toList()
        );
    }

    private int bruteForceBlockSize() {
        final int initialBlockSize = encryptionFunction.apply(new byte[]{}).length;
        int currentBlockSize = initialBlockSize;
        int count = 1;

        while (currentBlockSize == initialBlockSize) {
            currentBlockSize = encrypt("A".repeat(count++).getBytes()).length;
        }

        return currentBlockSize - initialBlockSize;
    }

    private void verifyEcbMode() {
        final byte[] cipherText = encrypt("A".repeat(blockSize * 3).getBytes());
        final AesDetector.Mode mode = AesDetector.detectModeFromRepeatBytes(cipherText);

        if (mode != AesDetector.Mode.ECB) {
            throw new IllegalArgumentException("Not using ECB!");
        }
    }

    private int getSecretSize() {
        final int initialBlockSize = encrypt(new byte[]{}).length;
        int currentBlockSize = initialBlockSize;
        int count = 0;

        while (currentBlockSize == initialBlockSize) {
            currentBlockSize = encrypt("A".repeat(++count).getBytes()).length;
        }

        return initialBlockSize - blockSize * (blockIndexForSecretText) - count;
    }

    private byte[] breakBlock(final int blockIndex) {
        final byte[] unEncryptedBlock = new byte[blockSize];

        for (int i = blockSize - 1; i >= 0; i--) {
            if (numberOfKnownBytes >= secretSize) {
                return Bytes.toArray(Bytes.asList(unEncryptedBlock).subList(i + 1, blockSize));
            }

            final byte[] encrypted = encrypt("A".repeat(i).getBytes());
            final byte[] block = Arrays.copyOfRange(encrypted, blockIndex * blockSize, blockIndex * blockSize + blockSize);

            final byte newKnowByte = bruteForceBlock(block);
            lastKnowBlockMinusOne = Arrays.copyOfRange(lastKnowBlockMinusOne, 1, lastKnowBlockMinusOne.length);
            lastKnowBlockMinusOne = Bytes.concat(lastKnowBlockMinusOne, new byte[]{newKnowByte});
            unEncryptedBlock[i] = newKnowByte;
            numberOfKnownBytes++;
        }

        return unEncryptedBlock;
    }

    private byte bruteForceBlock(final byte[] block) {
        return IntStream.range(0, 256).boxed()
                .map(Integer::byteValue)
                .collect(Collectors.toMap(this::extractEncryptedBlock, Function.identity()))
                .get(Bytes.asList(block));
    }

    private List<Byte> extractEncryptedBlock(final byte byteToBeLastInBock) {
        return Function.<Byte>identity()
                .andThen(lastByteInBlock -> Bytes.concat(lastKnowBlockMinusOne, new byte[]{lastByteInBlock}))
                .andThen(this::encrypt)
                .andThen(Bytes::asList)
                .andThen(cipherText -> cipherText.subList(blockIndexForSecretText * blockSize, blockSize * blockIndexForSecretText + blockSize))
                .apply(byteToBeLastInBock);
    }

    private int calculateBaselinePaddingNeeded() {
        byte[] a = encryptionFunction.apply("A".getBytes());
        byte[] b = encryptionFunction.apply("B".getBytes());
        final int blockIndexWithDiff = firstBlockWithDiff(a, b);
        int count = 0;

        while (true) {
            count++;
            a = encryptionFunction.apply("A".repeat(count).concat("A").getBytes());
            b = encryptionFunction.apply("A".repeat(count).concat("B").getBytes());
            final int newBlockIndexWithDiff = firstBlockWithDiff(a, b);

            if (newBlockIndexWithDiff != blockIndexWithDiff) {
                return count % blockSize;
            }
        }
    }

    private int getBlockIndexForSecretStart() {
        byte[] encryptedOutput1 = encryptionFunction.apply("A".getBytes());
        byte[] encryptedOutput2 = encryptionFunction.apply("B".getBytes());
        final int blockIndexWithDiff = firstBlockWithDiff(encryptedOutput1, encryptedOutput2);
        int count = 1;

        while (true) {
            count++;
            encryptedOutput1 = encryptionFunction.apply("A".repeat(count).concat("A").getBytes());
            encryptedOutput2 = encryptionFunction.apply("A".repeat(count).concat("B").getBytes());
            final int newBlockIndexWithDiff = firstBlockWithDiff(encryptedOutput1, encryptedOutput2);

            if (newBlockIndexWithDiff != blockIndexWithDiff) {
                return newBlockIndexWithDiff - (count / blockSize);
            }
        }
    }

    private int firstBlockWithDiff(final byte[] b1, final byte[] b2) {
        final List<List<Byte>> blocks1 = Lists.partition(Bytes.asList(b1), blockSize);
        final List<List<Byte>> blocks2 = Lists.partition(Bytes.asList(b2), blockSize);

        for (int i = 0; i < blocks1.size() && i < blocks2.size(); i++) {
            if (!blocks1.get(i).equals(blocks2.get(i))) {
                return i;
            }
        }

        return Math.min(blocks1.size(), blocks2.size()) - 1;
    }

    private byte[] encrypt(final byte[] bytes) {
        return encryptionFunction.apply(Bytes.concat("A".repeat(baselinePaddingNeeded).getBytes(), bytes));
    }
}
