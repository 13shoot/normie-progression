package io.github._13shoot.normieprogression.tier;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class TierStorage {

    private final File file;
    private final YamlConfiguration config;

    public TierStorage(JavaPlugin plugin) {

        this.file = new File(plugin.getDataFolder(), "tier.yml");

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public void loadAll() {

        for (String key : config.getKeys(false)) {

            UUID uuid = UUID.fromString(key);
            int level = config.getInt(key + ".tier", 0);

            TierManager.promote(uuid, Tier.fromLevel(level));
        }
    }

    public void saveAll() {

        for (TierData data : TierManager.getAll()) {

            String key = data.getPlayerId().toString();
            config.set(key + ".tier", data.getTier().getLevel());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
