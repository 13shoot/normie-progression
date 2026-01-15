package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import org.bukkit.entity.Player;

import java.util.*;

public class DeathTrigger implements MarkTrigger {

    private static final Map<UUID, Deque<Long>> DEATHS = new HashMap<>();

    @Override
    public void handle(Player player) {

        UUID id = player.getUniqueId();
        long now = System.currentTimeMillis();

        Deque<Long> times = DEATHS.computeIfAbsent(id, k -> new ArrayDeque<>());
        times.addLast(now);

        while (!times.isEmpty() && now - times.peekFirst() > 10 * 60 * 1000) {
            times.pollFirst();
        }

        if (times.size() >= 2 && !MarkStorage.hasMark(id, MarkType.BLOOD)) {

            long expires = now + (3L * 24 * 60 * 60 * 1000);

            MarkStorage.addMark(id, new MarkData(
                    MarkType.BLOOD,
                    now,
                    expires,
                    now + (24L * 60 * 60 * 1000)
            ));
        }
    }
}
