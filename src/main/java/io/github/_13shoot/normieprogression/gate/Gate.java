package io.github._13shoot.normieprogression.gate;

import org.bukkit.entity.Player;

/**
 * Gate represents a progression condition.
 */
public interface Gate {

    String getId();

    boolean check(Player player);
}
