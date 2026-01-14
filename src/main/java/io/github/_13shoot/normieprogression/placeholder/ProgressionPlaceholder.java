package io.github._13shoot.normieprogression.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
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
    public String getPlugin() {
        // REQUIRED for PlaceholderAPI 2.11.x internal expansion
        return plugin.getName();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if (player == null) {
            return "";
        }

        // ==============================
        // DEBUG MODE
        // Return the raw params we receive
        // ==============================
        return "PARAM=" + params;
    }
}
