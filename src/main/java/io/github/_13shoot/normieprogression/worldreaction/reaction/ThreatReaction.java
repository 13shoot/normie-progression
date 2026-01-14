package io.github._13shoot.normieprogression.worldreaction.reaction;

import io.github._13shoot.normieprogression.worldreaction.ReactionContext;
import io.github._13shoot.normieprogression.worldreaction.WorldReaction;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Monster;

import java.util.Collection;

/**
 * ThreatReaction (A)
 *
 * Subtle threat perception:
 * - Hostile mobs hesitate to approach high-tier players.
 * - Implemented by clearing target if mob is too far.
 *
 * Safe, reversible, no persistence.
 */
public class ThreatReaction implements WorldReaction {

    // distance threshold where mobs may disengage
    private static final double DISENGAGE_DISTANCE = 12.0;

    @Override
    public String getId() {
        return "ThreatReaction";
    }

    @Override
    public void evaluate(ReactionContext context) {

        Player player = context.getPlayer();

        // Only meaningful at night or low light contexts
        if (!context.isNight()) {
            return;
        }

        Collection<Monster> monsters =
                player.getWorld().getEntitiesByClass(Monster.class);

        for (Monster monster : monsters) {

            if (!(monster instanceof Mob mob)) continue;

            if (mob.getTarget() == null) continue;
            if (!mob.getTarget().getUniqueId().equals(player.getUniqueId())) continue;

            double distance = mob.getLocation().distance(player.getLocation());

            // If monster is far, let it disengage
            if (distance > DISENGAGE_DISTANCE) {
                mob.setTarget(null);
            }
        }
    }
}
