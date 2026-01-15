package io.github._13shoot.normieprogression.gui;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

        // -------------------------------------------------
        // Step 2A: Progression Icons
        // -------------------------------------------------
        List<ItemStack> progressionIcons = buildProgressionIcons(player);

        for (int i = 0; i < progressionIcons.size() && i < PROGRESSION_SLOTS.length; i++) {
            inv.setItem(PROGRESSION_SLOTS[i], progressionIcons.get(i));
        }

        player.openInventory(inv);
    }

    private static List<ItemStack> buildProgressionIcons(Player player) {

        List<ItemStack> icons = new ArrayList<>();
        UUID id = player.getUniqueId();

        Tier tier = TierManager.getTier(id);
        VisibilityData v = VisibilityManager.get(id);

        // Stage 1: Seen by the world
        if (tier != Tier.T0_UNSEEN) {
            icons.add(icon(Material.COMPASS,
                    ChatColor.WHITE + "A Noticed Presence",
                    ChatColor.GRAY + "The world no longer overlooks you."));
        }

        // Stage 2: Recognized existence
        if (tier.ordinal() >= Tier.T1_RECOGNIZED.ordinal()) {
            icons.add(icon(Material.ECHO_SHARD,
                    ChatColor.GRAY + "Echoes Left Behind",
                    ChatColor.DARK_GRAY + "Your actions leave traces."));
        }

        // Stage 3: Known name
        if (tier.ordinal() >= Tier.T2_ACKNOWLEDGED.ordinal()) {
            icons.add(icon(Material.NAME_TAG,
                    ChatColor.GOLD + "A Name Remembered",
                    ChatColor.GRAY + "Some remember who you are."));
        }

        // Stage 4: World response
        if (tier.ordinal() >= Tier.T3_RESPONDED.ordinal()) {
            icons.add(icon(Material.NETHER_STAR,
                    ChatColor.LIGHT_PURPLE + "The World Responds",
                    ChatColor.GRAY + "Your presence carries weight."));
        }

        // Stage 5: Part of history
        if (tier.ordinal() >= Tier.T4_REMEMBERED.ordinal()) {
            icons.add(icon(Material.AMETHYST_SHARD,
                    ChatColor.AQUA + "Part of the Record",
                    ChatColor.GRAY + "The world keeps a place for you."));
        }

        return icons;
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
