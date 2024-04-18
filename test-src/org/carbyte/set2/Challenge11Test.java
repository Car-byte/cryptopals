package org.carbyte.set2;

import org.carbyte.detection.AesDetector;
import org.carbyte.oracle.AesEcbCbcDetectionOracle;
import org.carbyte.oracle.EncryptionOracle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Challenge11Test {

    @Test
    public void challenge11() {
        final EncryptionOracle encryptionOracle = new EncryptionOracle();
        final AesEcbCbcDetectionOracle aesEcbCbcDetectionOracle = new AesEcbCbcDetectionOracle();

        for (int i = 0; i < 10; i++) {
            final AesDetector.Mode detectedMode = new Challenge11(aesEcbCbcDetectionOracle, encryptionOracle).solve();
            final AesDetector.Mode actualMode = encryptionOracle.getLastUsedMode();

            Assertions.assertEquals(actualMode, detectedMode);
        }
    }
}