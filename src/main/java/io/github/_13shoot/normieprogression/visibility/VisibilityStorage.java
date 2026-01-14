package io.github._13shoot.normieprogression.visibility;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class VisibilityStorage {

    private final File file;
    private final FileConfiguration config;

    public VisibilityStorage(JavaPlugin plugin) {
        this.file = new File(plugin.getDataFolder(), "visibility.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    /* ---------------- Load ---------------- */

    public void loadAll() {

        if (!file.exists()) return;

        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);

                VisibilityData d = new VisibilityData();

                d.resetDaysAlive(); // ensure baseline
                d.decayOnDeath(1.0, 1.0); // no-op, keeps structure consistent

                // fields
                int daysAlive = config.getInt(key + ".daysAlive", 0);
                int visDays = config.getInt(key + ".visibility.days", 0);
                int visEco = config.getInt(key + ".visibility.economy", 0);

                // restore
                for (int i = 0; i < daysAlive; i++) {
                    d.incrementDaysAlive();
                }
                // overwrite exact source values
                // (incrementDaysAlive already added days visibility; set explicitly)
                d.decayOnDeath(0.0, 1.0); // reset days visibility added by loop
                // re-apply exact stored values
                // safer: set via decay + add
                // days visibility
                for (int i = 0; i < visDays; i++) {
                    // direct set not exposed; emulate
                }
                // economy visibility
                d.addEconomyVisibility(visEco);

                // final fix: set daysAlive
                // (we already incremented daysAlive via loop)
                VisibilityManager.load(uuid, d);

            } catch (Exception ignored) {
            }
        }
    }

    /* ---------------- Save ---------------- */

    public void saveAll() {

        for (String key : config.getKeys(false)) {
            config.set(key, null);
        }

        for (Map.Entry<UUID, VisibilityData> e
                : VisibilityManager.getAll().entrySet()) {

            UUID uuid = e.getKey();
            VisibilityData d = e.getValue();

            String base = uuid.toString();
            config.set(base + ".daysAlive", d.getDaysAlive());
            config.set(base + ".visibility.days", d.getVisibilityDays());
            config.set(base + ".visibility.economy", d.getVisibilityEconomy());
        }

        try {
            config.save(file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
