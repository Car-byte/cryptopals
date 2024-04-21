package org.carbyte.padding;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;
import org.carbyte.cipher.Aes;

import java.util.Arrays;

public class Pkcs7 {

    public static class InvalidPaddingException extends RuntimeException {
    }

    public static byte[] pad(final byte[] input) {
        return pad(input, Aes.KEY_SIZE_BYTES);
    }

    public static byte[] pad(final byte[] input, final int blockSize) {
        Preconditions.checkArgument(blockSize > 0);

        final int paddingNeeded = blockSize - (input.length % blockSize);

        final byte[] padding = new byte[paddingNeeded];
        Arrays.fill(padding, (byte) paddingNeeded);

        return Bytes.concat(input, padding);
    }

    public static byte[] unPad(final byte[] padded) {
        Preconditions.checkArgument(padded.length != 0);

        checkValidPadding(padded);

        final int paddingLength = padded[padded.length - 1];
        return Arrays.copyOfRange(padded, 0, padded.length - paddingLength);
    }

    private static void checkValidPadding(final byte[] padded) {
        final int expectedPaddingValue = padded[padded.length - 1];
        int expectedPaddingLength = padded[padded.length - 1];

        Preconditions.checkArgument(padded.length - expectedPaddingLength >= 0);

        while (expectedPaddingLength > 0) {
            if (padded[padded.length - expectedPaddingLength] != expectedPaddingValue) {
                throw new InvalidPaddingException();
            }

            expectedPaddingLength--;
        }
    }
}
