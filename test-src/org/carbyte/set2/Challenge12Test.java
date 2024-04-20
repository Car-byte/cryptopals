package org.carbyte.set2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

class Challenge12Test {

    private static final byte[] SECRET_BYTES = Base64.getDecoder().decode("Um9sbGluJyBpbiBteSA1LjAKV2l0aCBteSByYWctdG9wIGRvd24gc28gbXkg" +
            "aGFpciBjYW4gYmxvdwpUaGUgZ2lybGllcyBvbiBzdGFuZGJ5IHdhdmluZyBq" +
            "dXN0IHRvIHNheSBoaQpEaWQgeW91IHN0b3A/IE5vLCBJIGp1c3QgZHJvdmUg" +
            "YnkK");
    private static final String EXPECTED = "Rollin' in my 5.0\n" +
            "With my rag-top down so my hair can blow\n" +
            "The girlies on standby waving just to say hi\n" +
            "Did you stop? No, I just drove by\n";

    @Test
    public void challenge12() {
        final byte[] plainText = new Challenge12(SECRET_BYTES).solve();
        Assertions.assertEquals(EXPECTED, new String(plainText));
    }
}