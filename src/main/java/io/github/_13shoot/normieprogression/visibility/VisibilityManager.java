package io.github._13shoot.normieprogression.visibility;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VisibilityManager {

    private static final Map<UUID, VisibilityData> data = new HashMap<>();

    // Soft cap settings (v0.2.3)
    private static final int ECON_VIS_PER_DAY_SOFT_CAP = 20;

    private VisibilityManager() {}

    public static VisibilityData getOrCreate(UUID uuid) {
        return data.computeIfAbsent(uuid, k -> new VisibilityData());
    }

    public static VisibilityData get(UUID uuid) {
        return data.get(uuid);
    }

    public static int getVisibility(UUID uuid) {
        VisibilityData d = data.get(uuid);
        return d == null ? 0 : d.getTotalVisibility();
    }

    public static void addEconomyVisibility(UUID uuid, int amount) {
        if (amount <= 0) return;

        VisibilityData d = getOrCreate(uuid);

        int today = d.getEconomyVisibilityToday();
        int allowed = Math.max(0, ECON_VIS_PER_DAY_SOFT_CAP - today);

        int applied = Math.min(amount, allowed);
        if (applied > 0) {
            d.addEconomyVisibility(applied);
            d.addEconomyVisibilityToday(applied);
        }
    }

    public static Map<UUID, VisibilityData> getAll() {
        return data;
    }

    public static void load(UUID uuid, VisibilityData d) {
        data.put(uuid, d);
    }
}
