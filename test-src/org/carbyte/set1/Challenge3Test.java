package org.carbyte.set1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

class Challenge3Test {

    private static final byte[] SINGLE_BYTE_XORED_BLOCK = HexFormat.of().parseHex("1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736");
    private static final byte[] DECODED = "Cooking MC's like a pound of bacon".getBytes();

    @Test
    public void challenge3() {
        final byte[] decodedActual = new Challenge3(SINGLE_BYTE_XORED_BLOCK).solve();

        Assertions.assertArrayEquals(DECODED, decodedActual);
    }
}