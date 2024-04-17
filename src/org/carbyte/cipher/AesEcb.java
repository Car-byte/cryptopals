package org.carbyte.cipher;

import com.google.common.base.Preconditions;

import javax.crypto.Cipher;

public class AesEcb extends Aes {

    public AesEcb(byte[] key) {
        super(key);
    }

    @Override
    public byte[] encrypt(byte[] plainTextBlock) {
        try {
            return getCipherWithInit(Cipher.ENCRYPT_MODE).doFinal(plainTextBlock);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(byte[] cipherTextBlock) {
        try {
            return getCipherWithInit(Cipher.DECRYPT_MODE).doFinal(cipherTextBlock);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Cipher getCipherWithInit(final int opMode) {
        Preconditions.checkArgument(opMode == Cipher.ENCRYPT_MODE || opMode == Cipher.DECRYPT_MODE);
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(opMode, getKey());
            return cipher;
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
