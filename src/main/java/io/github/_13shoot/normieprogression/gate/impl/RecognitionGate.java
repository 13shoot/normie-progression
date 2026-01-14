package io.github._13shoot.normieprogression.gate.impl;

import io.github._13shoot.normieprogression.gate.Gate;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import org.bukkit.entity.Player;

public class RecognitionGate implements Gate {

    private static final int REQUIRED_VISIBILITY = 10;
    private static final int REQUIRED_DAYS = 3;

    @Override
    public String getId() {
        return "recognition";
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
