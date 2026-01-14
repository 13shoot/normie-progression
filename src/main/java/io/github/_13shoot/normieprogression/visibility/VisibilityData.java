package io.github._13shoot.normieprogression.visibility;

import java.util.UUID;

public class VisibilityData {

    private final UUID playerId;

    // Survival axis
    private int daysAlive;

    // Economic axis
    private double totalMoneyEarned;

    public VisibilityData(UUID playerId) {
        this.playerId = playerId;
        this.daysAlive = 0;
        this.totalMoneyEarned = 0.0;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    /* -----------------------------
     * Days Alive
     * ----------------------------- */
    public int getDaysAlive() {
        return daysAlive;
    }

    public void incrementDaysAlive() {
        this.daysAlive++;
    }

    public void resetDaysAlive() {
        this.daysAlive = 0;
    }

    /* -----------------------------
     * Money Earned
     * ----------------------------- */
    public double getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    public void addMoneyEarned(double amount) {
        if (amount <= 0) return;
        this.totalMoneyEarned += amount;
    }
}
