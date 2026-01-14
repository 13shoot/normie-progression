package io.github._13shoot.normieprogression.visibility;

public class VisibilityData {

    private int daysAlive;

    // NEW: source attribution
    private int visibilityDays;
    private int visibilityEconomy;

    public VisibilityData() {
        this.daysAlive = 0;
        this.visibilityDays = 0;
        this.visibilityEconomy = 0;
    }

    /* ---------------- Days Alive ---------------- */

    public int getDaysAlive() {
        return daysAlive;
    }

    public void incrementDaysAlive() {
        this.daysAlive++;
        // days contribute visibility (1:1 for now)
        this.visibilityDays++;
    }

    public void resetDaysAlive() {
        this.daysAlive = 0;
    }

    /* ---------------- Visibility (sources) ---------------- */

    public int getVisibilityDays() {
        return visibilityDays;
    }

    public int getVisibilityEconomy() {
        return visibilityEconomy;
    }

    public void addEconomyVisibility(int amount) {
        if (amount <= 0) return;
        this.visibilityEconomy += amount;
    }

    public int getTotalVisibility() {
        return visibilityDays + visibilityEconomy;
    }

    /* ---------------- Decay hooks (used later) ---------------- */

    // called on death in Step 2
    public void decayOnDeath(double daysFactor, double economyFactor) {
        this.visibilityDays =
                Math.max(0, (int) Math.floor(this.visibilityDays * daysFactor));
        this.visibilityEconomy =
                Math.max(0, (int) Math.floor(this.visibilityEconomy * economyFactor));
    }
}
