package io.github._13shoot.normieprogression.gate;

import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import org.bukkit.entity.Player;

public class GateService {

    private GateService() {
    }

    public static void evaluate(Player player) {

        for (Gate gate : GateRegistry.getAll()) {

            if (!gate.check(player)) {
                continue;
            }

            // Gate_01 → Tier 1
            if (gate.getId().equals("recognition")) {
                TierManager.promote(
                        player.getUniqueId(),
                        Tier.T1_RECOGNIZED
                );
            }
        }
    }
}
