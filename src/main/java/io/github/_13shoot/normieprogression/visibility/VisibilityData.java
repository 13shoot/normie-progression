package io.github._13shoot.normieprogression.visibility;

public class VisibilityData {

    private int daysAlive;
    private int visibilityDays;
    private int visibilityEconomy;

    // NEW: daily tracking for soft cap
    private int economyVisibilityToday;

    public VisibilityData() {
        this.daysAlive = 0;
        this.visibilityDays = 0;
        this.visibilityEconomy = 0;
        this.economyVisibilityToday = 0;
    }

    public int getDaysAlive() {
        return daysAlive;
    }

    public void incrementDaysAlive() {
        this.daysAlive++;
        this.visibilityDays++;
        this.economyVisibilityToday = 0; // reset daily cap
    }

    public void resetDaysAlive() {
        this.daysAlive = 0;
    }

    public int getVisibilityDays() {
        return visibilityDays;
    }

    public int getVisibilityEconomy() {
        return visibilityEconomy;
    }

    public int getEconomyVisibilityToday() {
        return economyVisibilityToday;
    }

    public void addEconomyVisibility(int amount) {
        if (amount <= 0) return;
        this.visibilityEconomy += amount;
    }

    public void addEconomyVisibilityToday(int amount) {
        if (amount <= 0) return;
        this.economyVisibilityToday += amount;
    }

    public int getTotalVisibility() {
        return visibilityDays + visibilityEconomy;
    }

    public void decayOnDeath(double daysFactor, double economyFactor) {
        this.visibilityDays =
                Math.max(0, (int) Math.floor(this.visibilityDays * daysFactor));
        this.visibilityEconomy =
                Math.max(0, (int) Math.floor(this.visibilityEconomy * economyFactor));
    }
}
