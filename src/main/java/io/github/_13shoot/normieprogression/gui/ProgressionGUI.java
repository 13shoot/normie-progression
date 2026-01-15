package io.github._13shoot.normieprogression.gui;

import io.github._13shoot.normieprogression.mark.MarkData;
import io.github._13shoot.normieprogression.mark.MarkStorage;
import io.github._13shoot.normieprogression.mark.MarkType;
import io.github._13shoot.normieprogression.tier.Tier;
import io.github._13shoot.normieprogression.tier.TierManager;
import io.github._13shoot.normieprogression.visibility.VisibilityData;
import io.github._13shoot.normieprogression.visibility.VisibilityManager;
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

    private static final int[] PROGRESSION_SLOTS = {2,3,4,5,6,7};
    private static final int[] MARK_SLOTS = {20,21,22,23,24,25,29,30,31,32,33,34};
    private static final int[] CHRONICLE_SLOTS = {38,39,40,41,42,43};

    public static void open(Player player) {

        Inventory inv = Bukkit.createInventory(null, 54, GUI_TITLE);

        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        int[] glassSlots = {
                1,8,
                9,10,11,12,13,14,15,16,17,
                19,26,
                27,28,35,
                36,37,44,
                45,46,47,48,49,50,51,52,53
        };

        for (int slot : glassSlots) {
            inv.setItem(slot, glass);
        }

        inv.setItem(0, createTitle(Material.COMPASS,
                ChatColor.GOLD + "Progression:",
                ChatColor.GRAY + "The path you have already walked."));

        inv.setItem(18, createTitle(Material.AMETHYST_SHARD,
                ChatColor.GOLD + "Marks:",
                ChatColor.GRAY + "Traces the world chose to remember."));

        inv.setItem(36, createTitle(Material.WRITTEN_BOOK,
                ChatColor.LIGHT_PURPLE + "Chronicle:",
                ChatColor.GRAY + "Fragments of your lived history."));

        ItemStack progressionEmpty = createPlaceholder(Material.PAPER,
                ChatColor.YELLOW + "Progression",
                ChatColor.GRAY + "Progression still empty...");

        ItemStack markEmpty = createPlaceholder(Material.PAPER,
                ChatColor.YELLOW + "Marks",
                ChatColor.GRAY + "Marks still empty...");

        ItemStack chronicleEmpty = createPlaceholder(Material.PAPER,
                ChatColor.YELLOW + "Chronicle",
                ChatColor.GRAY + "Empty...");

        for (int slot : PROGRESSION_SLOTS) inv.setItem(slot, progressionEmpty);
        for (int slot : MARK_SLOTS) inv.setItem(slot, markEmpty);
        for (int slot : CHRONICLE_SLOTS) inv.setItem(slot, chronicleEmpty);

        // ------------------------------
        // Step 2A: Progression Icons
        // ------------------------------
        List<ItemStack> progressionIcons = buildProgressionIcons(player);
        for (int i = 0; i < progressionIcons.size() && i < PROGRESSION_SLOTS.length; i++) {
            inv.setItem(PROGRESSION_SLOTS[i], progressionIcons.get(i));
        }

        // ------------------------------
        // Step 2B-2: Mark Icons
        // ------------------------------
        List<ItemStack> markIcons = buildMarkIcons(player);
        for (int i = 0; i < markIcons.size() && i < MARK_SLOTS.length; i++) {
            inv.setItem(MARK_SLOTS[i], markIcons.get(i));
        }

        player.openInventory(inv);
    }

    // -------------------------------------------------
    // Progression Icons (unchanged)
    // -------------------------------------------------
    private static List<ItemStack> buildProgressionIcons(Player player) {

        List<ItemStack> icons = new ArrayList<>();
        UUID id = player.getUniqueId();

        Tier tier = TierManager.getTier(id);

        if (tier != Tier.T0_UNSEEN) {
            icons.add(icon(Material.COMPASS,
                    ChatColor.WHITE + "A Noticed Presence",
                    ChatColor.GRAY + "The world no longer overlooks you."));
        }

        if (tier.ordinal() >= Tier.T1_RECOGNIZED.ordinal()) {
            icons.add(icon(Material.ECHO_SHARD,
                    ChatColor.GRAY + "Echoes Left Behind",
                    ChatColor.DARK_GRAY + "Your actions leave traces."));
        }

        if (tier.ordinal() >= Tier.T2_ACKNOWLEDGED.ordinal()) {
            icons.add(icon(Material.NAME_TAG,
                    ChatColor.GOLD + "A Name Remembered",
                    ChatColor.GRAY + "Some remember who you are."));
        }

        if (tier.ordinal() >= Tier.T3_RESPONDED.ordinal()) {
            icons.add(icon(Material.NETHER_STAR,
                    ChatColor.LIGHT_PURPLE + "The World Responds",
                    ChatColor.GRAY + "Your presence carries weight."));
        }

        if (tier.ordinal() >= Tier.T4_REMEMBERED.ordinal()) {
            icons.add(icon(Material.AMETHYST_SHARD,
                    ChatColor.AQUA + "Part of the Record",
                    ChatColor.GRAY + "The world keeps a place for you."));
        }

        return icons;
    }

    // -------------------------------------------------
    // Mark Icons (NEW)
    // -------------------------------------------------
    private static List<ItemStack> buildMarkIcons(Player player) {

        List<ItemStack> icons = new ArrayList<>();
        long now = System.currentTimeMillis();

        for (MarkData data : MarkStorage.getMarks(player.getUniqueId())) {

            MarkType type = data.getType();
            ItemStack item = createMarkItem(type, data, now);
            icons.add(item);
        }

        return icons;
    }

    private static ItemStack createMarkItem(MarkType type, MarkData data, long now) {

        Material mat;
        ChatColor titleColor;

        if (type.isPermanent()) {
            mat = Material.AMETHYST_SHARD;
            titleColor = ChatColor.AQUA;
        } else {
            mat = Material.CLOCK;
            titleColor = ChatColor.GOLD;
        }

        String title = titleColor + formatName(type.name());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "A mark carried by your actions.");

        if (!type.isPermanent()) {
            lore.add(ChatColor.DARK_RED + "Its presence feels temporary.");
            if (data.isExpired(now)) {
                lore.add(ChatColor.RED + "It is fading away.");
            } else {
                lore.add(ChatColor.RED + "Time is running out.");
            }
        } else {
            lore.add(ChatColor.DARK_GRAY + "This mark will not leave you.");
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

    // -------------------------------------------------
    // Helpers
    // -------------------------------------------------
    private static String formatName(String raw) {
        return raw.substring(0,1).toUpperCase() +
                raw.substring(1).toLowerCase().replace("_", " ");
    }

    private static ItemStack icon(Material mat, String title, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createTitle(Material mat, String title, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }

    private static ItemStack createPlaceholder(Material mat, String title, String lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title);
            meta.setLore(Arrays.asList(lore));
            item.setItemMeta(meta);
        }
        return item;
    }
}
