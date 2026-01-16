package io.github._13shoot.normieprogression.placeholder;

import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * =====================================================
 * NormieProgression - PlaceholderAPI Expansion
 * -----------------------------------------------------
 *
 * Identifier:
 *   %normieprogression_<params>%
 *
 * Supported placeholders:
 *
 *  VISIBILITY
 *   - %normieprogression_visibility%
 *
 *  TIER
 *   - %normieprogression_tier%
 *   - %normieprogression_tier_level%
 *
 *  MARKS (boolean: true / false)
 *   - %normieprogression_mark_SURVIVAL%
 *   - %normieprogression_mark_PERSISTENCE%
 *   - %normieprogression_mark_RECOGNITION%
 *   - %normieprogression_mark_COLD%
 *   - %normieprogression_mark_INFLUENCE%
 *   - %normieprogression_mark_WITNESS%
 *   - %normieprogression_mark_HUNGER%
 *   - %normieprogression_mark_BLOOD%
 *   - %normieprogression_mark_LOSS%
 *   - %normieprogression_mark_TRADE%
 *   - %normieprogression_mark_FEAR%
 *   - %normieprogression_mark_FAVOR%
 *
 * Design goals:
 *  - Simple string output (easy for Skript)
 *  - No side effects
 *  - Safe on reload
 *  - Expandable in future (Chronicle / WorldReaction)
 * =====================================================
 */
public class ProgressionPlaceholder extends PlaceholderExpansion {

    private final JavaPlugin plugin;

    public ProgressionPlaceholder(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * -------------------------------------------------
     * Placeholder identifier
     *
     * IMPORTANT:
     *  - This is the prefix after %
     *  - Usage example:
     *      %normieprogression_visibility%
     * -------------------------------------------------
     */
    @Override
    public String getIdentifier() {
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
        // Required for PlaceholderAPI internal expansion
        return plugin.getName();
    }

    @Override
    public boolean persist() {
        // Keep expansion registered across /papi reload
        return true;
    }

    /**
     * -------------------------------------------------
     * Main placeholder handler
     * -------------------------------------------------
     *
     * params = everything after %normieprogression_
     *
     * Example:
     *   %normieprogression_mark_SURVIVAL%
     *        └── params = "mark_SURVIVAL"
     */
    @Override
    public String onPlaceholderRequest(Player player, String params) {

        // Safety check (PlaceholderAPI can call this async / without player)
        if (player == null) {
            return "";
        }

        String id = params.toLowerCase();

        /* =============================================
         * VISIBILITY
         * ============================================= */
        if (id.equals("visibility")) {
            return String.valueOf(
                    VisibilityManager.getVisibility(player.getUniqueId())
            );
        }

        /* =============================================
         * TIER
         * ============================================= */

        // Full enum name (e.g. T3_RESPONDED)
        if (id.equals("tier")) {
            Tier tier = TierManager.getTier(player.getUniqueId());
            return tier.name();
        }

        // Numeric tier level (0,1,2,3...)
        if (id.equals("tier_level")) {
            Tier tier = TierManager.getTier(player.getUniqueId());
            return String.valueOf(tier.getLevel());
        }

        /* =============================================
         * MARKS
         * ============================================= */

        // Pattern: mark_<TYPE>
        // Example: mark_loss / mark_SURVIVAL
        if (id.startsWith("mark_")) {

            String raw = params.substring(5); // remove "mark_"
            MarkType type;

            try {
                type = MarkType.valueOf(raw.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Unknown mark type requested
                return "false";
            }

            boolean has = MarkStorage.hasMark(
                    player.getUniqueId(),
                    type
            );

            // IMPORTANT:
            //  - Return "true" / "false" as STRING
            //  - This makes Skript integration MUCH easier
            return String.valueOf(has);
        }

        /* =============================================
         * UNKNOWN PLACEHOLDER
         * ============================================= */
        return "";
    }
}
