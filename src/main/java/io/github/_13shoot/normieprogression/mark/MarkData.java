package io.github._13shoot.normieprogression.mark;

public class MarkData {

    private final MarkType type;

    // in-game days
    private int obtainedAt;
    private int expiresAt;     // -1 = permanent

    public MarkData(
            MarkType type,
            int obtainedAt,
            int expiresAt,
    ) {
        this.type = type;
        this.obtainedAt = obtainedAt;
        this.expiresAt = expiresAt;
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

    public int getObtainedDay() {
        return obtainedAt;
    }

    public int getExpiresDay() {
        return expiresAt;
    }


    public int getExpiresAt() {
        return expiresAt;
    }
}
