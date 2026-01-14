package io.github._13shoot.normieprogression.tier;

import org.bukkit.entity.Player;

public class TierService {

    private TierService() {
    }

    public static double getEconomicMultiplier(Player player) {

        Tier tier =
                TierManager.getTier(player.getUniqueId());

        return TierEffectRegistry
                .get(tier)
                .getEconomicMultiplier();
    }

    public static int getVisibilityBonus(Player player) {

        Tier tier =
                TierManager.getTier(player.getUniqueId());

        return TierEffectRegistry
                .get(tier)
                .getVisibilityBonus();
    }
}
