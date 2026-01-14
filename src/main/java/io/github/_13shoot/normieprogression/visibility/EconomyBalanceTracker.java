package io.github._13shoot.normieprogression.visibility;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyBalanceTracker {

    private final JavaPlugin plugin;
    private final Economy economy;

    private final Map<UUID, Double> lastBalance = new HashMap<>();

    public EconomyBalanceTracker(JavaPlugin plugin, Economy economy) {
        this.plugin = plugin;
        this.economy = economy;
    }

    public void start() {

        // Run every 10 seconds (200 ticks)
        Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::checkBalances,
                200L,
                200L
        );
    }

    private void checkBalances() {

        for (Player player : Bukkit.getOnlinePlayers()) {

            UUID id = player.getUniqueId();
            double current = economy.getBalance(player);
            double previous = lastBalance.getOrDefault(id, current);

            if (current > previous) {
                double earned = current - previous;

                VaultEconomyHook.onMoneyEarned(data, earned);
            }

            lastBalance.put(id, current);
        }
    }
}
