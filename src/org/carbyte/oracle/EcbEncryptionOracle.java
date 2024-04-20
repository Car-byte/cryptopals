package org.carbyte.oracle;

import com.google.common.primitives.Bytes;
import org.carbyte.cipher.Aes;
import org.carbyte.cipher.AesEcb;

import java.security.SecureRandom;

public class EcbEncryptionOracle {

    private final byte[] bytesToAppend;
    private final byte[] key;

    public EcbEncryptionOracle(final byte[] bytesToAppend) {
        this.bytesToAppend = bytesToAppend;
        this.key = new byte[Aes.KEY_SIZE_BYTES];
        new SecureRandom().nextBytes(key);
    }

    public byte[] encrypt(final byte[] plainText) {
        return new AesEcb(key).encrypt(Bytes.concat(plainText, bytesToAppend));
    }
}
