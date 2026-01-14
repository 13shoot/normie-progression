package io.github._13shoot.normieprogression.visibility;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class VisibilityStorage {

    private final File file;
    private final YamlConfiguration config;

    public VisibilityStorage(JavaPlugin plugin) {

        this.file = new File(plugin.getDataFolder(), "visibility.yml");

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

    /* ------------------------------------------------
     * Load all data on startup
     * ------------------------------------------------ */
    public void loadAll() {

        for (String key : config.getKeys(false)) {

            UUID uuid = UUID.fromString(key);

            VisibilityData data =
                    VisibilityManager.getOrCreate(uuid);

            data.resetDaysAlive();
            data.addMoneyEarned(0);

            data.resetDaysAlive();
            data.addMoneyEarned(
                    config.getDouble(key + ".moneyEarned", 0)
            );

            int days =
                    config.getInt(key + ".daysAlive", 0);

            for (int i = 0; i < days; i++) {
                data.incrementDaysAlive();
            }
        }
    }

    /* ------------------------------------------------
     * Save all data on shutdown
     * ------------------------------------------------ */
    public void saveAll() {

        for (VisibilityData data :
                VisibilityManager.getAllData()) {

            String key = data.getPlayerId().toString();

            config.set(key + ".daysAlive", data.getDaysAlive());
            config.set(key + ".moneyEarned", data.getTotalMoneyEarned());
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
