package io.github._13shoot.normieprogression.worldreaction;

/**
 * WorldReaction
 *
 * Base interface for all world reactions.
 * WorldReaction must be:
 * - time-based
 * - stateless (no persistence here)
 * - safe to skip silently
 */
public interface WorldReaction {

    /**
     * Unique id for debug/logging.
     */
    String getId();

    /**
     * Evaluate reaction based on context.
     * Implementation may apply subtle effects.
     */
    void evaluate(ReactionContext context);
}
