package io.github._13shoot.normieprogression.worldreaction.reaction;

import io.github._13shoot.normieprogression.worldreaction.ReactionContext;
import io.github._13shoot.normieprogression.worldreaction.WorldReaction;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Random;

/**
 * FeedbackReaction (D)
 *
 * Subtle narrative feedback:
 * - Fires rarely
 * - Only after world reactions have likely taken effect
 * - Never explains mechanics
 */
public class FeedbackReaction implements WorldReaction {

    private static final Random RANDOM = new Random();

    // Very low chance to avoid spam (~5%)
    private static final double FEEDBACK_CHANCE = 0.05;

    @Override
    public String getId() {
        return "FeedbackReaction";
    }

    @Override
    public void evaluate(ReactionContext context) {

        // Only at night, when world reactions feel noticeable
        if (!context.isNight()) {
            return;
        }

        // Very rare feedback
        if (RANDOM.nextDouble() > FEEDBACK_CHANCE) {
            return;
        }

        Player player = context.getPlayer();

        // Extremely subtle: sound only, no chat spam
        player.playSound(
                player.getLocation(),
                Sound.AMBIENT_CAVE,
                0.3f,
                0.8f
        );
    }
}
