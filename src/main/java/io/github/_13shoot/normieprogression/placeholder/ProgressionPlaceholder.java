package io.github._13shoot.normieprogression.placeholder;

import io.github._13shoot.normieprogression.visibility.VisibilityManager;
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
        // IMPORTANT:
        // Do NOT use underscore (_) here
        // Example: %normieprogression_visibility%
        return "normieprogression";
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
        // Required for PlaceholderAPI 2.11.x internal expansion
        return plugin.getName();
    }

    @Override
    public boolean persist() {
        // Keep this expansion registered across reloads
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {

        if (player == null) {
            return "";
        }

        // ----------------------------------------
        // Visibility placeholder
        // ----------------------------------------
        if (params.equalsIgnoreCase("visibility")) {
            return String.valueOf(
                    VisibilityManager.getVisibility(player.getUniqueId())
            );
        }

        // ----------------------------------------
        // Economic multiplier placeholder (stub)
        // ----------------------------------------
        if (params.equalsIgnoreCase("economic_multiplier")) {
            // Phase 1: fixed multiplier
            // Phase 2: derive from visibility / marks
            return "1.0";
        }

        return "";
    }
}
