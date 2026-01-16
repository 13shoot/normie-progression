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
import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * /np command
 *
 * - /np            → open GUI
 * - /np help       → admin help
 *
 * All admin commands:
 * - OP only
 * - <player> always LAST argument
 */
public class ProgressionCommand implements CommandExecutor, TabCompleter {

    /* =========================================================
     * TAB COMPLETE
     * ========================================================= */
    @Override
    public List<String> onTabComplete(
            CommandSender sender,
            Command command,
            String alias,
            String[] args
    ) {
        if (!sender.isOp()) return List.of();

        if (args.length == 1) {
            return List.of(
                    "help",
                    "gui",
                    "status",
                    "visibility",
                    "gate",
                    "tier",
                    "mark",
                    "chronicle",
                    "reload"
            );
        }

        switch (args[0].toLowerCase()) {

            case "gate" -> {
                if (args.length == 2) return List.of("eval", "debug");
                if (args.length == 3) return onlinePlayers();
            }

            case "visibility" -> {
                if (args.length == 2) return List.of("status", "reset");
                if (args.length == 3) return onlinePlayers();
            }

            case "tier" -> {
                if (args.length == 2) return List.of("get", "set", "reset");
                if (args.length == 3 && args[1].equalsIgnoreCase("set"))
                    return Arrays.stream(Tier.values()).map(Enum::name).toList();
                if (args.length >= 3) return onlinePlayers();
            }

            case "mark" -> {
                if (args.length == 2) return List.of("list", "add", "remove", "reset");
                if (args.length == 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove")))
                    return Arrays.stream(MarkType.values()).map(Enum::name).toList();
                if (args.length >= 3) return onlinePlayers();
            }

            case "chronicle" -> {
                if (args.length == 2) return List.of("list", "reset");
                if (args.length == 3) return onlinePlayers();
            }
        }

        return List.of();
    }

    private List<String> onlinePlayers() {
        return Bukkit.getOnlinePlayers()
                .stream()
                .map(Player::getName)
                .collect(Collectors.toList());
    }

    /* =========================================================
     * COMMAND
     * ========================================================= */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /* ---------- Player GUI ---------- */
        if (args.length == 0 || args[0].equalsIgnoreCase("gui")) {
            if (sender instanceof Player p) {
                ProgressionGUI.open(p);
            } else {
                sender.sendMessage("§cPlayers only.");
            }
            return true;
        }

        /* ---------- Admin only ---------- */
        if (!sender.isOp()) {
            sender.sendMessage("§cAdmin only.");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help" -> sendHelp(sender);
            case "status" -> handleStatus(sender, args);
            case "visibility" -> handleVisibility(sender, args);
            case "gate" -> handleGate(sender, args);
            case "tier" -> handleTier(sender, args);
            case "mark" -> handleMark(sender, args);
            case "chronicle" -> handleChronicle(sender, args);
            case "reload" -> handleReload(sender);
            default -> sendHelp(sender);
        }
        return true;
    }

    /* =========================================================
     * STATUS / VISIBILITY
     * ========================================================= */
    private void handleStatus(CommandSender s, String[] a) {
        if (a.length < 2) {
            s.sendMessage("§c/np status <player>");
            return;
        }

        Player p = Bukkit.getPlayer(a[1]);
        if (p == null) {
            s.sendMessage("§cPlayer not found.");
            return;
        }

        VisibilityData v = VisibilityManager.get(p.getUniqueId());

        s.sendMessage("§6=== Progression Status ===");
        s.sendMessage("§7Player: §f" + p.getName());
        s.sendMessage("§bVisibility");
        s.sendMessage(" §7Days Alive: §f" + v.getDaysAlive());
        s.sendMessage(" §7Total Visibility: §f" + v.getTotalVisibility());
        s.sendMessage("§dTier");
        s.sendMessage(" §7Tier: §f" + TierManager.getTier(p.getUniqueId()));
        s.sendMessage("§eMarks: §f" + MarkStorage.getMarks(p.getUniqueId()).size());
        s.sendMessage("§aChronicles: §f" + ChronicleStorage.get(p.getUniqueId()).size());
    }

    private void handleVisibility(CommandSender s, String[] a) {
        if (a.length < 3) {
            s.sendMessage("§c/np visibility <status|reset> <player>");
            return;
        }

        Player p = Bukkit.getPlayer(a[2]);
        if (p == null) {
            s.sendMessage("§cPlayer not found.");
            return;
        }

        if (a[1].equalsIgnoreCase("reset")) {
            VisibilityManager.getOrCreate(p.getUniqueId()).resetDaysAlive();
            s.sendMessage("§aVisibility reset.");
        }
    }

