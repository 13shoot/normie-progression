package io.github._13shoot.normieprogression.visibility;

import io.github._13shoot.normieprogression.gate.GateService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VisibilityListener implements Listener {

    private final JavaPlugin plugin;

    // Decay factors (LOCKED FOR v0.2.3)
    // days: small penalty, economy: stronger penalty
    private static final double DAYS_DECAY_FACTOR = 0.90;     // -10%
    private static final double ECON_DECAY_FACTOR = 0.70;     // -30%

    public VisibilityListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /* ---------------- Day counter ---------------- */

    public void startDayCounter() {

        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        VisibilityData d =
                                VisibilityManager.getOrCreate(p.getUniqueId());
                        d.incrementDaysAlive();
                        GateService.evaluate(p);
                    }
                },
                24000L,
                24000L
        );
    }

    /* ---------------- Death ---------------- */

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {

        Player p = e.getEntity();
        VisibilityData d =
                VisibilityManager.getOrCreate(p.getUniqueId());

        // Existing behavior
        d.resetDaysAlive();

        // NEW: visibility decay on death (v0.2.3)
        d.decayOnDeath(DAYS_DECAY_FACTOR, ECON_DECAY_FACTOR);

        GateService.evaluate(p);
    }
}
