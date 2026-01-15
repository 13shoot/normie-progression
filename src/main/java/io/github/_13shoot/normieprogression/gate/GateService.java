package io.github._13shoot.normieprogression.gate;

import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import org.bukkit.entity.Player;

import java.util.Map;

public class GateService {

    private GateService() {
    }

    // -------------------------------------------------
    // Gate → Tier mapping (locked)
    // -------------------------------------------------
    private static final Map<String, Tier> GATE_TIER_MAP = Map.of(
            "recognition", Tier.T1_RECOGNIZED,
            "acknowledgement", Tier.T2_ACKNOWLEDGED,
            "responded", Tier.T3_RESPONDED,
            "remembered", Tier.T4_REMEMBERED
    );

    public static void evaluate(Player player) {

        for (Gate gate : GateRegistry.getAll()) {

            if (!gate.check(player)) {
                continue;
            }

            // Promote tier if mapped
            Tier targetTier = GATE_TIER_MAP.get(gate.getId());
            if (targetTier != null) {
                TierManager.promote(
                        player.getUniqueId(),
                        targetTier
                );
            }

            // Chronicle hook (gate milestone)
            io.github._13shoot.normieprogression.chronicle.GateChronicleHook
                    .onGatePassed(player, gate.getId());
        }
    }
}
