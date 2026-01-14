package io.github._13shoot.normieprogression.tier;

/**
 * Defines internal modifiers granted by a Tier.
 * These values are read by other systems.
 */
public class TierEffectProfile {

    private final double economicMultiplier;
    private final int visibilityBonus;

    public TierEffectProfile(double economicMultiplier, int visibilityBonus) {
        this.economicMultiplier = economicMultiplier;
        this.visibilityBonus = visibilityBonus;
    }

    public double getEconomicMultiplier() {
        return economicMultiplier;
    }

    public int getVisibilityBonus() {
        return visibilityBonus;
    }
}
