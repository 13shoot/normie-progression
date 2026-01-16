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

import java.util.*;

public class DeathTriggerListener implements Listener {

    // Track recent deaths in game-days
    private static final Map<UUID, Deque<int>> DEATH_DAYS = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();
        UUID id = player.getUniqueId();

        VisibilityData v = VisibilityManager.get(id);
        if (v == null) return;

        int nowDay = v.getDaysAlive();

        /* ---------------------------------------------
         * Remove all temporary marks on death
         * --------------------------------------------- */
        for (MarkData mark : new ArrayList<>(MarkStorage.getMarks(id))) {
            if (!mark.isPermanent()) {
                MarkStorage.removeMark(id, mark.getType());
            }
        }

        /* ---------------------------------------------
         * Track death history (by game days)
         * --------------------------------------------- */
        Deque<int> days = DEATH_DAYS.computeIfAbsent(id, k -> new ArrayDeque<>());
        days.addLast(nowDay);

        while (!days.isEmpty() && nowDay - days.peekFirst() > 1) {
            days.pollFirst();
        }

        /* ---------------------------------------------
         * BLOOD
         * - 2 deaths within 1 in-game day
         * --------------------------------------------- */
        if (days.size() >= 2 && !MarkStorage.hasMark(id, MarkType.BLOOD)) {

            MarkStorage.addMark(id, new MarkData(
                    MarkType.BLOOD,
                    nowDay,
                    nowDay + 3, // expires in 3 in-game days
                    nowDay + 1  // cooldown 1 day
            ));
        }

        /* ---------------------------------------------
         * LOSS
         * - Any death
         * --------------------------------------------- */
        if (!MarkStorage.hasMark(id, MarkType.LOSS)) {

            MarkStorage.addMark(id, new MarkData(
                    MarkType.LOSS,
                    nowDay,
                    nowDay + 3,
                    nowDay + 1
            ));
        }
    }
}
