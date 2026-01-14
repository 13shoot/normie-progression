package io.github._13shoot.normieprogression.tier;

/**
 * Tier represents a progression state.
 * Tier is monotonic: once increased, it does not decrease.
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

    public static Tier fromLevel(int level) {
        for (Tier tier : values()) {
            if (tier.level == level) {
                return tier;
            }
        }
        return T0_UNSEEN;
    }
}
