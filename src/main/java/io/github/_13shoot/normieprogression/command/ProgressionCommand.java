package io.github._13shoot.normieprogression.command;

import io.github._13shoot.normieprogression.gate.GateRegistry;
import io.github._13shoot.normieprogression.gate.GateService;
import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ProgressionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.isOp()) {
            sender.sendMessage("§cAdmin only.");
            return true;
        }

        if (args.length < 1) {
            sendHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "gate" -> handleGate(sender, args);
            case "visibility" -> handleVisibility(sender, args);
            case "tier" -> handleTier(sender, args);
            case "reset" -> handleReset(sender, args);
            case "debug" -> handleDebug(sender, args);
            default -> sendHelp(sender);
        }

        return true;
    }

    private void handleGate(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /np gate <eval|debug> <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        if (args[1].equalsIgnoreCase("eval")) {
            GateService.evaluate(target);
            sender.sendMessage("§aGate evaluation triggered for " + target.getName());
            return;
        }

        if (args[1].equalsIgnoreCase("debug")) {

            sender.sendMessage("§e=== Gate Debug ===");
            sender.sendMessage("Player: " + target.getName());

            GateRegistry.getAll().forEach(gate -> {
                boolean pass = gate.check(target);
                sender.sendMessage("- " + gate.getId() + ": " + (pass ? "§aPASS" : "§cFAIL"));
            });
        }
    }

    private void handleVisibility(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /np visibility <set|add|reset> <player> [amount]");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        UUID id = target.getUniqueId();
        VisibilityData data = VisibilityManager.getOrCreate(id);

        switch (args[1].toLowerCase()) {

            case "reset" -> {
                data.decayOnDeath(0.0, 0.0);
                sender.sendMessage("§aVisibility reset for " + target.getName());
            }

            case "add" -> {
                int amt = Integer.parseInt(args[3]);
                VisibilityManager.addEconomyVisibility(id, amt);
                sender.sendMessage("§aAdded visibility to " + target.getName());
            }

            case "set" -> {
                int total = Integer.parseInt(args[3]);
                data.decayOnDeath(0.0, 0.0);
                VisibilityManager.addEconomyVisibility(id, total);
                sender.sendMessage("§aVisibility set for " + target.getName());
            }
        }
    }

    private void handleTier(CommandSender sender, String[] args) {

        if (args.length < 4) {
            sender.sendMessage("§cUsage: /np tier <set|reset> <player> <level>");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        if (args[1].equalsIgnoreCase("reset")) {
            TierManager.forceSet(target.getUniqueId(), Tier.T0_UNSEEN);
            sender.sendMessage("§aTier reset.");
            return;
        }

        int level = Integer.parseInt(args[3]);
        Tier tier = Tier.fromNumber(level);
        TierManager.forceSet(target.getUniqueId(), tier);
        sender.sendMessage("§aTier set to " + tier.name());
    }

    private void handleReset(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /np reset <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        UUID id = target.getUniqueId();

        TierManager.forceSet(id, Tier.T0_UNSEEN);

        VisibilityData data = VisibilityManager.getOrCreate(id);
        data.decayOnDeath(0.0, 0.0);

        sender.sendMessage("§aProgression fully reset for " + target.getName());
    }

    private void handleDebug(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /np debug <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        VisibilityData v = VisibilityManager.get(target.getUniqueId());

        sender.sendMessage("§e=== Progression Debug ===");
        sender.sendMessage("Player: " + target.getName());
        sender.sendMessage("Days Alive: " + v.getDaysAlive());
        sender.sendMessage("Visibility (days): " + v.getVisibilityDays());
        sender.sendMessage("Visibility (economy): " + v.getVisibilityEconomy());
        sender.sendMessage("Visibility (total): " + v.getTotalVisibility());
        sender.sendMessage("Tier: " + TierManager.getTier(target.getUniqueId()));
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§e=== Normie Progression Admin ===");
        sender.sendMessage("/np gate eval <player>");
        sender.sendMessage("/np gate debug <player>");
        sender.sendMessage("/np visibility set|add|reset <player> <amount>");
        sender.sendMessage("/np tier set|reset <player> <level>");
        sender.sendMessage("/np reset <player>");
        sender.sendMessage("/np debug <player>");
    }
}
