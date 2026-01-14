package io.github._13shoot.normieprogression.visibility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisibilityManager {

    private static final Map<UUID, VisibilityData> data = new HashMap<>();

    private VisibilityManager() {
    }

    public static VisibilityData getOrCreate(UUID uuid) {
        return data.computeIfAbsent(uuid, k -> new VisibilityData());
    }

    public static VisibilityData get(UUID uuid) {
        return data.get(uuid);
    }

    // NEW: unified accessor used by gates
    public static int getVisibility(UUID uuid) {
        VisibilityData d = data.get(uuid);
        return d == null ? 0 : d.getTotalVisibility();
    }

    /* ---------------- Economy hook ---------------- */

    public static void addEconomyVisibility(UUID uuid, int amount) {
        getOrCreate(uuid).addEconomyVisibility(amount);
    }

    /* ---------------- Persistence helpers ---------------- */

    public static Map<UUID, VisibilityData> getAll() {
        return data;
    }

    public static void load(UUID uuid, VisibilityData d) {
        data.put(uuid, d);
    }
}
