package org.carbyte.set1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

class Challenge7Test {

    private static final String INPUT_FILE = Challenge7Test.class.getResource("challenge7.txt").getFile();
    private static final byte[] KEY = "YELLOW SUBMARINE".getBytes();
    private static final String EXPECTED = "Play that funky music Come on, Come on, let me hear";

    @Test
    public void challenge7() throws IOException {
        final String cipherTextB64 = String.join("", Files.readAllLines(Path.of(INPUT_FILE)));
        final byte[] cipherText = Base64.getDecoder().decode(cipherTextB64);

        final byte[] plainText = new Challenge7(KEY, cipherText).solve();

        Assertions.assertTrue(new String(plainText).contains(EXPECTED));
    }
}