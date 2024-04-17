package org.carbyte.set2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Challenge9Test {

    private static final byte[] INPUT_TEXT = "YELLOW SUBMARINE".getBytes();
    private static final int BLOCK_SIZE = 20;
    private static final byte[] PADDED_INPUT = "YELLOW SUBMARINE\04\04\04\04".getBytes();

    @Test
    public void challenge9() {
        final byte[] padded = new Challenge9(INPUT_TEXT, BLOCK_SIZE).solve();

        Assertions.assertArrayEquals(PADDED_INPUT, padded);
    }
}