package io.github._13shoot.normieprogression.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class ProgressionPlaceholder extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    public ProgressionPlaceholder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getIdentifier() {
        // %normie_progression_xxx%
        return "normie_progression";
    }

    @Override
    public String getAuthor() {
        return "13shoot";
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        // Keep placeholder registered on reload
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        if (player == null) {
            return "";
        }

        // %normie_progression_visibility%
        if (params.equalsIgnoreCase("visibility")) {
            return "0"; // dummy value for now
        }

        // %normie_progression_economic_multiplier%
        if (params.equalsIgnoreCase("economic_multiplier")) {
            return "1.0"; // base multiplier
        }

        // Unknown placeholder
        return "";
    }
}
