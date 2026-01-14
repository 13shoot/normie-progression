package io.github._13shoot.normieprogression.visibility;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VisibilityManager {

    // In-memory storage for visibility data
    private static final Map<UUID, VisibilityData> dataMap =
            new ConcurrentHashMap<>();

    private VisibilityManager() {
        // utility class
    }

    /* ------------------------------------------------
     * Data access
     * ------------------------------------------------ */

    /**
     * Get existing VisibilityData or create a new one.
     */
    public static VisibilityData getOrCreate(UUID playerId) {
        return dataMap.computeIfAbsent(playerId, VisibilityData::new);
    }

    /**
     * Get VisibilityData if exists.
     */
    public static VisibilityData get(UUID playerId) {
        return dataMap.get(playerId);
    }

    /**
     * Remove data (not used yet, reserved for cleanup).
     */
    public static void remove(UUID playerId) {
        dataMap.remove(playerId);
    }

    /**
     * Get all visibility data entries.
     * Used by day counter scheduler.
     */
    public static Collection<VisibilityData> getAllData() {
        return dataMap.values();
    }

    /* ------------------------------------------------
     * Visibility calculation
     * ------------------------------------------------ */

    /**
     * Calculate visibility value for a player.
     */
    public static int getVisibility(UUID playerId) {
        VisibilityData data = dataMap.get(playerId);
        if (data == null) {
            return 0;
        }

        return VisibilityService.calculateVisibility(
                data.getDaysAlive(),
                data.getTotalMoneyEarned()
        );
    }
}
