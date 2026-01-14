package io.github._13shoot.normieprogression.command;

import io.github._13shoot.normieprogression.NormieProgression;
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
            case "save" -> handleSave(admin);
            case "reload" -> handleReload(admin);
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

        // /np tier set <player> <0-4>
        if (args.length == 4 && args[1].equalsIgnoreCase("set")) {

            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                admin.sendMessage("§cPlayer not found.");
                return;
            }

            try {
                int number = Integer.parseInt(args[3]);
                Tier tier = Tier.fromNumber(number);
                TierManager.forceSet(target.getUniqueId(), tier);

                admin.sendMessage("§aSet tier of " + target.getName()
                        + " to " + tier.name());
            } catch (NumberFormatException e) {
                admin.sendMessage("§cTier must be a number (0–4).");
            }
            return;
        }

        // /np tier reset <player>
        if (args.length == 3 && args[1].equalsIgnoreCase("reset")) {

            Player target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                admin.sendMessage("§cPlayer not found.");
                return;
            }

            TierManager.forceSet(target.getUniqueId(), Tier.T0_UNSEEN);
            admin.sendMessage("§eReset tier of " + target.getName());
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
        admin.sendMessage("§7Visibility (days): §f" + vData.getVisibilityDays());
        admin.sendMessage("§7Visibility (economy): §f" + vData.getVisibilityEconomy());
        admin.sendMessage("§7Visibility (total): §e" + (vData.getVisibilityDays() + vData.getVisibilityEconomy()));
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

    /* ------------------------------------------------
     * /np save
     * ------------------------------------------------ */
    private void handleSave(Player admin) {

        NormieProgression plugin =
                (NormieProgression) Bukkit.getPluginManager()
                        .getPlugin("NormieProgression");

        if (plugin != null) {
            plugin.forceSave();
            admin.sendMessage("§aProgression data saved.");
        }
    }

    /* ------------------------------------------------
     * /np reload
     * ------------------------------------------------ */
    private void handleReload(Player admin) {

        NormieProgression plugin =
                (NormieProgression) Bukkit.getPluginManager()
                        .getPlugin("NormieProgression");

        if (plugin != null) {
            plugin.forceSave();
            plugin.forceLoad();
            admin.sendMessage("§aProgression data reloaded.");
        }
    }

    private void sendUsage(Player player) {
        player.sendMessage("§6=== Normie Progression Admin ===");
        player.sendMessage("§7/np tier");
        player.sendMessage("§7/np tier set <player> <0-4>");
        player.sendMessage("§7/np tier reset <player>");
        player.sendMessage("§7/np debug <player>");
        player.sendMessage("§7/np gate eval <player>");
        player.sendMessage("§7/np save");
        player.sendMessage("§7/np reload");
    }
}
