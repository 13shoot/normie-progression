package io.github._13shoot.normieprogression.visibility;

import java.util.UUID;

public class VisibilityData {

    private final UUID playerId;
    private int daysAlive;
    private double totalMoneyEarned;

    public VisibilityData(UUID playerId) {
        this.playerId = playerId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public int getDaysAlive() {
        return daysAlive;
    }

    public void setDaysAlive(int daysAlive) {
        this.daysAlive = daysAlive;
    }

    public double getTotalMoneyEarned() {
        return totalMoneyEarned;
    }

    public void addMoneyEarned(double amount) {
        this.totalMoneyEarned += amount;
    }
}
