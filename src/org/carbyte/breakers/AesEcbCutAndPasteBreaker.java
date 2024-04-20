package org.carbyte.breakers;

import com.google.common.primitives.Bytes;
import org.carbyte.common.Profile;
import org.carbyte.padding.Pkcs7;

import java.util.Arrays;
import java.util.UUID;

public class AesEcbCutAndPasteBreaker {

    private static final int LEN_WITHOUT_ROLE = "email=".length() + "&".length() + "uuid=".length()
            + UUID.randomUUID().toString().length() + "&".length() + "role=".length();

    public Profile breakEcb() {
        final int blockSize = getBlockSize();
        final byte[] adminBlockEncrypted = getAdminRoleBlock(blockSize);

        final int paddingNeeded = blockSize - (LEN_WITHOUT_ROLE % blockSize);
        final byte[] encryptedProfile = Profile.profileFor("A".repeat(paddingNeeded)).encryptProfile();
        final byte[] encryptedProfileWithoutRole = Arrays.copyOfRange(encryptedProfile, 0, encryptedProfile.length - blockSize);

        return Profile.decryptProfile(Bytes.concat(encryptedProfileWithoutRole, adminBlockEncrypted));
    }

    private int getBlockSize() {
        final int initialSize = Profile.profileFor("A").encryptProfile().length;
        int currentSize = initialSize;
        int count = 1;

        while (initialSize == currentSize) {
            currentSize = Profile.profileFor("A".repeat(++count)).encryptProfile().length;
        }

        return currentSize - initialSize;
    }

    private byte[] getAdminRoleBlock(final int blockSize) {
        final byte[] adminPadded = Pkcs7.pad("ADMIN".getBytes(), blockSize);
        final int paddingNeeded = blockSize - "email=".length();
        final byte[] encryptedProfile = Profile.profileFor("A".repeat(paddingNeeded) + new String(adminPadded)).encryptProfile();
        return Arrays.copyOfRange(encryptedProfile, blockSize, blockSize * 2);
    }
}
