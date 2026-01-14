package io.github._13shoot.normieprogression.gate.impl;

import io.github._13shoot.normieprogression.gate.Gate;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.entity.Player;

public class RememberedGate implements Gate {

    private static final int REQUIRED_DAYS = 30;
    private static final int REQUIRED_VISIBILITY = 250;

    @Override
    public String getId() {
        return "remembered";
    }

    @Override
    public boolean check(Player player) {

        VisibilityData data =
                VisibilityManager.get(player.getUniqueId());

        if (data == null) return false;

        return data.getDaysAlive() >= REQUIRED_DAYS
                && VisibilityManager.getVisibility(
                        player.getUniqueId()
                ) >= REQUIRED_VISIBILITY;
    }
}
