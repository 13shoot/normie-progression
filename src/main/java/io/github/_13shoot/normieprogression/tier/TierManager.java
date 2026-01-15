package io.github._13shoot.normieprogression.tier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TierManager {

    private static final Map<UUID, Tier> tiers = new HashMap<>();

    private TierManager() {
    }

    public static Tier getTier(UUID uuid) {
        return tiers.getOrDefault(uuid, Tier.T0_UNSEEN);
    }

    /**
     * Promote tier following progression rules.
     * This method is monotonic: it will NOT downgrade tier.
     */
    public static void promote(UUID uuid, Tier newTier) {

        Tier current = getTier(uuid);

        if (newTier.getLevel() > current.getLevel()) {
            tiers.put(uuid, newTier);
        }
    }

    /**
     * Force set tier (admin only).
     * This bypasses progression rules.
     */
    public static void forceSet(UUID uuid, Tier tier) {
        tiers.put(uuid, tier);

        // Chronicle hook (Tier reached)
        io.github._13shoot.normieprogression.chronicle.TierChronicleHook
                .onTierReached(uuid, tier);
    }

    /* ------------------------------------------------
     * Persistence helpers
     * ------------------------------------------------ */
    public static Map<UUID, Tier> getAll() {
        return tiers;
    }

    public static void load(UUID uuid, Tier tier) {
        tiers.put(uuid, tier);
    }
}
