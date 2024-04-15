package org.carbyte.set1;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

class Challenge5Test {

    private static final byte[] PLAIN_TEXT = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal".getBytes();
    private static final byte[] KEY = "ICE".getBytes();
    private static final byte[] CIPHER_TEXT = HexFormat.of().parseHex("0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f");

    @Test
    public void challenge5() {
        final byte[] cipherText = new Challenge5(PLAIN_TEXT, KEY).solve();

        Assertions.assertArrayEquals(CIPHER_TEXT, cipherText);
    }
}