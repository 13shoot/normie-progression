package io.github._13shoot.normieprogression.chronicle;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChronicleFileStorage {

    private final JavaPlugin plugin;
    private final File file;
    private FileConfiguration config;

    public ChronicleFileStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "chronicle.yml");
    }

    public void load() {

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create chronicle.yml");
                e.printStackTrace();
                return;
            }
        }

        config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("players")) return;

        for (String uuidStr : config.getConfigurationSection("players").getKeys(false)) {

            UUID uuid;
            try {
                uuid = UUID.fromString(uuidStr);
            } catch (IllegalArgumentException ex) {
                continue;
            }

            List<Map<?, ?>> rawList = config.getMapList("players." + uuidStr);
            List<ChronicleEntry> entries = new ArrayList<>();

            for (Map<?, ?> map : rawList) {

                try {
                    ChronicleType type =
                            ChronicleType.valueOf((String) map.get("type"));
                    String id = (String) map.get("id");
                    String title = (String) map.get("title");
                    List<String> body = (List<String>) map.get("body");

                    entries.add(new ChronicleEntry(type, id, title, body));

                } catch (Exception ignored) {
                }
            }

            ChronicleStorage.set(uuid, entries);
        }

        plugin.getLogger().info("Chronicle loaded.");
    }

    public void save() {

        if (config == null) {
            config = new YamlConfiguration();
        }

        config.set("players", null);

        for (Map.Entry<UUID, List<ChronicleEntry>> entry :
                ChronicleStorage.getAll().entrySet()) {

            String key = "players." + entry.getKey().toString();
            List<Map<String, Object>> out = new ArrayList<>();

            for (ChronicleEntry c : entry.getValue()) {

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("type", c.getType().name());
                map.put("id", c.getId());
                map.put("title", c.getTitle());
                map.put("body", c.getBody());

                out.add(map);
            }

            config.set(key, out);
        }

        try {
            config.save(file);
            plugin.getLogger().info("Chronicle saved.");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save chronicle.yml");
            e.printStackTrace();
        }
    }
}
