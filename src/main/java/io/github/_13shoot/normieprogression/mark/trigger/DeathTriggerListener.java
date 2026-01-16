package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DeathTriggerListener implements Listener {

    // Track recent deaths in in-game days
    private static final Map<UUID, Deque<Integer>> DEATH_DAYS = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UUID id = player.getUniqueId();

        VisibilityData v = VisibilityManager.get(id);
        if (v == null) return;

        int today = v.getDaysAlive();

        /* ---------------------------------------------
         * Remove all temporary marks on death
         * --------------------------------------------- */
        for (MarkData mark : new ArrayList<>(MarkStorage.getMarks(id))) {
            if (!mark.isPermanent()) {
                MarkStorage.removeMark(id, mark.getType());
            }
        }

        /* ---------------------------------------------
         * Track death history (by in-game day)
         * --------------------------------------------- */
        Deque<Integer> days =
                DEATH_DAYS.computeIfAbsent(id, k -> new ArrayDeque<>());

        days.addLast(today);

        // Keep only deaths within the last 1 in-game day
        while (!days.isEmpty() && today - days.peekFirst() > 1) {
            days.pollFirst();
        }

        /* ---------------------------------------------
         * BLOOD
         * - 2 deaths within 1 in-game day
         * --------------------------------------------- */
        if (days.size() >= 2) {

            int day = today;

            // 1) cooldown check
            if (MarkStorage.isOnCooldown(id, MarkType.BLOOD, day)) return;

            // 2) already has mark
            if (MarkStorage.hasMark(id, MarkType.BLOOD)) return;

            // 3) add mark (3 days)
            MarkStorage.addMark(id, new MarkData(
                    MarkType.BLOOD,
                    day,
                    day + 3
            ));

            // 4) set cooldown (หมด + พัก 3 วัน)
            MarkStorage.setCooldown(
                    id,
                    MarkType.BLOOD,
                    day + 6
            );
        }

        /* ---------------------------------------------
         * LOSS
         * - Any death
         * --------------------------------------------- */
        int day = today;

        // 1) cooldown
        if (MarkStorage.isOnCooldown(id, MarkType.LOSS, day)) return;

        // 2) already active
        if (MarkStorage.hasMark(id, MarkType.LOSS)) return;

        // 3) add mark
        MarkStorage.addMark(id, new MarkData(
                MarkType.LOSS,
                day,
                day + 3
        ));

        // 4) cooldown
        MarkStorage.setCooldown(
                id,
                MarkType.LOSS,
                day + 6
        );
        }
    }
}
