package org.carbyte.cipher;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Bytes;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;

public class AesCtr extends Aes {

    private final byte[] nonce;
    private long counter;

    public AesCtr(final byte[] key, final byte[] nonce) {
        super(key);
        Preconditions.checkArgument(nonce.length == Aes.BLOCK_SIZE_BYTES / 2);
        this.nonce = nonce;
        this.counter = 0;
    }

    @Override
    public byte[] encrypt(final byte[] plainTextBlock) {
        try {
            final byte[] encryptedBytes = generateBlocks(plainTextBlock.length);
            return Xor.xorBlocks(encryptedBytes, plainTextBlock);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(final byte[] cipherTextBlock) {
        return encrypt(cipherTextBlock);
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

    private byte[] longToBytes(final long x) {
        final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        final byte[] buff = buffer.array();
        Bytes.reverse(buff);
        return buff;
    }

    private byte[] generateBlocks(final int length) {
        final Cipher cipher = getCipherWithInit(Cipher.ENCRYPT_MODE);
        final byte[] bytes = new byte[length];
        int i = 0;

        while (i < length) {
            final byte[] counterBytes = Bytes.concat(nonce, longToBytes(counter));
            counter++;

            final byte[] encrypted = cipher.update(counterBytes);
            System.arraycopy(encrypted, 0 ,bytes, i, Math.min(Aes.BLOCK_SIZE_BYTES, length - i));
            i += Aes.BLOCK_SIZE_BYTES;
        }

        return bytes;
    }
}
