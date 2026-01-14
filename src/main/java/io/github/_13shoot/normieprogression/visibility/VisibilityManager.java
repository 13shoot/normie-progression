package io.github._13shoot.normieprogression.visibility;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VisibilityManager {

    private static final Map<UUID, VisibilityData> dataMap = new ConcurrentHashMap<>();

    private VisibilityManager() {
    }

    /* -----------------------------
     * Data Access
     * ----------------------------- */

    public static VisibilityData getOrCreate(UUID playerId) {
        return dataMap.computeIfAbsent(playerId, VisibilityData::new);
    }

    public static VisibilityData get(UUID playerId) {
        return dataMap.get(playerId);
    }

    public static void remove(UUID playerId) {
        dataMap.remove(playerId);
    }

    /* -----------------------------
     * Visibility Value
     * ----------------------------- */

    public static int getVisibility(UUID playerId) {
        VisibilityData data = dataMap.get(playerId);
        if (data == null) return 0;

        return VisibilityService.calculateVisibility(
                data.getDaysAlive(),
                data.getTotalMoneyEarned()
        );
    }
}
