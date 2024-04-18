package org.carbyte.set2;

import org.carbyte.common.Challenge;
import org.carbyte.detection.AesDetector;
import org.carbyte.oracle.AesEcbCbcDetectionOracle;
import org.carbyte.oracle.EncryptionOracle;

public class Challenge11 implements Challenge<AesDetector.Mode> {

    private final AesEcbCbcDetectionOracle aesEcbCbcDetectionOracle;
    private final EncryptionOracle encryptionOracle;

    public Challenge11(final AesEcbCbcDetectionOracle aesEcbCbcDetectionOracle,
                       final EncryptionOracle encryptionOracle) {
        this.aesEcbCbcDetectionOracle = aesEcbCbcDetectionOracle;
        this.encryptionOracle = encryptionOracle;
    }

    @Override
    public AesDetector.Mode solve() {
        return aesEcbCbcDetectionOracle.detectMode(encryptionOracle::encrypt);
    }
}
