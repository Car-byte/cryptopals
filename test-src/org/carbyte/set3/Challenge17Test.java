package org.carbyte.set3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

public class Challenge17Test {

    private static List<byte[]> PLAIN_TEXT_SAMPLES = List.of(
            Base64.getDecoder().decode("MDAwMDAwTm93IHRoYXQgdGhlIHBhcnR5IGlzIGp1bXBpbmc="),
            Base64.getDecoder().decode("MDAwMDAxV2l0aCB0aGUgYmFzcyBraWNrZWQgaW4gYW5kIHRoZSBWZWdhJ3MgYXJlIHB1bXBpbic="),
            Base64.getDecoder().decode("MDAwMDAyUXVpY2sgdG8gdGhlIHBvaW50LCB0byB0aGUgcG9pbnQsIG5vIGZha2luZw=="),
            Base64.getDecoder().decode("MDAwMDAzQ29va2luZyBNQydzIGxpa2UgYSBwb3VuZCBvZiBiYWNvbg=="),
            Base64.getDecoder().decode("MDAwMDA0QnVybmluZyAnZW0sIGlmIHlvdSBhaW4ndCBxdWljayBhbmQgbmltYmxl"),
            Base64.getDecoder().decode("MDAwMDA1SSBnbyBjcmF6eSB3aGVuIEkgaGVhciBhIGN5bWJhbA=="),
            Base64.getDecoder().decode("MDAwMDA2QW5kIGEgaGlnaCBoYXQgd2l0aCBhIHNvdXBlZCB1cCB0ZW1wbw=="),
            Base64.getDecoder().decode("MDAwMDA3SSdtIG9uIGEgcm9sbCwgaXQncyB0aW1lIHRvIGdvIHNvbG8="),
            Base64.getDecoder().decode("MDAwMDA4b2xsaW4nIGluIG15IGZpdmUgcG9pbnQgb2g="),
            Base64.getDecoder().decode("MDAwMDA5aXRoIG15IHJhZy10b3AgZG93biBzbyBteSBoYWlyIGNhbiBibG93")
    );

    @Test
    public void challenge17() {
        PLAIN_TEXT_SAMPLES.forEach(plainTextSample -> {
            final byte[] plainText = new Challenge17(plainTextSample).solve();
            Assertions.assertArrayEquals(plainTextSample, plainText);
        });
    }
}
