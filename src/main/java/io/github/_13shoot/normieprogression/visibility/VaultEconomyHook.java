package io.github._13shoot.normieprogression.visibility;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultEconomyHook {

    private static Economy economy;

    private VaultEconomyHook() {
    }

    public static boolean init() {

        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp =
                Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;

        economy = rsp.getProvider();
        return economy != null;
    }

    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Convert money earned to economy visibility.
     * This is intentionally simple for v0.2.3.
     */
    public static void onMoneyEarned(
            VisibilityData data,
            double amount
    ) {

        if (amount <= 0) return;

        // simple mapping: every X money = +1 visibility
        int visibilityGain = (int) Math.floor(amount / 10.0);

        if (visibilityGain <= 0) return;

        data.addEconomyVisibility(visibilityGain);
    }
}
