package org.carbyte.breakers;

import com.google.common.primitives.Bytes;
import org.carbyte.detection.AesDetector;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AesEcbBreaker {

    private final Function<byte[], byte[]> encryptionFunction;
    private int knowSize;
    private int secretSize;
    private int blockSize;
    private byte[] lastKnowBlockMinusOne;

    public AesEcbBreaker(final Function<byte[], byte[]> encryptionFunction) {
        this.encryptionFunction = encryptionFunction;
    }

    public byte[] breakAesEcb() {
        blockSize = bruteForceBlockSize();
        secretSize = getSecretSize();
        knowSize = 0;
        verifyEcbMode();

        final int numberOfBlocks = encryptionFunction.apply(new byte[0]).length / blockSize;
        lastKnowBlockMinusOne = "A".repeat(blockSize - 1).getBytes();

        return Bytes.toArray(
                IntStream.range(0, numberOfBlocks).boxed()
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
            currentBlockSize = encryptionFunction.apply("A".repeat(count++).getBytes()).length;
        }

        return currentBlockSize - initialBlockSize;
    }

    private void verifyEcbMode() {
        final byte[] cipherText = encryptionFunction.apply("A".repeat(blockSize * 3).getBytes());
        final AesDetector.Mode mode = AesDetector.detectModeFromRepeatBytes(cipherText);

        if (mode != AesDetector.Mode.ECB) {
            throw new IllegalArgumentException("Not using ECB!");
        }
    }

    private int getSecretSize() {
        final int initialBlockSize = encryptionFunction.apply(new byte[]{}).length;
        int currentBlockSize = initialBlockSize;
        int count = 0;

        while (currentBlockSize == initialBlockSize) {
            currentBlockSize = encryptionFunction.apply("A".repeat(++count).getBytes()).length;
        }

        return initialBlockSize - count;
    }

    private byte[] breakBlock(final int blockIndex) {
        final byte[] unEncryptedBlock = new byte[blockSize];

        for (int i = blockSize - 1; i >= 0; i--) {
            if (knowSize >= secretSize) {
                return Bytes.toArray(Bytes.asList(unEncryptedBlock).subList(i + 1, blockSize));
            }

            final byte[] padding = "A".repeat(i).getBytes();
            final byte[] encrypted = encryptionFunction.apply(padding);
            final byte[] block = Arrays.copyOfRange(encrypted, blockIndex * blockSize, blockIndex * blockSize + blockSize);

            final byte newKnowByte = bruteForceBlock(block);
            lastKnowBlockMinusOne = Arrays.copyOfRange(lastKnowBlockMinusOne, 1, lastKnowBlockMinusOne.length);
            lastKnowBlockMinusOne = Bytes.concat(lastKnowBlockMinusOne, new byte[]{newKnowByte});
            unEncryptedBlock[i] = newKnowByte;
            knowSize++;
        }

        return unEncryptedBlock;
    }

    private byte bruteForceBlock(final byte[] block) {
        return IntStream.range(0, 256).boxed()
                .map(Integer::byteValue)
                .collect(Collectors.toMap(this::extractEncryptedBlock, Function.identity()))
                .get(Bytes.asList(block));
    }

    private List<Byte> extractEncryptedBlock(final byte b) {
        final byte[] unEncryptedBlock = Bytes.concat(lastKnowBlockMinusOne, new byte[]{b});
        final byte[] cipherText = encryptionFunction.apply(unEncryptedBlock);
        return Bytes.asList(cipherText).subList(0, blockSize);
    }
}
