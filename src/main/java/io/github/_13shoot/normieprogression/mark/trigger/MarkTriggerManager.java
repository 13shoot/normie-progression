package io.github._13shoot.normieprogression.mark.trigger;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MarkTriggerManager {

    private static final List<MarkTrigger> TRIGGERS = new ArrayList<>();

    public static void register(MarkTrigger trigger) {
        TRIGGERS.add(trigger);
    }

    public static void handle(Player player) {
        for (MarkTrigger trigger : TRIGGERS) {
            trigger.handle(player);
        }
    }
}
