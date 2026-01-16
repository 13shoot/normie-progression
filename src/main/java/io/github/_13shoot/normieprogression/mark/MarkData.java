package io.github._13shoot.normieprogression.mark;

public class MarkData {

    private final MarkType type;

    // in-game days
    private int obtainedAt;
    private int expiresAt;     // -1 = permanent
    private int cooldownUntil;

    public MarkData(
            MarkType type,
            int obtainedAt,
            int expiresAt,
            int cooldownUntil
    ) {
        this.type = type;
        this.obtainedAt = obtainedAt;
        this.expiresAt = expiresAt;
        this.cooldownUntil = cooldownUntil;
    }

    public MarkType getType() {
        return type;
    }

    public boolean isPermanent() {
        return type.isPermanent();
    }

    public boolean isExpired(int currentDay) {
        return !isPermanent()
                && expiresAt > 0
                && currentDay >= expiresAt;
    }

    public boolean isOnCooldown(int currentDay) {
        return currentDay < cooldownUntil;
    }

    public int getExpiresAt() {
        return expiresAt;
    }
}
