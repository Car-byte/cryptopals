package org.carbyte.cipher;


import com.google.common.primitives.Bytes;

public class Heuristic {

    // bytes are signed, so used a mask to upcast the byte to an int
    // to preserve the most significant bit
    private static final int MASK = 0xff;

    /**
     * Computes a heuristic for a byte array. This heuristic attempts to give the highest score
     * to bytes which is english writing.
     *
     * This method is mostly used during a brute force to identify the best keys which when used
     * results in actual readable text.
     * @param bytes the bytes to find a heuristic score of
     * @return the computed heuristic score
     */
    public static int computeHeuristic(final byte[] bytes) {
        return Bytes.asList(bytes)
                .stream()
                .mapToInt(Heuristic::getHeuristicOfByte)
                .sum();
    }

    private static int getHeuristicOfByte(final byte b) {
        final char characterOfB = (char) (b & MASK);

        if (characterOfB == '\n' || characterOfB == ' ') {
            return 1;
        }

        if (Character.isAlphabetic(characterOfB)) {
            return 1;
        }

        if (!Character.isDefined(characterOfB)) {
            return -1;
        }

        return 0;
    }
}
