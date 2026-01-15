package io.github._13shoot.normieprogression.mark;

public enum MarkType {

    SURVIVAL(true),
    HUNGER(false),
    PERSISTENCE(true),
    BLOOD(false),
    RECOGNITION(true),
    LOSS(false),
    TRADE(false),
    COLD(true),
    INFLUENCE(true),
    FEAR(false),
    WITNESS(true),
    FAVOR(false);

    private final boolean permanent;

    MarkType(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isPermanent() {
        return permanent;
    }
}
