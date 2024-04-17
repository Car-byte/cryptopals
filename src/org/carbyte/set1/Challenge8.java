package org.carbyte.set1;

import org.carbyte.common.Challenge;
import org.carbyte.detection.AesDetector;

public class Challenge8 implements Challenge<byte[]> {

    private final byte[][] cipherTextList;

    public Challenge8(final byte[][] cipherTextList) {
        this.cipherTextList = cipherTextList;
    }

    @Override
    public byte[] solve() {
        return AesDetector.detectEcbFromList(cipherTextList);
    }
}
