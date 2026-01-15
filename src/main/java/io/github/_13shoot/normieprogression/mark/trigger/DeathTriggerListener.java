package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;

import java.util.*;

public class DeathTriggerListener implements Listener {

    // Track recent deaths per player
    private static final Map<UUID, Deque<Long>> DEATHS = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UUID id = player.getUniqueId();
        long now = System.currentTimeMillis();

        /* ---------------------------------------------
         * Remove all temporary marks on death
         * --------------------------------------------- */
        for (MarkData mark : new ArrayList<>(MarkStorage.getMarks(id))) {
            if (!mark.isPermanent()) {
                MarkStorage.removeMark(id, mark.getType());
            }
        }

        /* ---------------------------------------------
         * BLOOD mark trigger
         * - 2 deaths within 10 minutes
         * --------------------------------------------- */
        Deque<Long> times = DEATHS.computeIfAbsent(id, k -> new ArrayDeque<>());
        times.addLast(now);

        while (!times.isEmpty() && now - times.peekFirst() > 10 * 60 * 1000) {
            times.pollFirst();
        }

        if (times.size() >= 2 && !MarkStorage.hasMark(id, MarkType.BLOOD)) {

            long expiresAt = now + (3L * 24 * 60 * 60 * 1000); // 3 days
            long cooldownUntil = now + (24L * 60 * 60 * 1000); // 1 day

            MarkStorage.addMark(id, new MarkData(
                    MarkType.BLOOD,
                    now,
                    expiresAt,
                    cooldownUntil
            ));
        }
    }
}
