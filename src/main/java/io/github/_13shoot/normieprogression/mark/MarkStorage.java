package io.github._13shoot.normieprogression.mark;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * MarkStorage
 *
 * - Handles in-memory mark state
 * - Handles cooldowns (game-day based)
 * - Persists marks to marks.yml
 */
public class MarkStorage {

    private static final Map<UUID, Map<MarkType, MarkData>> DATA = new HashMap<>();
    private static final Map<UUID, Map<MarkType, Integer>> COOLDOWNS = new HashMap<>();

    private static File file;
    private static FileConfiguration config;

    /* =================================================
     * INIT
     * ================================================= */
    public static void init(JavaPlugin plugin) {

        file = new File(plugin.getDataFolder(), "marks.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
        loadAll();
    }

    /* =================================================
     * BASIC ACCESS
     * ================================================= */
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

    /* =================================================
     * COOLDOWN (GAME DAY BASED)
     * ================================================= */
    public static boolean isOnCooldown(UUID player, MarkType type, int currentDay) {
        return COOLDOWNS.containsKey(player)
                && COOLDOWNS.get(player).getOrDefault(type, -1) > currentDay;
    }

    public static void setCooldown(UUID player, MarkType type, int untilDay) {
        COOLDOWNS
                .computeIfAbsent(player, k -> new EnumMap<>(MarkType.class))
                .put(type, untilDay);
    }

    /* =================================================
     * CLEANUP (GAME DAY BASED)
     * ================================================= */
    public static void cleanupExpired(int currentDay) {

        for (Map<MarkType, MarkData> marks : DATA.values()) {
            marks.values().removeIf(mark -> mark.isExpired(currentDay));
        }
    }

    /* =================================================
     * PERSISTENCE
     * ================================================= */
    public static void saveAll() {

        config.set("players", null);

        for (UUID uuid : DATA.keySet()) {

            String base = "players." + uuid;

            for (MarkData mark : DATA.get(uuid).values()) {

                String path = base + "." + mark.getType().name();

                config.set(path + ".obtained", mark.getObtainedDay());
                config.set(path + ".expires", mark.getExpiresDay());
            }
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadAll() {

        DATA.clear();
        COOLDOWNS.clear();

        if (!config.contains("players")) return;

        for (String uuidKey : config.getConfigurationSection("players").getKeys(false)) {

            UUID uuid = UUID.fromString(uuidKey);
            Map<MarkType, MarkData> marks = new EnumMap<>(MarkType.class);

            String base = "players." + uuidKey;

            for (String markKey : config.getConfigurationSection(base).getKeys(false)) {

                MarkType type = MarkType.valueOf(markKey);

                int obtained = config.getInt(base + "." + markKey + ".obtained");
                int expires = config.getInt(base + "." + markKey + ".expires");

                marks.put(type, new MarkData(
                        type,
                        obtained,
                        expires
                ));
            }

            DATA.put(uuid, marks);
        }
    }
}
