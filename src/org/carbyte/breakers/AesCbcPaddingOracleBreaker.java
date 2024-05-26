package org.carbyte.breakers;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.carbyte.cipher.Aes;
import org.carbyte.cipher.Xor;
import org.carbyte.padding.Pkcs7;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class AesCbcPaddingOracleBreaker {

    private final BiFunction<byte[], byte[], Boolean> paddingOracle;
    private final byte[] cipherText;
    private final byte[] iv;

    public AesCbcPaddingOracleBreaker(final BiFunction<byte[], byte[], Boolean> paddingOracle,
                                      final byte[] cipherText,
                                      final byte[] iv) {
        Preconditions.checkArgument(iv.length == Aes.BLOCK_SIZE_BYTES);
        this.paddingOracle = paddingOracle;
        this.cipherText = cipherText;
        this.iv = iv;
    }

    public byte[] doPaddingOracleAttack() {
        Preconditions.checkState(cipherText.length % Aes.BLOCK_SIZE_BYTES == 0);

        final List<List<Byte>> cipherTextBlocks = Lists.partition(Bytes.asList(cipherText), Aes.BLOCK_SIZE_BYTES);
        Preconditions.checkState(cipherText.length == cipherTextBlocks.stream().mapToInt(List::size).sum());
        final List<Byte> plainText = new ArrayList<>();

        byte[] prevBlock = iv;
        for (List<Byte> block: cipherTextBlocks) {
            final List<Byte> brokenBlock = breakBlock(block);
            Preconditions.checkState(brokenBlock.size() == block.size());
            final byte[] blockPlainText = Xor.xorBlocks(prevBlock, Bytes.toArray(brokenBlock));

            plainText.addAll(Bytes.asList(blockPlainText));
            prevBlock = Bytes.toArray(block);
        }

        return Pkcs7.unPad(Bytes.toArray(plainText));
    }

    private List<Byte> breakBlock(final List<Byte> cipherTextBlock) {
        Preconditions.checkState(cipherTextBlock.size() == Aes.BLOCK_SIZE_BYTES);

        final byte[] zeroingIv = new byte[Aes.BLOCK_SIZE_BYTES];
        for (int i = Aes.BLOCK_SIZE_BYTES - 1; i >= 0; i--) {
            final int targetValue = Aes.BLOCK_SIZE_BYTES - i;
            final byte[] tempIv = Xor.singleByteXor(zeroingIv, (byte) targetValue);

            int b = 0;
            boolean found = false;
            for (; b <= 255; b++) {
                tempIv[i] = (byte) b;

                if (paddingOracle.apply(tempIv, Bytes.toArray(cipherTextBlock))) {
                    if (i == Aes.BLOCK_SIZE_BYTES - 1) {
                        tempIv[Aes.BLOCK_SIZE_BYTES - 2] ^= 1;
                        if (!paddingOracle.apply(tempIv, Bytes.toArray(cipherTextBlock))) {
                            continue;
                        }
                    }

                    found = true;
                    break;
                }
            }

            Preconditions.checkState(found);
            zeroingIv[i] = (byte) (b ^ targetValue);
        }

        return Bytes.asList(zeroingIv);
    }
}
