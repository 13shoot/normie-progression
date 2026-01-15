package io.github._13shoot.normieprogression.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ProgressionGUI {

    public static final String GUI_TITLE = ChatColor.DARK_GRAY + "Your Path So Far";

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, GUI_TITLE);

        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        // Glass frame: column 2 and 9, all rows
        for (int row = 0; row < 6; row++) {
            inv.setItem(row * 9 + 1, glass);
            inv.setItem(row * 9 + 8, glass);
        }

        // Glass rows: row 2 and row 6
        for (int col = 0; col < 9; col++) {
            inv.setItem(9 + col, glass);
            inv.setItem(45 + col, glass);
        }

        // Titles with improved icons
        inv.setItem(0, createTitle(
                Material.COMPASS,
                ChatColor.GOLD + "Progression:",
                ChatColor.GRAY + "The path you have already walked."
        ));

        inv.setItem(18, createTitle(
                Material.AMETHYST_SHARD,
                ChatColor.GOLD + "Marks:",
                ChatColor.GRAY + "Traces the world chose to remember."
        ));

        inv.setItem(36, createTitle(
                Material.WRITTEN_BOOK,
                ChatColor.LIGHT_PURPLE + "Chronicle:",
                ChatColor.GRAY + "Fragments of your lived history."
        ));

        // Placeholder papers
        ItemStack progressionEmpty = createPlaceholder(
                Material.PAPER,
                ChatColor.YELLOW + "Progression",
                ChatColor.GRAY + "Progression still empty..."
        );

        ItemStack markEmpty = createPlaceholder(
                Material.PAPER,
                ChatColor.YELLOW + "Marks",
                ChatColor.GRAY + "Marks still empty..."
        );

        ItemStack chronicleEmpty = createPlaceholder(
                Material.PAPER,
                ChatColor.YELLOW + "Chronicle",
                ChatColor.GRAY + "Empty..."
        );

        // Fill progression area
        for (int slot = 2; slot <= 7; slot++) {
            inv.setItem(slot, progressionEmpty);
        }

        // Fill mark area (row 3-4)
        for (int row = 3; row <= 4; row++) {
            for (int col = 2; col <= 7; col++) {
                inv.setItem(row * 9 + col, markEmpty);
            }
        }

        // Fill chronicle area
        for (int col = 2; col <= 7; col++) {
            inv.setItem(5 * 9 + col, chronicleEmpty);
        }

        player.openInventory(inv);
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
