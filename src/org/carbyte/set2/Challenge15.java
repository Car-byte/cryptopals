package org.carbyte.set2;

import org.carbyte.common.Challenge;
import org.carbyte.padding.Pkcs7;

public class Challenge15 implements Challenge<byte[]> {

    private final byte[] bytesToUnPad;

    public Challenge15(final byte[] bytesToUnPad) {
        this.bytesToUnPad = bytesToUnPad;
    }

    @Override
    public byte[] solve() {
        return Pkcs7.unPad(bytesToUnPad);
    }
}
