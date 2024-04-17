package org.carbyte.set1;


import org.carbyte.cipher.Xor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

class Challenge6Test {

    private static final String INPUT_FILE = Challenge6Test.class.getResource("challenge6.txt").getFile();
    private static final String EXPECTED_STR = "Play that funky music Come on, Come on, let me hear";
    private static final byte[] HAMMING_DISTANCE_1 = "this is a test".getBytes();
    private static final byte[] HAMMING_DISTANCE_2 = "wokka wokka!!!".getBytes();
    private static final int HAMMING_DISTANCE = 37;

    @Test
    public void challenge6HammingDistance() {
        final int hammingDistance = Xor.computeHammingDistance(HAMMING_DISTANCE_1, HAMMING_DISTANCE_2);

        Assertions.assertEquals(HAMMING_DISTANCE, hammingDistance);
    }

    @Test
    public void challenge6() throws IOException {
        final String inputB64 = String.join("", Files.readAllLines(Path.of(INPUT_FILE)));
        final byte[] input = Base64.getDecoder().decode(inputB64);

        final byte[] plainText = new Challenge6(input).solve();

        Assertions.assertTrue(new String(plainText).contains(EXPECTED_STR));
    }
}