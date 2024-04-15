package org.carbyte.set1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;
import java.util.List;

class Challenge4Test {

    private static final String INPUT_FILE = Challenge4Test.class.getResource("challenge4.txt").getFile();
    private static final byte[] DECODED_INPUT = "Now that the party is jumping\n".getBytes();

    @Test
    public void challenge4() throws IOException {
        final List<String> encodedStrings = Files.readAllLines(Path.of(INPUT_FILE));
        final byte[][] encodedStringBytes = new byte[encodedStrings.size()][];

        for (int i = 0; i < encodedStringBytes.length; i++) {
            encodedStringBytes[i] = HexFormat.of().parseHex(encodedStrings.get(i));
        }

        final byte[] decodedInput = new Challenge4(encodedStringBytes).solve();
        Assertions.assertArrayEquals(DECODED_INPUT, decodedInput);
    }
}