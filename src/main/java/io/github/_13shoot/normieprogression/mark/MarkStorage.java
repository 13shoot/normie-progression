package io.github._13shoot.normieprogression.mark;

import java.util.*;

public class MarkStorage {

    private static final Map<UUID, Map<MarkType, MarkData>> DATA = new HashMap<>();

    public static Collection<MarkData> getMarks(UUID player) {
        return DATA.getOrDefault(player, Collections.emptyMap()).values();
    }

    public static boolean hasMark(UUID player, MarkType type) {
        return DATA.containsKey(player) && DATA.get(player).containsKey(type);
    }

    public static void addMark(UUID player, MarkData data) {
        DATA.computeIfAbsent(player, k -> new EnumMap<>(MarkType.class))
            .put(data.getType(), data);
    }

    public static void removeMark(UUID player, MarkType type) {
        if (DATA.containsKey(player)) {
            DATA.get(player).remove(type);
        }
    }

    public static void cleanupExpired(long now) {
        for (Map<MarkType, MarkData> marks : DATA.values()) {
            marks.values().removeIf(mark -> mark.isExpired(now));
        }
    }
}
