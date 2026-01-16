package io.github._13shoot.normieprogression.mark;

/**
 * MarkData represents a single Mark owned by a player.
 *
 * IMPORTANT DESIGN NOTES:
 * - This class uses GAME DAYS, not real-world time.
 * - All time values are based on VisibilityData.getDaysAlive().
 * - This makes marks deterministic, persistent, and fair.
 */
public class MarkData {

    // -------------------------------------------------
    // Core identity
    // -------------------------------------------------
    private final MarkType type;

    // -------------------------------------------------
    // Game-time based fields
    // -------------------------------------------------
    private final int obtainedDay;        // Day when the mark was obtained
    private final int expiresDay;         // Day when the mark expires (-1 = permanent)
    private final int cooldownUntilDay;   // Day until this mark can be obtained again

    /**
     * Constructor for MarkData.
     *
     * @param type              Mark type
     * @param obtainedDay       Game day when the mark was obtained
     * @param expiresDay        Game day when the mark expires (-1 if permanent)
     * @param cooldownUntilDay  Game day when the mark can be re-obtained
     */
    public MarkData(
            MarkType type,
            int obtainedDay,
            int expiresDay,
            int cooldownUntilDay
    ) {
        this.type = type;
        this.obtainedDay = obtainedDay;
        this.expiresDay = expiresDay;
        this.cooldownUntilDay = cooldownUntilDay;
    }

    // -------------------------------------------------
    // Basic getters
    // -------------------------------------------------
    public MarkType getType() {
        return type;
    }

    public int getObtainedDay() {
        return obtainedDay;
    }

    public int getExpiresDay() {
        return expiresDay;
    }

    public int getCooldownUntilDay() {
        return cooldownUntilDay;
    }

    // -------------------------------------------------
    // State helpers
    // -------------------------------------------------

    /**
     * @return true if this mark is permanent
     */
    public boolean isPermanent() {
        return expiresDay < 0;
    }

    /**
     * Check if this mark is expired based on current game day.
     *
     * @param currentDay Current game day (from VisibilityData)
     * @return true if expired
     */
    public boolean isExpired(int currentDay) {
        return !isPermanent() && currentDay >= expiresDay;
    }

    /**
     * Check if this mark is still on cooldown.
     *
     * @param currentDay Current game day (from VisibilityData)
     * @return true if still on cooldown
     */
    public boolean isOnCooldown(int currentDay) {
        return currentDay < cooldownUntilDay;
    }
}
