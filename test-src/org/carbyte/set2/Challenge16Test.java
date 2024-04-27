package org.carbyte.set2;

import org.carbyte.common.KeyValueParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Challenge16Test {

    private static final byte[] PREPEND_INPUT = "comment1=cooking%20MCs;userdata=".getBytes();
    private static final byte[] APPEND_INPUT = ";comment2=%20like%20a%20pound%20of%20bacon".getBytes();

    @Test
    public void challenge16() {
        final byte[] output = new Challenge16(PREPEND_INPUT, APPEND_INPUT).solve();
        final var keyValues = KeyValueParser.parse(new String(output));

        Assertions.assertEquals("true", keyValues.get("admin"));
    }
}