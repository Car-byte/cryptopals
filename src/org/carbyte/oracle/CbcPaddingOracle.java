package org.carbyte.oracle;

import org.carbyte.cipher.Aes;
import org.carbyte.cipher.AesCbc;
import org.carbyte.padding.Pkcs7;

import java.security.SecureRandom;
import java.util.Arrays;

public class CbcPaddingOracle {

    private final byte[] iv;
    private final byte[] key;

    public CbcPaddingOracle() {
        this.iv = new byte[Aes.KEY_SIZE_BYTES];
        this.key = new byte[Aes.KEY_SIZE_BYTES];
        new SecureRandom().nextBytes(iv);
        new SecureRandom().nextBytes(key);
    }

    public byte[] encrypt(final byte[] plainText) {
        return new AesCbc(key, iv).encrypt(plainText);
    }

    public boolean decrypt(final byte[] iv, final byte[] cipherText) {
        try {
            new AesCbc(key, iv).decrypt(cipherText);
            return true;
        } catch (final Pkcs7.InvalidPaddingException e) {
            return false;
        }
    }

    public byte[] getIv() {
        return Arrays.copyOf(iv, iv.length);
    }
}
