package org.carbyte.set2;

import org.carbyte.padding.Pkcs7;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Challenge15Test {

    private static final byte[] VALID_PADDING = "ICE ICE BABY\04\04\04\04".getBytes();
    private static final byte[] INVALID_PADDING_1 = "ICE ICE BABY\05\05\05\05".getBytes();
    private static final byte[] INVALID_PADDING_2 = "ICE ICE BABY\01\02\03\04".getBytes();

    @Test
    public void challenge15ValidPadding() {
        Assertions.assertDoesNotThrow(() -> {
            new Challenge15(VALID_PADDING).solve();
        });
    }

    @Test
    public void challenge15InvalidPadding1() {
        Assertions.assertThrows(Pkcs7.InvalidPaddingException.class, () -> {
            new Challenge15(INVALID_PADDING_1).solve();
        });
    }

    @Test
    public void challenge15InvalidPadding2() {
        Assertions.assertThrows(Pkcs7.InvalidPaddingException.class, () -> {
            new Challenge15(INVALID_PADDING_2).solve();
        });
    }
}