package org.carbyte.set1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Challenge1Test {

    private static final String HEX_INPUT = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
    private static final String BASE64_OUTPUT = "SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t";

    @Test
    public void challenge1() {
        final String actualOutput = new Challenge1().solve(HEX_INPUT);

        Assertions.assertEquals(BASE64_OUTPUT, actualOutput);
    }
}