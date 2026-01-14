package io.github._13shoot.normieprogression.worldreaction;

import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.tier.Tier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * WorldReactionManager
 *
 * v0.3.0 STEP 1
 * - Time-based only (every 10 seconds)
 * - Tier >= 2 only
 * - No gameplay changes here
 * - Safe orchestrator for all reactions
 */
public class WorldReactionManager {

    private static final List<WorldReaction> REACTIONS = new ArrayList<>();

    private final JavaPlugin plugin;

    // 10 seconds = 200 ticks
    private static final long INTERVAL_TICKS = 200L;

    public WorldReactionManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Register all world reactions here.
     * STEP 1: empty by design.
     */
    public void registerDefaults() {
        // Reactions will be registered in STEP 2
    }

    /**
     * Start world reaction evaluation loop.
     */
    public void start() {

        Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::tick,
                INTERVAL_TICKS,
                INTERVAL_TICKS
        );
    }

    private void tick() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            Tier tier = TierManager.getTier(player.getUniqueId());

            // World Reaction starts at Tier 2
            if (tier == null || tier.getLevel() < 2) {
                continue;
            }

            ReactionContext context = ReactionContext.from(player);

            for (WorldReaction reaction : REACTIONS) {
                try {
                    reaction.evaluate(context);
                } catch (Exception e) {
                    plugin.getLogger().warning(
                            "[WorldReaction] Reaction failed: "
                                    + reaction.getId()
                                    + " (" + e.getMessage() + ")"
                    );
                }
            }
        }
    }

    public static void register(WorldReaction reaction) {
        REACTIONS.add(reaction);
    }

    public static List<WorldReaction> getRegistered() {
        return REACTIONS;
    }
}
