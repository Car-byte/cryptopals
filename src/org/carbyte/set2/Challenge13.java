package org.carbyte.set2;

import org.carbyte.breakers.AesEcbCutAndPasteBreaker;
import org.carbyte.common.Challenge;
import org.carbyte.common.Profile;

public class Challenge13 implements Challenge<Profile> {

    @Override
    public Profile solve() {
        return new AesEcbCutAndPasteBreaker().breakEcb();
    }
}
