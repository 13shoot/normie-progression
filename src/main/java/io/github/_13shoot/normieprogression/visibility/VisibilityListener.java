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

    /* ------------------------------------------------
     * Days Alive Counter (every Minecraft day)
     * ------------------------------------------------ */
    public void startDayCounter() {

        // 24000 ticks = 1 Minecraft day
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> {
                    for (Player player : Bukkit.getOnlinePlayers()) {

                        VisibilityData data =
                                VisibilityManager.getOrCreate(
                                        player.getUniqueId()
                                );

                        data.incrementDaysAlive();

                        // 🔑 Evaluate gate when day increases
                        GateService.evaluate(player);
                    }
                },
                24000L,
                24000L
        );
    }

    /* ------------------------------------------------
     * Reset on death
     * ------------------------------------------------ */
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        VisibilityData data =
                VisibilityManager.getOrCreate(
                        player.getUniqueId()
                );

        data.resetDaysAlive();

        // Optional: gate re-evaluation (safe)
        GateService.evaluate(player);
    }
}
