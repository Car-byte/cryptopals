package org.carbyte.detection;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.carbyte.cipher.Aes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class AesDetector {

    public enum Mode {
        ECB,
        CBC
    }

    public static byte[] detectEcbFromList(final byte[][] listOfCipherText) {
        return Arrays.stream(listOfCipherText)
                .filter(cipherText -> detectModeFromRepeatBytes(cipherText) == Mode.ECB)
                .findAny()
                .orElseThrow();
    }

    public static Mode detectModeFromRepeatBytes(final byte[] cipherText) {
        final List<List<Byte>> partitions = Lists.partition(Bytes.asList(cipherText), Aes.KEY_SIZE_BYTES);

        // This can be optimized, but is good enough for now
        if (new HashSet<>(partitions).size() == partitions.size()) {
            return Mode.CBC;
        }

        return Mode.ECB;
    }
}
