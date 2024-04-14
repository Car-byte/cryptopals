package org.carbyte.set1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HexFormat;

class Challenge2Test {

    private final byte[] BLOCK_1 = HexFormat.of().parseHex("1c0111001f010100061a024b53535009181c");
    private final byte[] BLOCK_2 = HexFormat.of().parseHex("686974207468652062756c6c277320657965");
    private final byte[] XORED_BLOCK = HexFormat.of().parseHex("746865206b696420646f6e277420706c6179");

    @Test
    public void challenge2() {
        final byte[] actualOutput = new Challenge2(BLOCK_1, BLOCK_2).solve();

        Assertions.assertArrayEquals(XORED_BLOCK, actualOutput);
    }
}