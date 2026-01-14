package io.github._13shoot.normieprogression.placeholder;

import io.github._13shoot.normieprogression.visibility.VisibilityService;
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
        // %normieprogression_visibility%
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

            // TEMP MOCK VALUES (Phase 1)
            // These will be replaced by real hooks later
            int daysAlive = 7;
            double totalMoneyEarned = 1250.0;

            int visibility = VisibilityService.calculateVisibility(
                    daysAlive,
                    totalMoneyEarned
            );

            return String.valueOf(visibility);
        }

        // ----------------------------------------
        // Economic multiplier placeholder
        // (stub for next step)
        // ----------------------------------------
        if (params.equalsIgnoreCase("economic_multiplier")) {
            return "1.0";
        }

        return "";
    }
}
