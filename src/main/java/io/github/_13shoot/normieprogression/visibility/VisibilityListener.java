package io.github._13shoot.normieprogression.visibility;

import io.github._13shoot.normieprogression.gate.GateService;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VisibilityListener implements Listener {

    private final JavaPlugin plugin;

    // Decay factors (LOCKED FOR v0.2.3)
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

                        // 1️⃣ increment in-game day
                        d.incrementDaysAlive();

                        int currentDay = d.getDaysAlive();

                        // 2️⃣ CLEANUP expired marks (🔥 สำคัญที่สุด)
                        MarkStorage.cleanupExpired(currentDay);

                        // 3️⃣ re-evaluate gates
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

        // Visibility decay (v0.2.3)
        d.decayOnDeath(DAYS_DECAY_FACTOR, ECON_DECAY_FACTOR);

        // Optional but safe: cleanup again after reset
        MarkStorage.cleanupExpired(d.getDaysAlive());

        GateService.evaluate(p);
    }
}
