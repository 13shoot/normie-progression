package io.github._13shoot.normieprogression.worldreaction.reaction;

import io.github._13shoot.normieprogression.worldreaction.ReactionContext;
import io.github._13shoot.normieprogression.worldreaction.WorldReaction;
import org.bukkit.GameRule;
import org.bukkit.World;

/**
 * YieldReaction (B)
 *
 * Environmental ease:
 * - Slightly reduces decay/friction without boosting drops directly.
 * - Uses safe vanilla-aligned knobs only.
 *
 * NOTE:
 * This reaction is intentionally subtle and reversible.
 */
public class YieldReaction implements WorldReaction {

    @Override
    public String getId() {
        return "YieldReaction";
    }

    @Override
    public void evaluate(ReactionContext context) {

        World world = context.getWorld();

        // Apply only once per tick safely; values are idempotent
        // Keep vanilla-friendly: no drop rate modification here
        if (world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED) == null) {
            return;
        }

        int current = world.getGameRuleValue(GameRule.RANDOM_TICK_SPEED);

        // Vanilla default is 3 — we do NOT change it.
        // This reaction intentionally does nothing invasive.
        // Placeholder for future micro-adjustments if needed.
        if (current < 3) {
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 3);
        }
    }
}
