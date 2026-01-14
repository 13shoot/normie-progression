package io.github._13shoot.normieprogression.command;

import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.tier.TierService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TierDebugCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Player only.");
            return true;
        }

        Tier tier =
                TierManager.getTier(player.getUniqueId());

        double ecoMult =
                TierService.getEconomicMultiplier(player);

        int visBonus =
                TierService.getVisibilityBonus(player);

        player.sendMessage("§6=== Normie Tier ===");
        player.sendMessage("§7Tier: §e" + tier.name());
        player.sendMessage("§7Label: §f" + tier.getLabel());
        player.sendMessage("§7Level: §f" + tier.getLevel());
        player.sendMessage("§7Economic Multiplier: §a" + ecoMult);
        player.sendMessage("§7Visibility Bonus: §b+" + visBonus);

        return true;
    }
}
