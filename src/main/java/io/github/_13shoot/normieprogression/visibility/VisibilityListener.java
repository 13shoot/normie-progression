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

        d.resetDaysAlive(); // daysAlive reset (existing behavior)

        // decay applied in Step 2 (v0.2.3)
        // d.decayOnDeath(daysFactor, economyFactor);

        GateService.evaluate(p);
    }
}
