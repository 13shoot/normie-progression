package io.github._13shoot.normieprogression.visibility;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class VisibilityListener implements Listener {

    private final JavaPlugin plugin;

    public VisibilityListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /* ------------------------------------------------
     * Init player data on join
     * ------------------------------------------------ */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        VisibilityManager.getOrCreate(event.getPlayer().getUniqueId());
    }

    /* ------------------------------------------------
     * Reset daysAlive on death
     * ------------------------------------------------ */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        VisibilityData data =
                VisibilityManager.get(event.getEntity().getUniqueId());

        if (data != null) {
            data.resetDaysAlive();
        }
    }

    /* ------------------------------------------------
     * Start day counter task
     * ------------------------------------------------ */
    public void startDayCounter() {

        // Run once every Minecraft day (24000 ticks)
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                () -> VisibilityManager
                        .getOrCreateForAll()
                        .forEach(VisibilityData::incrementDaysAlive),
                24000L,
                24000L
        );
    }
}
