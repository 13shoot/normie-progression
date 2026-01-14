package io.github._13shoot.normieprogression;


import io.github._13shoot.normieprogression.placeholder.ProgressionPlaceholder;
import io.github._13shoot.normieprogression.visibility.VisibilityListener;
import io.github._13shoot.normieprogression.visibility.EconomyBalanceTracker;
import io.github._13shoot.normieprogression.visibility.VaultEconomyHook;
import io.github._13shoot.normieprogression.command.ProgressionCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class NormieProgression extends JavaPlugin {

    @Override
    public void onEnable() {

        // Register Placeholder
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ProgressionPlaceholder(this).register();
            getLogger().info("Progression placeholders registered.");
        }

        // Register Visibility Listener
        VisibilityListener visibilityListener =
                new VisibilityListener(this);

        Bukkit.getPluginManager().registerEvents(
                visibilityListener,
                this
        );

        // Start day counter
        visibilityListener.startDayCounter();

        getLogger().info("NormieProgression enabled.");
        // ---------------------------------------------
        // Hook Vault economy for visibility
        // ---------------------------------------------
        if (VaultEconomyHook.init()) {

            EconomyBalanceTracker tracker =
                    new EconomyBalanceTracker(
                            this,
                            VaultEconomyHook.getEconomy()
                    );

            tracker.start();
            getLogger().info("Economy visibility tracking enabled.");
        } else {
            getLogger().warning("Vault economy not found. Economic visibility disabled.");
        }
        // Register debug command
        getCommand("np").setExecutor(new ProgressionCommand());
    }
}
