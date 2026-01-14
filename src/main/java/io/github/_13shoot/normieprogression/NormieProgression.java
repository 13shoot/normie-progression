package io.github._13shoot.normieprogression;

import io.github._13shoot.normieprogression.placeholder.ProgressionPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NormieProgression extends JavaPlugin {

    @Override
    public void onEnable() {

        // ---------------------------------------------
        // Register PlaceholderAPI expansion (IMPORTANT)
        // ---------------------------------------------
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ProgressionPlaceholder(this).register();
            getLogger().info("Progression placeholders registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found. Placeholders will not work.");
        }

        getLogger().info("NormieProgression enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("NormieProgression disabled.");
    }
}
