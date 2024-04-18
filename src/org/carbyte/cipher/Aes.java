package org.carbyte.cipher;

import com.google.common.base.Preconditions;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class Aes {

    public static final String AES = "AES";
    public static final int KEY_SIZE_BYTES = 16;
    public static final int BLOCK_SIZE_BYTES = 16;
    private final SecretKey key;

    public Aes(final byte[] key) {
        Preconditions.checkArgument(key.length == KEY_SIZE_BYTES);
        this.key = new SecretKeySpec(key, AES);
    }

    protected SecretKey getKey() {
        return key;
    }

    public abstract byte[] encrypt(final byte[] plainTextBlock);

    public abstract byte[] decrypt(final byte[] cipherTextBlock);

    protected abstract Cipher getCipherWithInit(final int opMode);
}
