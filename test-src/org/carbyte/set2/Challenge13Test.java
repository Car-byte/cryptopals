package org.carbyte.set2;

import org.carbyte.common.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class Challenge13Test {

    @Test
    public void challenge13() {
        final Profile profile = new Challenge13().solve();
        Assertions.assertEquals(Profile.Role.ADMIN, profile.getRole());
    }
}