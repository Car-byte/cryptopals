package org.carbyte.common;

import com.google.common.base.CharMatcher;
import org.carbyte.cipher.AesEcb;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class Profile {

    public enum Role {
        ADMIN,
        USER
    }

    private static byte[] KEY = new byte[AesEcb.KEY_SIZE_BYTES];
    static {
        new SecureRandom().nextBytes(KEY);
    }
    private static final AesEcb aesEcb = new AesEcb(KEY);
    private final String email;
    private final UUID uuid;
    private final Role role;

    private Profile(final String email, final UUID uuid, final Role role) {
        this.email = email;
        this.uuid = uuid;
        this.role = role;
    }

    private Profile(final String email, final String  uuid, final String role) {
        this.email = email;
        this.uuid = UUID.fromString(uuid);
        this.role = Role.valueOf(role);
    }

    public Role getRole() {
        return role;
    }

    public static Profile profileFor(final String email) {
        final String filteredEmail = CharMatcher.anyOf("@&").removeFrom(email);
        return new Profile(filteredEmail, UUID.randomUUID(), Role.USER);
    }

    public byte[] encryptProfile() {
        final String profileSerialized = "email=" + email + "&uuid=" + uuid + "&role=" + role;
        return aesEcb.encrypt(profileSerialized.getBytes());
    }

    public static Profile decryptProfile(final byte[] encryptedProfile) {
        final String decryptedProfileSerialized = new String(aesEcb.decrypt(encryptedProfile));
        final List<String[]> keyAndValuePairs = Arrays.stream(decryptedProfileSerialized.split("&"))
                .map(keyValuePair -> keyValuePair.split("="))
                .toList();
        final Map<String, String> keyValueMap = keyAndValuePairs.stream()
                .collect(Collectors.toMap(keyValuePair -> keyValuePair[0], keyValuePair -> keyValuePair[1]));

        return new Profile(keyValueMap.get("email"), keyValueMap.get("uuid"), keyValueMap.get("role"));
    }
}
