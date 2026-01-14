package io.github._13shoot.normieprogression.visibility;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class VaultEconomyHook {

    private static Economy economy;

    private VaultEconomyHook() {
    }

    public static boolean init() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        var rsp = Bukkit.getServicesManager()
                .getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        economy = rsp.getProvider();
        return economy != null;
    }

    /**
     * Call this when money is deposited to a player.
     * Only positive delta will be counted.
     */
    public static void recordDeposit(OfflinePlayer player, double amount) {

        if (economy == null) return;
        if (amount <= 0) return;

        VisibilityData data =
                VisibilityManager.getOrCreate(player.getUniqueId());

        data.addMoneyEarned(amount);
    }

    public static Economy getEconomy() {
        return economy;
    }
}
