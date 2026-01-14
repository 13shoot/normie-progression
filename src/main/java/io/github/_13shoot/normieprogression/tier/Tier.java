package io.github._13shoot.normieprogression.tier;

/**
 * Represents progression tier of a player.
 * Tier is monotonic: once promoted, it should not be downgraded
 * except by admin tools.
 */
public enum Tier {

    T0_UNSEEN(0, "Unseen"),
    T1_RECOGNIZED(1, "Recognized"),
    T2_ACKNOWLEDGED(2, "Acknowledged"),
    T3_RESPONDED(3, "Responded"),
    T4_REMEMBERED(4, "Remembered");

    private final int level;
    private final String label;

    Tier(int level, String label) {
        this.level = level;
        this.label = label;
    }

    public int getLevel() {
        return level;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Convert numeric tier (admin-friendly) to Tier enum.
     * Used by admin commands.
     */
    public static Tier fromNumber(int number) {
        return switch (number) {
            case 1 -> T1_RECOGNIZED;
            case 2 -> T2_ACKNOWLEDGED;
            case 3 -> T3_RESPONDED;
            case 4 -> T4_REMEMBERED;
            default -> T0_UNSEEN;
        };
    }

    /**
     * Convert stored tier level to Tier enum.
     * Used by TierStorage when loading from yml.
     */
    public static Tier fromLevel(int level) {
        return switch (level) {
            case 1 -> T1_RECOGNIZED;
            case 2 -> T2_ACKNOWLEDGED;
            case 3 -> T3_RESPONDED;
            case 4 -> T4_REMEMBERED;
            default -> T0_UNSEEN;
        };
    }
}
