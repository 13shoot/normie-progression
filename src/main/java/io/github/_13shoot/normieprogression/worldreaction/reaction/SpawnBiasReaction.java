package io.github._13shoot.normieprogression.worldreaction.reaction;

import io.github._13shoot.normieprogression.worldreaction.ReactionContext;
import io.github._13shoot.normieprogression.worldreaction.WorldReaction;
import org.bukkit.World;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;

import java.util.Collection;

/**
 * SpawnBiasReaction (C)
 *
 * Presence bias:
 * - Does NOT change spawn rules.
 * - Does NOT cancel spawns.
 * - Subtly prunes excess hostile mobs far from the player.
 *
 * Goal:
 * World feels less aggressive, not empty.
 */
public class SpawnBiasReaction implements WorldReaction {

    // Radius to consider entities around player
    private static final double CHECK_RADIUS = 32.0;

    // Hard cap for hostile mobs near player (very conservative)
    private static final int MAX_HOSTILES_NEAR = 8;

    @Override
    public String getId() {
        return "SpawnBiasReaction";
    }

    @Override
    public void evaluate(ReactionContext context) {

        World world = context.getWorld();

        Collection<Entity> nearby =
                world.getNearbyEntities(
                        context.getPlayer().getLocation(),
                        CHECK_RADIUS,
                        CHECK_RADIUS,
                        CHECK_RADIUS
                );

        int hostileCount = 0;

        for (Entity entity : nearby) {
            if (entity instanceof Monster) {
                hostileCount++;
            }
        }

        // If hostile presence is already reasonable, do nothing
        if (hostileCount <= MAX_HOSTILES_NEAR) {
            return;
        }

        // Gently remove excess hostile mobs farthest from player
        for (Entity entity : nearby) {

            if (!(entity instanceof Monster monster)) continue;

            double distance =
                    entity.getLocation().distance(context.getPlayer().getLocation());

            // Only prune far mobs, never those close to the player
            if (distance > 20.0) {
                monster.remove();
                hostileCount--;

                if (hostileCount <= MAX_HOSTILES_NEAR) {
                    break;
                }
            }
        }
    }
}
