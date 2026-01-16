package io.github._13shoot.normieprogression.mark.trigger;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MarkTriggerListeners implements Listener {

    private static final Map<UUID, Integer> COMMAND_COUNT = new HashMap<>();
    private static final Map<UUID, Double> LAST_BALANCE = new HashMap<>();
    private static final Random RANDOM = new Random();

    // common config for temporary marks
    private static final int TEMP_DURATION = 3;   // days active
    private static final int TEMP_COOLDOWN = 3;   // days after expire

    /* ------------------------------------------------
     * INFLUENCE (permanent)
     * ------------------------------------------------ */
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();

        int count = COMMAND_COUNT.getOrDefault(id, 0) + 1;
        COMMAND_COUNT.put(id, count);

        if (count >= 30 && !MarkStorage.hasMark(id, MarkType.INFLUENCE)) {
            int day = VisibilityManager.get(id).getDaysAlive();

            MarkStorage.addMark(id, new MarkData(
                    MarkType.INFLUENCE,
                    day,
                    -1
            ));
        }
    }

    /* ------------------------------------------------
     * FAVOR (temporary)
     * ------------------------------------------------ */
    @EventHandler
    public void onMove(PlayerMoveEvent e) {

        if (RANDOM.nextInt(5000) != 0) return;

        Player p = e.getPlayer();
        UUID id = p.getUniqueId();
        int day = VisibilityManager.get(id).getDaysAlive();

        if (MarkStorage.isOnCooldown(id, MarkType.FAVOR, day)) return;
        if (MarkStorage.hasMark(id, MarkType.FAVOR)) return;

        MarkStorage.addMark(id, new MarkData(
                MarkType.FAVOR,
                day,
                day + TEMP_DURATION
        ));

        MarkStorage.setCooldown(
                id,
                MarkType.FAVOR,
                day + TEMP_DURATION + TEMP_COOLDOWN
        );
    }

    /* ------------------------------------------------
     * FEAR (temporary)
     * ------------------------------------------------ */
    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player p)) return;

        double hp = p.getHealth() - e.getFinalDamage();
        if (hp > 2.0 || hp <= 0) return;

        UUID id = p.getUniqueId();
        int day = VisibilityManager.get(id).getDaysAlive();

        if (MarkStorage.isOnCooldown(id, MarkType.FEAR, day)) return;
        if (MarkStorage.hasMark(id, MarkType.FEAR)) return;

        MarkStorage.addMark(id, new MarkData(
                MarkType.FEAR,
                day,
                day + TEMP_DURATION
        ));

        MarkStorage.setCooldown(
                id,
                MarkType.FEAR,
                day + TEMP_DURATION + TEMP_COOLDOWN
        );
    }

    /* ------------------------------------------------
     * WITNESS (permanent)
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
                    v.getDaysAlive(),
                    -1
            ));
        }
    }

    /* ------------------------------------------------
     * HUNGER (temporary)
     * ------------------------------------------------ */
    @EventHandler
    public void onMoveHunger(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        if (p.getFoodLevel() > 6) return;

        UUID id = p.getUniqueId();
        int day = VisibilityManager.get(id).getDaysAlive();

        if (MarkStorage.isOnCooldown(id, MarkType.HUNGER, day)) return;
        if (MarkStorage.hasMark(id, MarkType.HUNGER)) return;

        MarkStorage.addMark(id, new MarkData(
                MarkType.HUNGER,
                day,
                day + TEMP_DURATION
        ));

        MarkStorage.setCooldown(
                id,
                MarkType.HUNGER,
                day + TEMP_DURATION + TEMP_COOLDOWN
        );
    }

    /* ------------------------------------------------
     * COLD (permanent)
     * ------------------------------------------------ */
    @EventHandler
    public void onCold(PlayerMoveEvent e) {

        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.CREATIVE) return;

        Material block = p.getLocation().getBlock().getType();
        UUID id = p.getUniqueId();

        if ((block == Material.SNOW_BLOCK || block == Material.ICE)
                && !MarkStorage.hasMark(id, MarkType.COLD)) {

            int day = VisibilityManager.get(id).getDaysAlive();

            MarkStorage.addMark(id, new MarkData(
                    MarkType.COLD,
                    day,
                    -1
            ));
        }
    }

    /* ------------------------------------------------
     * TRADE (temporary)
     * ------------------------------------------------ */
    public static void startEconomyWatcher(Economy economy) {

        new BukkitRunnable() {
            @Override
            public void run() {

                for (Player p : Bukkit.getOnlinePlayers()) {

                    UUID id = p.getUniqueId();
                    double balance = economy.getBalance(p);
                    double last = LAST_BALANCE.getOrDefault(id, balance);

                    int day = VisibilityManager.get(id).getDaysAlive();

                    if (Math.abs(balance - last) >= 1000) {

                        if (!MarkStorage.isOnCooldown(id, MarkType.TRADE, day)
                                && !MarkStorage.hasMark(id, MarkType.TRADE)) {

                            MarkStorage.addMark(id, new MarkData(
                                    MarkType.TRADE,
                                    day,
                                    day + TEMP_DURATION
                            ));

                            MarkStorage.setCooldown(
                                    id,
                                    MarkType.TRADE,
                                    day + TEMP_DURATION + TEMP_COOLDOWN
                            );
                        }
                    }

                    LAST_BALANCE.put(id, balance);
                }
            }
        }.runTaskTimer(
                Bukkit.getPluginManager().getPlugin("NormieProgression"),
                20L * 60,
                20L * 60
        );
    }
}
