package org.carbyte.set3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;

public class Challenge18Test {

    private static final byte[] CIPHER_TEXT = Base64.getDecoder().decode("L77na/nrFsKvynd6HzOoG7GHTLXsTVu9qvY/2syLXzhPweyyMTJULu/6/kXX0KSvoOLSFQ==");
    private static final byte[] KEY = "YELLOW SUBMARINE".getBytes();
    private static final byte[] NONCE = "\00".repeat(8).getBytes();
    private static final byte[] EXPECTED = "Yo, VIP Let's kick it Ice, Ice, baby Ice, Ice, baby ".getBytes();

    @Test
    public void challenge18() {
        final byte[] plainText = new Challenge18(KEY, NONCE, CIPHER_TEXT).solve();
        Assertions.assertArrayEquals(EXPECTED, plainText);
    }
}
