package org.carbyte.breakers;

import com.google.common.base.Preconditions;
import org.carbyte.common.KeyValueParser;
import org.carbyte.oracle.CbcEncryptionOracle;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class AesCbcBitFlipBreaker {

    private static byte[] TWO_BITS_OFF_TARGET = ":admin<true".getBytes();
    private static final int OFFSET = 6;

    private final CbcEncryptionOracle cbcEncryptionOracle;

    public AesCbcBitFlipBreaker(final CbcEncryptionOracle cbcEncryptionOracle) {
        this.cbcEncryptionOracle = cbcEncryptionOracle;
    }

    public byte[] breakCbc() {
        final byte[] cipherText = cbcEncryptionOracle.encrypt(TWO_BITS_OFF_TARGET);
        final CbcBruteForceIterator bruteForceIterator = new CbcBruteForceIterator(cipherText);

        while (bruteForceIterator.hasNext()) {
            final byte[] cipherTextModified = bruteForceIterator.next();
            if (isAdmin(cipherTextModified)) {
                return cipherTextModified;
            }
        }

        throw new RuntimeException("Could not break CBC!");
    }

    private boolean isAdmin(final byte[] cipherTextModified) {
        try {
            final byte[] plainText = cbcEncryptionOracle.decrypt(cipherTextModified);
            final Map<String, String> keyValues = KeyValueParser.parse(new String(plainText));
            return "true".equals(keyValues.get("admin"));
        } catch (final Throwable ignore) {
        }

        return false;
    }

    private static class CbcBruteForceIterator implements Iterator<byte[]> {

        private final byte[] cipherText;
        private int index;
        private int mask;

        public CbcBruteForceIterator(final byte[] cipherText) {
            this.cipherText = cipherText;
            this.index = 0;
            this.mask = 0;
        }

        @Override
        public boolean hasNext() {
            return (index + OFFSET) < cipherText.length;
        }

        @Override
        public byte[] next() {
            Preconditions.checkState(hasNext());

            final byte[] cipherTextCopy = Arrays.copyOf(cipherText, cipherText.length);
            cipherTextCopy[index] ^= (byte) mask;
            cipherTextCopy[index + OFFSET] ^= (byte) mask;

            if (mask == 0xFF) {
                mask = 0;
                index++;
            } else {
                mask++;
            }

            return cipherTextCopy;
        }
    }
}
