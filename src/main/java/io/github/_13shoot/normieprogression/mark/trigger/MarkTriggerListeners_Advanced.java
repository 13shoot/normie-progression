package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;
import java.util.UUID;

public class MarkTriggerListeners_Advanced implements Listener {

    /* -------------------------------------------------
     * RECOGNITION
     * Advancement bundle (LOCKED LIST)
     * ------------------------------------------------- */
    private static final List<String> REQUIRED_ADVANCEMENTS = List.of(
            "minecraft:story/mine_diamond",
            "minecraft:nether/root",
            "minecraft:nether/return_to_sender",
            "minecraft:nether/obtain_ancient_debris",
            "minecraft:adventure/zombie_doctor",
            "minecraft:end/enter_end_gateway",
            "minecraft:end/kill_dragon",
            "minecraft:end/dragon_egg",
            "minecraft:end/elytra",
            "minecraft:end/find_end_city"
    );

    /* -------------------------------------------------
     * SURVIVAL + PERSISTENCE (PERMANENT)
     * State-based check on join
     * ------------------------------------------------- */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        VisibilityData v = VisibilityManager.get(id);
        if (v == null) return;

        int day = v.getDaysAlive();

        // SURVIVAL: survived 30+ in-game days
        if (day >= 30 && !MarkStorage.hasMark(id, MarkType.SURVIVAL)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.SURVIVAL,
                    day,
                    -1
            ));
        }

        // PERSISTENCE: survived 90+ in-game days
        if (day >= 90 && !MarkStorage.hasMark(id, MarkType.PERSISTENCE)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.PERSISTENCE,
                    day,
                    -1
            ));
        }
    }

    /* -------------------------------------------------
     * RECOGNITION (PERMANENT)
     * Full advancement bundle completion
     * ------------------------------------------------- */
    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        if (MarkStorage.hasMark(id, MarkType.RECOGNITION)) return;

        for (String key : REQUIRED_ADVANCEMENTS) {

            Advancement adv = Bukkit.getAdvancement(
                    NamespacedKey.fromString(key)
            );
            if (adv == null) return;

            AdvancementProgress progress = p.getAdvancementProgress(adv);
            if (!progress.isDone()) return;
        }

        int day = VisibilityManager.get(id).getDaysAlive();

        MarkStorage.addMark(id, new MarkData(
                MarkType.RECOGNITION,
                day,
                -1
        ));
    }
}
