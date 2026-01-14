package io.github._13shoot.normieprogression.tier;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TierManager {

    private static final Map<UUID, TierData> tierMap =
            new ConcurrentHashMap<>();

    private TierManager() {
    }

    public static TierData getOrCreate(UUID playerId) {
        return tierMap.computeIfAbsent(playerId, TierData::new);
    }

    public static Tier getTier(UUID playerId) {
        TierData data = tierMap.get(playerId);
        if (data == null) {
            return Tier.T0_UNSEEN;
        }
        return data.getTier();
    }

    public static void promote(UUID playerId, Tier newTier) {
        getOrCreate(playerId).promoteTo(newTier);
    }

    public static Collection<TierData> getAll() {
        return tierMap.values();
    }
}
