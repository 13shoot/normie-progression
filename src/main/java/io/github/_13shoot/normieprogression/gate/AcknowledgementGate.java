package io.github._13shoot.normieprogression.gate.impl;

import io.github._13shoot.normieprogression.gate.Gate;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.entity.Player;

public class AcknowledgementGate implements Gate {

    private static final int REQUIRED_DAYS = 10;
    private static final int REQUIRED_VISIBILITY = 50;

    @Override
    public String getId() {
        return "acknowledgement";
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
