package org.carbyte.oracle;

import com.google.common.primitives.Bytes;
import org.carbyte.cipher.Aes;
import org.carbyte.cipher.AesCbc;
import org.carbyte.cipher.AesEcb;
import org.carbyte.detection.AesDetector;

import java.security.SecureRandom;

public class EncryptionOracle {

    private final SecureRandom secureRandom;
    private AesDetector.Mode lastUsedMode;
    private final byte[] key;

    public EncryptionOracle() {
        this.secureRandom = new SecureRandom();
        this.key = new byte[Aes.KEY_SIZE_BYTES];
        secureRandom.nextBytes(key);
    }

    public byte[] encrypt(final byte[] plainText) {
        final byte[] plainTextWithRandom = getPlainTextWithRandom(plainText);
        return encryptWithRandomMode(plainTextWithRandom);
    }

    private byte[] getPlainTextWithRandom(final byte[] plainText) {
        final int randomPrefixSize = secureRandom.nextInt(5, 11);
        final int randomSuffixSize = secureRandom.nextInt(5, 11);

        final byte[] randomPrefix = new byte[randomPrefixSize];
        final byte[] randomSuffix = new byte[randomSuffixSize];

        secureRandom.nextBytes(randomPrefix);
        secureRandom.nextBytes(randomSuffix);

        return Bytes.concat(randomPrefix, plainText, randomSuffix);
    }

    private byte[] encryptWithRandomMode(final byte[] plainTextWithRandom) {
        final boolean shouldEncryptWithEcb = secureRandom.nextBoolean();
        if (shouldEncryptWithEcb) {
            lastUsedMode = AesDetector.Mode.ECB;
            return new AesEcb(key).encrypt(plainTextWithRandom);
        } else {
            lastUsedMode = AesDetector.Mode.CBC;
            final byte[] randomIv = new byte[Aes.BLOCK_SIZE_BYTES];
            secureRandom.nextBytes(randomIv);
            return new AesCbc(key, randomIv).encrypt(plainTextWithRandom);
        }
    }

    public AesDetector.Mode getLastUsedMode() {
        return lastUsedMode;
    }
}