    /* =========================================================
     * MARK
     * ========================================================= */
    private void handleMark(CommandSender s, String[] a) {
        if (a.length < 3) {
            s.sendMessage("§c/np mark <list|add|remove|reset> <player>");
            return;
        }

        Player p = Bukkit.getPlayer(a[a.length - 1]);
        if (p == null) {
            s.sendMessage("§cPlayer not found.");
            return;
        }

        UUID id = p.getUniqueId();
        int day = VisibilityManager.get(id).getDaysAlive();

        switch (a[1].toLowerCase()) {

            case "list" -> {
                s.sendMessage("§6=== Marks of " + p.getName() + " ===");
                for (MarkData m : MarkStorage.getMarks(id)) {
                    s.sendMessage((m.isPermanent() ? "§a" : "§c") + m.getType().name());
                }
            }

            case "add" -> {
                MarkType t = MarkType.valueOf(a[2].toUpperCase());
                MarkStorage.addMark(id, new MarkData(
                        t,
                        day,
                        t.isPermanent() ? -1 : day + 3,
                        day
                ));
                s.sendMessage("§aMark added.");
            }

            case "remove" -> {
                MarkType t = MarkType.valueOf(a[2].toUpperCase());
                MarkStorage.removeMark(id, t);
                s.sendMessage("§eMark removed.");
            }

            case "reset" -> {
                for (MarkType t : MarkType.values())
                    MarkStorage.removeMark(id, t);
                s.sendMessage("§cAll marks reset.");
            }
        }
    }

    /* =========================================================
     * CHRONICLE
     * ========================================================= */
    private void handleChronicle(CommandSender s, String[] a) {
        if (a.length < 3) {
            s.sendMessage("§c/np chronicle <list|reset> <player>");
            return;
        }

        Player p = Bukkit.getPlayer(a[2]);
        if (p == null) {
            s.sendMessage("§cPlayer not found.");
            return;
        }

        if (a[1].equalsIgnoreCase("list")) {
            s.sendMessage("§6=== Chronicle ===");
            for (ChronicleEntry e : ChronicleStorage.get(p.getUniqueId()))
                s.sendMessage("§f- " + e.getTitle());
        }

        if (a[1].equalsIgnoreCase("reset")) {
            ChronicleStorage.clear(p.getUniqueId());
            s.sendMessage("§cChronicle reset.");
        }
    }

    /* =========================================================
     * GATE / TIER / RELOAD
     * ========================================================= */
    private void handleGate(CommandSender s, String[] a) {
        Player p = Bukkit.getPlayer(a[a.length - 1]);
        if (p == null) return;

        if (a[1].equalsIgnoreCase("eval")) {
            GateService.evaluate(p);
            s.sendMessage("§aGate evaluated.");
        }

        if (a[1].equalsIgnoreCase("debug")) {
            GateRegistry.getAll().forEach(g ->
                    s.sendMessage(g.getId() + ": " + (g.check(p) ? "§aPASS" : "§cFAIL")));
        }
    }

    private void handleTier(CommandSender s, String[] a) {
        Player p = Bukkit.getPlayer(a[a.length - 1]);
        if (p == null) return;

        if (a[1].equalsIgnoreCase("set")) {
            TierManager.setTier(p.getUniqueId(), Tier.valueOf(a[2].toUpperCase()));
            s.sendMessage("§aTier set.");
        }

        if (a[1].equalsIgnoreCase("reset")) {
            TierManager.resetTier(p.getUniqueId());
            s.sendMessage("§cTier reset.");
        }
    }

    private void handleReload(CommandSender s) {
        NormieProgression.getPlugin(NormieProgression.class).forceLoad();
        s.sendMessage("§aAll data reloaded.");
    }

    /* =========================================================
     * HELP
     * ========================================================= */
    private void sendHelp(CommandSender s) {
        s.sendMessage("§6=== NormieProgression Admin ===");
        s.sendMessage("§7/np (open GUI)");
        s.sendMessage("§7/np status <player>");
        s.sendMessage("§7/np visibility <status|reset> <player>");
        s.sendMessage("§7/np gate <eval|debug> <player>");
        s.sendMessage("§7/np tier <get|set|reset> <player>");
        s.sendMessage("§7/np mark <list|add|remove|reset> <player>");
        s.sendMessage("§7/np chronicle <list|reset> <player>");
        s.sendMessage("§7/np reload");
    }
}
