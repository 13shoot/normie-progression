package io.github._13shoot.normieprogression.chronicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ChronicleStorage {

    private static final Map<UUID, List<ChronicleEntry>> DATA = new HashMap<>();

    public static List<ChronicleEntry> get(UUID player) {
        return DATA.computeIfAbsent(player, k -> new ArrayList<>());
    }

    public static boolean has(UUID player, String id) {
        return get(player).stream()
                .anyMatch(e -> e.getId().equalsIgnoreCase(id));
    }

    public static void add(UUID player, ChronicleEntry entry) {
        if (has(player, entry.getId())) return;
        get(player).add(entry);
    }

    public static void clear(UUID player) {
        DATA.remove(player);
    }

    // --------------------------------------------------
    // Persistence access
    // --------------------------------------------------

    public static Map<UUID, List<ChronicleEntry>> getAll() {
        return DATA;
    }

    public static void set(UUID player, List<ChronicleEntry> entries) {
        DATA.put(player, entries);
    }
}
