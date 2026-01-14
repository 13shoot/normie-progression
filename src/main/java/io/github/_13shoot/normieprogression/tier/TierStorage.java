package io.github._13shoot.normieprogression.tier;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class TierStorage {

    private final File file;
    private final FileConfiguration config;

    public TierStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "tier.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /* ------------------------------------------------
     * Load all tier data
     * ------------------------------------------------ */
    public void loadAll() {

        if (!file.exists()) return;

        for (String key : config.getKeys(false)) {

            try {
                UUID uuid = UUID.fromString(key);
                int level = config.getInt(key);

                Tier tier = Tier.fromLevel(level);
                TierManager.load(uuid, tier);

            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    /* ------------------------------------------------
     * Save all tier data
     * ------------------------------------------------ */
    public void saveAll() {

        // clear old data
        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }

        // ✅ FIX: iterate over entrySet()
        for (Map.Entry<UUID, Tier> entry : TierManager.getAll().entrySet()) {

            UUID uuid = entry.getKey();
            Tier tier = entry.getValue();

            config.set(uuid.toString(), tier.getLevel());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
