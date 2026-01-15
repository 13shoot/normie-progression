package io.github._13shoot.normieprogression.mark;

public class MarkData {

    private final MarkType type;
    private long obtainedAt;
    private long expiresAt; // -1 for permanent
    private long cooldownUntil;

    public MarkData(MarkType type, long obtainedAt, long expiresAt, long cooldownUntil) {
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

    public boolean isExpired(long now) {
        return !isPermanent() && expiresAt > 0 && now >= expiresAt;
    }

    public boolean isOnCooldown(long now) {
        return now < cooldownUntil;
    }

    public long getExpiresAt() {
        return expiresAt;
    }
}
