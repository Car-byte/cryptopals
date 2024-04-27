package org.carbyte.oracle;

import com.google.common.primitives.Bytes;
import org.carbyte.cipher.Aes;
import org.carbyte.cipher.AesCbc;
import org.carbyte.common.KeyValueParser;

import java.security.SecureRandom;

public class CbcEncryptionOracle {

    private final byte[] bytesToPrepend;
    private final byte[] bytesToAppend;
    private final byte[] key;
    private final byte[] iv;

    public CbcEncryptionOracle(final byte[] bytesToPrepend, final byte[] bytesToAppend) {
        this.bytesToPrepend = bytesToPrepend;
        this.bytesToAppend = bytesToAppend;
        this.key = new byte[Aes.KEY_SIZE_BYTES];
        this.iv = new byte[Aes.BLOCK_SIZE_BYTES];
        new SecureRandom().nextBytes(key);
        new SecureRandom().nextBytes(iv);
    }

    public byte[] encrypt(final byte[] plainText) {
        final byte[] sanitizedInput = KeyValueParser.sanatizeString(new String(plainText)).getBytes();
        return new AesCbc(key, iv).encrypt(Bytes.concat(bytesToPrepend, sanitizedInput, bytesToAppend));
    }

    public byte[] decrypt(final byte[] cipherText) {
        return new AesCbc(key, iv).decrypt(cipherText);
    }
}
