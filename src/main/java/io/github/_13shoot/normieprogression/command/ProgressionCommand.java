package io.github._13shoot.normieprogression.command;

import io.github._13shoot.normieprogression.NormieProgression;
import io.github._13shoot.normieprogression.chronicle.ChronicleEntry;
import io.github._13shoot.normieprogression.chronicle.ChronicleStorage;
import io.github._13shoot.normieprogression.gate.GateRegistry;
import io.github._13shoot.normieprogression.gate.GateService;
import io.github._13shoot.normieprogression.gui.ProgressionGUI;
import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class ProgressionCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Player GUI
        if (args.length >= 1 && args[0].equalsIgnoreCase("gui")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cPlayers only.");
                return true;
            }
            ProgressionGUI.open((Player) sender);
            return true;
        }

        // Admin only
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
            case "mark" -> handleMark(sender, args);
            case "status" -> handleStatus(sender, args);
            case "chronicle" -> handleChronicle(sender, args);
            case "reload" -> handleReload(sender);
            default -> sendHelp(sender);
        }
        return true;
    }

    /* ================= STATUS ================= */
    private void handleStatus(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§cUsage: /np status <player>");
            return;
        }

        Player p = Bukkit.getPlayer(args[1]);
        if (p == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        VisibilityData v = VisibilityManager.get(p.getUniqueId());

        sender.sendMessage("§6=== Progression Status ===");
        sender.sendMessage("§7Player: §f" + p.getName());
        sender.sendMessage("§7Tier: §a" + TierManager.getTier(p.getUniqueId()));
        sender.sendMessage("§7Days Alive: §f" + v.getDaysAlive());
        sender.sendMessage("§7Visibility: §f" + v.getTotalVisibility());
        sender.sendMessage("§7Marks: §f" + MarkStorage.getMarks(p.getUniqueId()).size());
        sender.sendMessage("§7Chronicles: §f" + ChronicleStorage.get(p.getUniqueId()).size());
    }

    /* ================= MARK ================= */
    private void handleMark(CommandSender sender, String[] args) {

        if (args.length < 3) {
            sender.sendMessage("§c/np mark add <player> <mark>");
            sender.sendMessage("§c/np mark addall <player>");
            sender.sendMessage("§c/np mark reset <player>");
            sender.sendMessage("§c/np mark list <player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[2]);
        if (target == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        UUID id = target.getUniqueId();
        long nowDay = VisibilityManager.get(id).getDaysAlive();

        switch (args[1].toLowerCase()) {

            case "list" -> {
                sender.sendMessage("§6=== Marks of " + target.getName() + " ===");
                for (MarkData data : MarkStorage.getMarks(id)) {
                    String color = data.getType().isPermanent() ? "§a" : "§c";
                    sender.sendMessage(color + data.getType().name());
                }
            }

            case "add" -> {
                MarkType type = MarkType.valueOf(args[3].toUpperCase());

                if (MarkStorage.hasMark(id, type)) {
                    sender.sendMessage("§eAlready has this mark.");
                    return;
                }

                long expires = type.isPermanent() ? -1 : nowDay + 3;
                MarkStorage.addMark(id, new MarkData(
                        type,
                        nowDay,
                        expires,
                        nowDay
                ));

                sender.sendMessage("§aAdded mark: " + type.name());
            }

            case "addall" -> {
                for (MarkType t : MarkType.values()) {
                    if (!MarkStorage.hasMark(id, t)) {
                        long expires = t.isPermanent() ? -1 : nowDay + 3;
                        MarkStorage.addMark(id, new MarkData(
                                t,
                                nowDay,
                                expires,
                                nowDay
                        ));
                    }
                }
                sender.sendMessage("§aAll marks added.");
            }

            case "reset" -> {
                for (MarkType t : MarkType.values()) {
                    MarkStorage.removeMark(id, t);
                }
                sender.sendMessage("§aAll marks removed.");
            }
        }
    }

    /* ================= CHRONICLE ================= */
    private void handleChronicle(CommandSender sender, String[] args) {

        if (args.length < 3 || !args[1].equalsIgnoreCase("list")) {
            sender.sendMessage("§cUsage: /np chronicle list <player>");
            return;
        }

        Player p = Bukkit.getPlayer(args[2]);
        if (p == null) {
            sender.sendMessage("§cPlayer not found.");
            return;
        }

        sender.sendMessage("§6=== Chronicle of " + p.getName() + " ===");
        for (ChronicleEntry e : ChronicleStorage.get(p.getUniqueId())) {
            sender.sendMessage("§f- " + e.getTitle());
        }
    }

    /* ================= RELOAD ================= */
    private void handleReload(CommandSender sender) {
        NormieProgression plugin = NormieProgression.getPlugin(NormieProgression.class);
        plugin.forceLoad();
        sender.sendMessage("§aNormieProgression reloaded.");
    }

    /* ================= GATE ================= */
    private void handleGate(CommandSender s, String[] a) {

        if (a.length < 3) {
            s.sendMessage("§cUsage: /np gate <eval|debug> <player>");
            return;
        }

        Player target = Bukkit.getPlayer(a[2]);
        if (target == null) {
            s.sendMessage("§cPlayer not found.");
            return;
        }

        if (a[1].equalsIgnoreCase("eval")) {
            GateService.evaluate(target);
            s.sendMessage("§aGate evaluation triggered.");
            return;
        }

        if (a[1].equalsIgnoreCase("debug")) {
            s.sendMessage("§6=== Gate Debug ===");
            GateRegistry.getAll().forEach(gate -> {
                boolean pass = gate.check(target);
                s.sendMessage("§7" + gate.getId() + ": " + (pass ? "§aPASS" : "§cFAIL"));
            });
        }
    }

    private void sendHelp(CommandSender s) {
        s.sendMessage("§6=== NormieProgression Admin ===");
        s.sendMessage("§7/np gui");
        s.sendMessage("§7/np status <player>");
        s.sendMessage("§7/np mark <add|addall|reset|list>");
        s.sendMessage("§7/np chronicle list <player>");
        s.sendMessage("§7/np reload");
    }
}
