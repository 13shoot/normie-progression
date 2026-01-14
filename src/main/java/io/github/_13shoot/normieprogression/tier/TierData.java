package io.github._13shoot.normieprogression.tier;

import java.util.UUID;

public class TierData {

    private final UUID playerId;
    private Tier tier;

    public TierData(UUID playerId) {
        this.playerId = playerId;
        this.tier = Tier.T0_UNSEEN;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public Tier getTier() {
        return tier;
    }

    public void promoteTo(Tier newTier) {
        if (newTier.getLevel() > this.tier.getLevel()) {
            this.tier = newTier;
        }
    }
}
