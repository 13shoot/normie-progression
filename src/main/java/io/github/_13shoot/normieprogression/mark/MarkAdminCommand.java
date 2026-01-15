package io.github._13shoot.normieprogression.mark;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MarkAdminCommand {

    public static boolean handle(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /np mark <add|remove|clear> ...");
            return true;
        }

        switch (args[1].toLowerCase()) {

            case "add" -> handleAdd(sender, args);
            case "remove" -> handleRemove(sender, args);
            case "clear" -> handleClear(sender, args);
            default -> sender.sendMessage("§cUnknown mark command.");
        }

        return true;
    }

    private static void handleAdd(CommandSender sender, String[] args) {

        if (args.length < 4) {
            sender.sendMessage("§cUsage: /np mark add <player> <mark>");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        MarkType type;
        try {
            type = MarkType.valueOf(args[3].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid mark type.");
            return;
        }

        UUID id = target.getUniqueId();
        long now = System.currentTimeMillis();

        if (MarkStorage.hasMark(id, type)) {
            sender.sendMessage("§ePlayer already has this mark.");
            return;
        }

        long expires = type.isPermanent() ? -1 : now + (3L * 24 * 60 * 60 * 1000); // 3 days
        long cooldown = now + (1L * 24 * 60 * 60 * 1000); // 1 day

        MarkData data = new MarkData(type, now, expires, cooldown);
        MarkStorage.addMark(id, data);

        sender.sendMessage("§aMark " + type.name() + " added to " + target.getName());
    }

    private static void handleRemove(CommandSender sender, String[] args) {

        if (args.length < 4) {
            sender.sendMessage("§cUsage: /np mark remove <player> <mark>");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        MarkType type;
        try {
            type = MarkType.valueOf(args[3].toUpperCase());
        } catch (IllegalArgumentException e) {
            sender.sendMessage("§cInvalid mark type.");
            return;
        }

        MarkStorage.removeMark(target.getUniqueId(), type);
        sender.sendMessage("§aMark removed.");
    }

    private static void handleClear(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§cUsage: /np mark clear <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        MarkStorage.getMarks(target.getUniqueId()).clear();
        sender.sendMessage("§aAll marks cleared for " + target.getName());
    }
}
