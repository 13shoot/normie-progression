package io.github._13shoot.normieprogression.tier;

import java.util.EnumMap;
import java.util.Map;

public class TierEffectRegistry {

    private static final Map<Tier, TierEffectProfile> effects =
            new EnumMap<>(Tier.class);

    static {
        // Base tier (no effect)
        effects.put(
                Tier.T0_UNSEEN,
                new TierEffectProfile(1.00, 0)
        );

        effects.put(
                Tier.T1_RECOGNIZED,
                new TierEffectProfile(1.05, 1)
        );

        effects.put(
                Tier.T2_ACKNOWLEDGED,
                new TierEffectProfile(1.10, 2)
        );

        effects.put(
                Tier.T3_RESPONDED,
                new TierEffectProfile(1.15, 3)
        );

        effects.put(
                Tier.T4_REMEMBERED,
                new TierEffectProfile(1.20, 4)
        );
    }

    private TierEffectRegistry() {
    }

    public static TierEffectProfile get(Tier tier) {
        return effects.getOrDefault(
                tier,
                effects.get(Tier.T0_UNSEEN)
        );
    }
}
