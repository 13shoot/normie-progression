package io.github._13shoot.normieprogression.gui;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ProgressionGUI {

    public static final String GUI_TITLE = ChatColor.DARK_GRAY + "Your Path So Far";

    private static final int[] PROGRESSION_SLOTS = {2,3,4,5,6};
    private static final int[] MARK_SLOTS = {20,21,22,23,24,25,29,30,31,32,33,34};
    private static final int[] CHRONICLE_SLOTS = {38,39,40,41,42,43};

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(null, 54, GUI_TITLE);

        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        int[] glassSlots = {
                1,7,8,
                9,10,11,12,13,14,15,16,17,
                19,26,
                27,28,35,
                36,37,44,
                45,46,47,48,49,50,51,52,53
        };

        for (int slot : glassSlots) inv.setItem(slot, glass);

        inv.setItem(0, title(Material.COMPASS, "§6Progression:", "§7What the world thinks of you."));
        inv.setItem(18, title(Material.AMETHYST_SHARD, "§6Marks:", "§7What the world left on you."));
        inv.setItem(36, title(Material.WRITTEN_BOOK, "§dChronicle:", "§7What the world remembers."));

        ItemStack progEmpty = placeholder("§eProgression", "§7Progression still empty...");
        ItemStack markEmpty = placeholder("§eMarks", "§7Marks still empty...");
        ItemStack chrEmpty  = placeholder("§eChronicle", "§7Empty...");

        for (int s : PROGRESSION_SLOTS) inv.setItem(s, progEmpty);
        for (int s : MARK_SLOTS) inv.setItem(s, markEmpty);
        for (int s : CHRONICLE_SLOTS) inv.setItem(s, chrEmpty);

        // Progression icons
        List<ItemStack> prog = buildProgressionIcons(player);
        for (int i = 0; i < prog.size() && i < PROGRESSION_SLOTS.length; i++) {
            inv.setItem(PROGRESSION_SLOTS[i], prog.get(i));
        }

        // Mark icons (polished)
        List<ItemStack> marks = buildMarkIcons(player);
        for (int i = 0; i < marks.size() && i < MARK_SLOTS.length; i++) {
            inv.setItem(MARK_SLOTS[i], marks.get(i));
        }

        player.openInventory(inv);
    }

    private static List<ItemStack> buildProgressionIcons(Player player) {

        List<ItemStack> list = new ArrayList<>();
        Tier tier = TierManager.getTier(player.getUniqueId());

        if (tier != Tier.T0_UNSEEN)
            list.add(icon(Material.COMPASS, "§fStill Here", "§7The world noticed your presence."));

        if (tier.ordinal() >= Tier.T1_RECOGNIZED.ordinal())
            list.add(icon(Material.ECHO_SHARD, "§7Echoes", "§8Your actions left traces."));

        if (tier.ordinal() >= Tier.T2_ACKNOWLEDGED.ordinal())
            list.add(icon(Material.NAME_TAG, "§6A Name", "§7Some remember it."));

        if (tier.ordinal() >= Tier.T3_RESPONDED.ordinal())
            list.add(icon(Material.NETHER_STAR, "§dResponse", "§7The world reacts."));

        if (tier.ordinal() >= Tier.T4_REMEMBERED.ordinal())
            list.add(icon(Material.AMETHYST_SHARD, "§bRecorded", "§7You are part of the record."));

        return list;
    }

    private static List<ItemStack> buildMarkIcons(Player player) {

        List<ItemStack> list = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (MarkData data : MarkStorage.getMarks(player.getUniqueId())) {
            list.add(markItem(data.getType(), data, now));
        }
        return list;
    }

    private static ItemStack markItem(MarkType type, MarkData data, long now) {

        Material mat;
        String title;
        List<String> lore = new ArrayList<>();

        switch (type) {

            case SURVIVAL -> {
                mat = Material.OAK_LOG;
                title = "§aMark of Survival";
                lore.add("§7You survived.");
                lore.add("§7No glory. No reward.");
                lore.add("§8Apparently, that was enough.");
            }

            case HUNGER -> {
                mat = Material.ROTTEN_FLESH;
                title = "§6Mark of Hunger";
                lore.add("§7You learned what hunger sounds like.");
                lore.add("§7It doesn't scream.");
                lore.add("§8It waits.");
                lore.add("§cThis feeling will pass.");
            }

            case PERSISTENCE -> {
                mat = Material.OBSIDIAN;
                title = "§bMark of Persistence";
                lore.add("§7You failed. Repeatedly.");
                lore.add("§7And still showed up.");
                lore.add("§8Annoyingly impressive.");
            }

            case BLOOD -> {
                mat = Material.REDSTONE;
                title = "§cMark of Blood";
                lore.add("§7Death kept checking on you.");
                lore.add("§7You kept answering.");
                lore.add("§8That conversation isn't over.");
                lore.add("§cThe stain will fade.");
            }

            case RECOGNITION -> {
                mat = Material.ENDER_EYE;
                title = "§dMark of Recognition";
                lore.add("§7You reached places you weren't meant to.");
                lore.add("§7The world pretends it was planned.");
                lore.add("§8Congratulations.");
            }

            case LOSS -> {
                mat = Material.SOUL_SAND;
                title = "§8Mark of Loss";
                lore.add("§7Something valuable disappeared.");
                lore.add("§7The world shrugged.");
                lore.add("§8You didn't.");
                lore.add("§cThe weight will lift.");
            }

            case TRADE -> {
                mat = Material.EMERALD;
                title = "§eMark of Trade";
                lore.add("§7Money moved fast.");
                lore.add("§7Trust did not.");
                lore.add("§8Merchants noticed.");
                lore.add("§cFortune is fickle.");
            }

            case COLD -> {
                mat = Material.BLUE_ICE;
                title = "§9Mark of Cold";
                lore.add("§7The cold tried to push you out.");
                lore.add("§7It failed.");
                lore.add("§8It stopped trying.");
            }

            case INFLUENCE -> {
                mat = Material.COMMAND_BLOCK;
                title = "§6Mark of Influence";
                lore.add("§7You spoke to the world.");
                lore.add("§7It listened. Regretfully.");
                lore.add("§8Power leaves traces.");
            }

            case FEAR -> {
                mat = Material.WITHER_SKELETON_SKULL;
                title = "§cMark of Fear";
                lore.add("§7You were almost gone.");
                lore.add("§7You noticed.");
                lore.add("§8That memory hasn't.");
                lore.add("§cFear fades slowly.");
            }

            case WITNESS -> {
                mat = Material.WRITTEN_BOOK;
                title = "§fMark of the Witness";
                lore.add("§7You watched others disappear.");
                lore.add("§7You didn't follow.");
                lore.add("§8Someone has to remember.");
            }

            case FAVOR -> {
                mat = Material.GLOWSTONE_DUST;
                title = "§dMark of Favor";
                lore.add("§7Something went right.");
                lore.add("§7Nobody knows why.");
                lore.add("§8Don't get used to it.");
                lore.add("§cLuck never stays.");
            }

            default -> {
                mat = Material.PAPER;
                title = "§7Unknown Mark";
                lore.add("§8The world hasn't decided yet.");
            }
        }

        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack icon(Material mat, String name, String lore) {
        ItemStack i = new ItemStack(mat);
        ItemMeta m = i.getItemMeta();
        if (m != null) {
            m.setDisplayName(name);
            m.setLore(List.of(lore));
            i.setItemMeta(m);
        }
        return i;
    }

    private static ItemStack title(Material mat, String name, String lore) {
        return icon(mat, name, lore);
    }

    private static ItemStack placeholder(String name, String lore) {
        return icon(Material.PAPER, name, lore);
    }

    private static ItemStack createItem(Material mat, String name) {
        ItemStack i = new ItemStack(mat);
        ItemMeta m = i.getItemMeta();
        if (m != null) {
            m.setDisplayName(name);
            i.setItemMeta(m);
        }
        return i;
    }
}
