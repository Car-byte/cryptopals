package org.carbyte.oracle;

import org.carbyte.cipher.Aes;
import org.carbyte.detection.AesDetector;

import java.util.function.Function;

public class AesEcbCbcDetectionOracle {

    private static final byte[] LARGE_ENOUGH_BUFFER_TO_DETECT_ECB = "\0".repeat(Aes.BLOCK_SIZE_BYTES * 3).getBytes();

    public AesDetector.Mode detectMode(final Function<byte[], byte[]> encryptionFunction) {
        final byte[] cipherText = encryptionFunction.apply(LARGE_ENOUGH_BUFFER_TO_DETECT_ECB);

        return AesDetector.detectModeFromRepeatBytes(cipherText);
    }
}
