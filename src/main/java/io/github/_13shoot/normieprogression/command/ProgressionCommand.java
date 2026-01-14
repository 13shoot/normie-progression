package io.github._13shoot.normieprogression.command;

import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ProgressionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command is player-only.");
            return true;
        }

        VisibilityData data =
                VisibilityManager.get(player.getUniqueId());

        if (data == null) {
            player.sendMessage("§cNo visibility data found.");
            return true;
        }

        int visibility =
                VisibilityManager.getVisibility(player.getUniqueId());

        player.sendMessage("§6=== Normie Progression ===");
        player.sendMessage("§7Days Alive: §f" + data.getDaysAlive());
        player.sendMessage("§7Money Earned: §f" + String.format("%.2f", data.getTotalMoneyEarned()));
        player.sendMessage("§7Visibility: §e" + visibility);

        return true;
    }
}
