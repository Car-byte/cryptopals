package org.carbyte.set1;

import org.carbyte.common.Challenge;

import java.util.Base64;
import java.util.HexFormat;

public class Challenge1 implements Challenge<String> {

    private final String input;

    public Challenge1(final String input) {
        this.input = input;
    }

    @Override
    public String solve() {
        return hexToBase64(input);
    }

    private String hexToBase64(final String input) {
        final byte[] decoded = HexFormat.of().parseHex(input);
        return Base64.getEncoder().encodeToString(decoded);
    }
}
