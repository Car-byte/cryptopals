package org.carbyte.set2;

import org.carbyte.cipher.Aes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

class Challenge10Test {

    private static final String INPUT_FILE = Challenge10Test.class.getResource("challenge10.txt").getFile();
    private static final byte[] KEY = "YELLOW SUBMARINE".getBytes();
    private static final byte[] IV = "\0".repeat(Aes.KEY_SIZE_BYTES).getBytes();
    private static final String EXPECTED = "Play that funky music Come on, Come on, let me hear";

    @Test
    public void challenge10() throws IOException {
        final String inputB64 = String.join("", Files.readAllLines(Path.of(INPUT_FILE)));
        final byte[] cipherText = Base64.getDecoder().decode(inputB64);

        final byte[] plainText = new Challenge10(cipherText, KEY, IV).solve();

        Assertions.assertTrue(new String(plainText).contains(EXPECTED));
    }
}