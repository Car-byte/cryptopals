package org.carbyte.set1;

import com.google.common.primitives.Bytes;
import org.carbyte.detection.AesDetector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

class Challenge8Test {

    private static final String INPUT_FILE = Challenge8Test.class.getResource("challenge8.txt").getFile();
    private static final byte[] EXPECTED_LINE = Bytes.toArray(List.of(-40, -128, 97, -105, 64, -88, -95, -101, 120, 64,
            -88, -93, 28, -127, 10, 61, 8, 100, -102, -9, 13, -64, 111, 79, -43, -46, -42, -100, 116, 76, -46, -125, -30, -35, 5,
            47, 107, 100, 29, -65, -99, 17, -80, 52, -123, 66, -69, 87, 8, 100, -102, -9, 13, -64, 111, 79, -43, -46, -42, -100,
            116, 76, -46, -125, -108, 117, -55, -33, -37, -63, -44, 101, -105, -108, -99, -100, 126, -126, -65, 90, 8, 100, -102,
            -9, 13, -64, 111, 79, -43, -46, -42, -100, 116, 76, -46, -125, -105, -87, 62, -85, -115, 106, -20, -43, 102, 72, -111,
            84, 120, -102, 107, 3, 8, 100, -102, -9, 13, -64, 111, 79, -43, -46, -42, -100, 116, 76, -46, -125, -44, 3, 24, 12, -104,
            -56, -10, -37, 31, 42, 63, -100, 64, 64, -34, -80, -85, 81, -78, -103, 51, -14, -63, 35, -59, -125, -122, -80, 111, -70, 24,
            106));

    @Test
    public void challenge8() throws IOException {
        final List<String> inputHexLines = Files.readAllLines(Path.of(INPUT_FILE));
        final List<byte[]> cipherTextList = inputHexLines.stream()
                .map(HexFormat.of()::parseHex)
                .toList();

        final byte[] ecbModeText = new Challenge8(cipherTextList.toArray(new byte[0][])).solve();

        Assertions.assertArrayEquals(EXPECTED_LINE, ecbModeText);
    }
}