package io.github._13shoot.normieprogression;

import io.github._13shoot.normieprogression.command.ProgressionCommand;
import io.github._13shoot.normieprogression.placeholder.ProgressionPlaceholder;
import io.github._13shoot.normieprogression.visibility.EconomyBalanceTracker;
import io.github._13shoot.normieprogression.visibility.VaultEconomyHook;
import io.github._13shoot.normieprogression.visibility.VisibilityListener;
import io.github._13shoot.normieprogression.visibility.VisibilityStorage;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NormieProgression extends JavaPlugin {

    private VisibilityStorage visibilityStorage;

    @Override
    public void onEnable() {

        /* ------------------------------------------------
         * PlaceholderAPI
         * ------------------------------------------------ */
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ProgressionPlaceholder(this).register();
            getLogger().info("Progression placeholders registered.");
        } else {
            getLogger().warning("PlaceholderAPI not found. Placeholders disabled.");
        }

        /* ------------------------------------------------
         * Visibility (daysAlive)
         * ------------------------------------------------ */
        VisibilityListener visibilityListener =
                new VisibilityListener(this);

        Bukkit.getPluginManager().registerEvents(
                visibilityListener,
                this
        );

        visibilityListener.startDayCounter();

        /* ------------------------------------------------
         * Visibility persistence (load)
         * ------------------------------------------------ */
        visibilityStorage = new VisibilityStorage(this);
        visibilityStorage.loadAll();

        /* ------------------------------------------------
         * Economy visibility (Vault)
         * ------------------------------------------------ */
        if (VaultEconomyHook.init()) {

            Economy economy = VaultEconomyHook.getEconomy();

            EconomyBalanceTracker tracker =
                    new EconomyBalanceTracker(this, economy);

            tracker.start();

            getLogger().info("Economy visibility tracking enabled.");
        } else {
            getLogger().warning("Vault economy not found. Economic visibility disabled.");
        }

        /* ------------------------------------------------
         * Debug command
         * ------------------------------------------------ */
        if (getCommand("np") != null) {
            getCommand("np").setExecutor(new ProgressionCommand());
        }

        getLogger().info("NormieProgression enabled.");
    }

    @Override
    public void onDisable() {

        /* ------------------------------------------------
         * Save visibility data
         * ------------------------------------------------ */
        if (visibilityStorage != null) {
            visibilityStorage.saveAll();
        }

        getLogger().info("NormieProgression disabled.");
    }
}
