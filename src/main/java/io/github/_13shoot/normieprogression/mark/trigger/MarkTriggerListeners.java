package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MarkTriggerListeners implements Listener {

    // -------------------------------
    // TRACKERS
    // -------------------------------
    private static final Map<UUID, Integer> COMMAND_COUNT = new HashMap<>();
    private static final Map<UUID, Long> LAST_BALANCE_CHECK = new HashMap<>();
    private static final Map<UUID, Double> LAST_BALANCE = new HashMap<>();
    private static final Random RANDOM = new Random();

    /* ------------------------------------------------
     * COMMAND USAGE → INFLUENCE (permanent)
     * 30+ commands in 1 day
     * ------------------------------------------------ */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        int count = COMMAND_COUNT.getOrDefault(id, 0) + 1;
        COMMAND_COUNT.put(id, count);

        if (count >= 30 && !MarkStorage.hasMark(id, MarkType.INFLUENCE)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.INFLUENCE,
                    System.currentTimeMillis(),
                    -1,
                    0
            ));
        }
    }

    /* ------------------------------------------------
     * RANDOM FAVOR → FAVOR (temporary)
     * Random chance while moving
     * ------------------------------------------------ */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        long now = System.currentTimeMillis();

        if (RANDOM.nextInt(5000) == 0 && !MarkStorage.hasMark(id, MarkType.FAVOR)) {

            MarkStorage.addMark(id, new MarkData(
                    MarkType.FAVOR,
                    now,
                    now + (3L * 24 * 60 * 60 * 1000),
                    now + (24L * 60 * 60 * 1000)
            ));
        }
    }

    /* ------------------------------------------------
     * FEAR → FEAR (temporary)
     * Near-death damage
     * ------------------------------------------------ */
    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player p)) return;

        double finalHealth = p.getHealth() - e.getFinalDamage();
        if (finalHealth <= 2.0 && finalHealth > 0) {

            UUID id = p.getUniqueId();
            long now = System.currentTimeMillis();

            if (!MarkStorage.hasMark(id, MarkType.FEAR)) {
                MarkStorage.addMark(id, new MarkData(
                        MarkType.FEAR,
                        now,
                        now + (3L * 24 * 60 * 60 * 1000),
                        now + (24L * 60 * 60 * 1000)
                ));
            }
        }
    }

    /* ------------------------------------------------
     * SURVIVAL / WITNESS
     * - Days alive without death
     * ------------------------------------------------ */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        VisibilityData v = VisibilityManager.get(id);
        if (v == null) return;

        if (v.getDaysAlive() >= 120 && !MarkStorage.hasMark(id, MarkType.WITNESS)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.WITNESS,
                    System.currentTimeMillis(),
                    -1,
                    0
            ));
        }
    }

    /* ------------------------------------------------
     * HUNGER → HUNGER (temporary)
     * ------------------------------------------------ */
    @EventHandler
    public void onMoveHunger(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        if (p.getFoodLevel() > 6) return;

        UUID id = p.getUniqueId();
        long now = System.currentTimeMillis();

        if (!MarkStorage.hasMark(id, MarkType.HUNGER)) {
            MarkStorage.addMark(id, new MarkData(
                    MarkType.HUNGER,
                    now,
                    now + (3L * 24 * 60 * 60 * 1000),
                    now + (24L * 60 * 60 * 1000)
            ));
        }
    }

    /* ------------------------------------------------
     * COLD → COLD (permanent)
     * - Cold biome presence (simplified)
     * ------------------------------------------------ */
    @EventHandler
    public void onCold(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) return;

        Material block = p.getLocation().getBlock().getType();
        UUID id = p.getUniqueId();

        if (block == Material.SNOW_BLOCK || block == Material.ICE) {
            if (!MarkStorage.hasMark(id, MarkType.COLD)) {
                MarkStorage.addMark(id, new MarkData(
                        MarkType.COLD,
                        System.currentTimeMillis(),
                        -1,
                        0
                ));
            }
        }
    }

    /* ------------------------------------------------
     * ECONOMY → TRADE (temporary)
     * - Balance swing detection
     * ------------------------------------------------ */
    public static void startEconomyWatcher(Economy economy) {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    UUID id = p.getUniqueId();
                    double balance = economy.getBalance(p);
                    double last = LAST_BALANCE.getOrDefault(id, balance);

                    if (Math.abs(balance - last) >= 1000
                            && !MarkStorage.hasMark(id, MarkType.TRADE)) {

                        long now = System.currentTimeMillis();

                        MarkStorage.addMark(id, new MarkData(
                                MarkType.TRADE,
                                now,
                                now + (3L * 24 * 60 * 60 * 1000),
                                now + (24L * 60 * 60 * 1000)
                        ));
                    }

                    LAST_BALANCE.put(id, balance);
                }
            }
        }.runTaskTimerAsynchronously(
                Bukkit.getPluginManager().getPlugin("NormieProgression"),
                20L * 60,
                20L * 60
        );
    }
}
