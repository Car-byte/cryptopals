package org.carbyte.cipher;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.carbyte.common.MutableReference;
import org.carbyte.padding.Pkcs7;

import javax.crypto.Cipher;
import java.util.List;

public class AesCbc extends Aes {

    private final byte[] iv;

    public AesCbc(final byte[] key, final byte[] iv) {
        super(key);
        Preconditions.checkArgument(iv.length == Aes.BLOCK_SIZE_BYTES);
        this.iv = iv;
    }

    @Override
    public byte[] encrypt(final byte[] plainTextBlock) {
        final byte[] plainTextPadded = Pkcs7.pad(plainTextBlock, Aes.BLOCK_SIZE_BYTES);
        final List<List<Byte>> blocks = Lists.partition(Bytes.asList(plainTextPadded), Aes.BLOCK_SIZE_BYTES);
        final MutableReference<byte[]> previousBlock = new MutableReference<>(iv);

        final List<Byte> cipherText = blocks.stream()
                .map(block -> Xor.xorBlocks(previousBlock.get(), Bytes.toArray(block)))
                .map(this::doEcbEncrypt)
                .peek(previousBlock::set)
                .flatMap(encryptedBlock -> Bytes.asList(encryptedBlock).stream())
                .toList();

        return Bytes.toArray(cipherText);
    }

    private byte[] doEcbEncrypt(final byte[] plainText) {
        try {
            return getCipherWithInit(Cipher.ENCRYPT_MODE).doFinal(plainText);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] decrypt(final byte[] cipherTextBlock) {
        final List<List<Byte>> blocks = Lists.partition(Bytes.asList(cipherTextBlock), Aes.BLOCK_SIZE_BYTES);
        final MutableReference<byte[]> previousBlock = new MutableReference<>(iv);
        final MutableReference<byte[]> currentBlock = new MutableReference<>(iv);

        final List<Byte> plainText = blocks.stream()
                .map(Bytes::toArray)
                .peek(currentBlock::set)
                .map(this::doEcbDecrypt)
                .map(block -> Xor.xorBlocks(previousBlock.get(), block))
                .peek(ignore -> previousBlock.set(currentBlock.get()))
                .flatMap(plainTextBlock -> Bytes.asList(plainTextBlock).stream())
                .toList();

        return Pkcs7.unPad(Bytes.toArray(plainText));
    }

    private byte[] doEcbDecrypt(final byte[] plainText) {
        try {
            return getCipherWithInit(Cipher.DECRYPT_MODE).doFinal(plainText);
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected Cipher getCipherWithInit(final int opMode) {
        try {
            final Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(opMode, getKey());
            return cipher;
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
