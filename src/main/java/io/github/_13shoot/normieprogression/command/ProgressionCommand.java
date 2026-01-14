package io.github._13shoot.normieprogression.command;

import io.github._13shoot.normieprogression.gate.GateService;
import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.tier.TierService;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.Bukkit;
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

        if (!(sender instanceof Player admin)) {
            sender.sendMessage("Player only.");
            return true;
        }

        if (!admin.isOp()) {
            admin.sendMessage("§cAdmin only.");
            return true;
        }

        if (args.length == 0) {
            sendUsage(admin);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "tier" -> handleTier(admin, args);
            case "debug" -> handleDebug(admin, args);
            case "gate" -> handleGate(admin, args);
            default -> sendUsage(admin);
        }

        return true;
    }

    /* ------------------------------------------------
     * /np tier ...
     * ------------------------------------------------ */
    private void handleTier(Player admin, String[] args) {

        if (args.length == 1) {
            Tier tier = TierManager.getTier(admin.getUniqueId());
            admin.sendMessage("§7Your Tier: §e" + tier.name());
            return;
        }

        if (args.length == 4 && args[1].equalsIgnoreCase("set")) {

            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                admin.sendMessage("§cPlayer not found.");
                return;
            }

            try {
                Tier tier = Tier.valueOf(args[3].toUpperCase());
                TierManager.promote(target.getUniqueId(), tier);
                admin.sendMessage("§aSet tier of " + target.getName()
                        + " to " + tier.name());
            } catch (IllegalArgumentException e) {
                admin.sendMessage("§cInvalid tier.");
            }
            return;
        }

        sendUsage(admin);
    }

    /* ------------------------------------------------
     * /np debug <player>
     * ------------------------------------------------ */
    private void handleDebug(Player admin, String[] args) {

        if (args.length != 2) {
            sendUsage(admin);
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            admin.sendMessage("§cPlayer not found.");
            return;
        }

        VisibilityData vData =
                VisibilityManager.get(target.getUniqueId());

        Tier tier =
                TierManager.getTier(target.getUniqueId());

        double eco =
                TierService.getEconomicMultiplier(target);

        int visBonus =
                TierService.getVisibilityBonus(target);

        admin.sendMessage("§6=== Progression Debug ===");
        admin.sendMessage("§7Player: §f" + target.getName());
        admin.sendMessage("§7Days Alive: §f" +
                (vData != null ? vData.getDaysAlive() : 0));
        admin.sendMessage("§7Visibility (raw): §f" +
                VisibilityManager.getVisibility(target.getUniqueId()));
        admin.sendMessage("§7Tier: §e" + tier.name());
        admin.sendMessage("§7Eco Mult: §a" + eco);
        admin.sendMessage("§7Visibility Bonus: §b+" + visBonus);
    }

    /* ------------------------------------------------
     * /np gate eval <player>
     * ------------------------------------------------ */
    private void handleGate(Player admin, String[] args) {

        if (args.length != 3 || !args[1].equalsIgnoreCase("eval")) {
            sendUsage(admin);
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            admin.sendMessage("§cPlayer not found.");
            return;
        }

        GateService.evaluate(target);
        admin.sendMessage("§aGate evaluation triggered for "
                + target.getName());
    }

    private void sendUsage(Player player) {
        player.sendMessage("§6=== Normie Progression Admin ===");
        player.sendMessage("§7/np tier");
        player.sendMessage("§7/np tier set <player> <tier>");
        player.sendMessage("§7/np debug <player>");
        player.sendMessage("§7/np gate eval <player>");
    }
}
