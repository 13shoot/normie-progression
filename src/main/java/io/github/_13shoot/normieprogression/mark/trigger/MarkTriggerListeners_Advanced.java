package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.*;

public class MarkTriggerListeners_Advanced implements Listener {

    // -------------------------------------------------
    // RECOGNITION
    // Advancement bundle (locked list)
    // -------------------------------------------------
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

    /* ------------------------------------------------
     * SURVIVAL + PERSISTENCE
     * Checked on join (state based)
     * ------------------------------------------------ */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        VisibilityData v = VisibilityManager.get(id);
        if (v == null) return;

        long now = System.currentTimeMillis();

        // SURVIVAL: basic world endurance (30+ days alive)
        if (v.getDaysAlive() >= 30 && !MarkStorage.hasMark(id, MarkType.SURVIVAL)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.SURVIVAL,
                    now,
                    -1,
                    0
            ));
        }

        // PERSISTENCE: long-term recovery & continuation (90+ days alive)
        if (v.getDaysAlive() >= 90 && !MarkStorage.hasMark(id, MarkType.PERSISTENCE)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.PERSISTENCE,
                    now,
                    -1,
                    0
            ));
        }
    }
    
    /* ------------------------------------------------
     * RECOGNITION
     * Advancement bundle completion
     * ------------------------------------------------ */
    @EventHandler
    public void onAdvancement(PlayerAdvancementDoneEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        if (MarkStorage.hasMark(id, MarkType.RECOGNITION)) return;

        for (String key : REQUIRED_ADVANCEMENTS) {

            Advancement adv = Bukkit.getAdvancement(org.bukkit.NamespacedKey.fromString(key));
            if (adv == null) return;

            AdvancementProgress prog = p.getAdvancementProgress(adv);
            if (!prog.isDone()) {
                return;
            }
        }

        MarkStorage.addMark(id, new MarkData(
                MarkType.RECOGNITION,
                System.currentTimeMillis(),
                -1,
                0
        ));
    }
}
