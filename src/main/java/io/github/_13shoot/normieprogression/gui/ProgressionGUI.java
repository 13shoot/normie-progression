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

        // Fill glass frame
        ItemStack glass = createItem(Material.GRAY_STAINED_GLASS_PANE, " ");

        for (int row = 0; row < 6; row++) {
            for (int col : new int[]{1, 8}) {
                int slot = row * 9 + col;
                inv.setItem(slot, glass);
            }
        }

        for (int col = 0; col < 9; col++) {
            inv.setItem(9 + col, glass);   // Row 2
            inv.setItem(45 + col, glass);  // Row 6
        }

        // Titles
        inv.setItem(0, createTitle(Material.NAME_TAG, ChatColor.GOLD + "Progression:",
                ChatColor.GRAY + "The way the world remembers you."));

        inv.setItem(18, createTitle(Material.NAME_TAG, ChatColor.GOLD + "Marks:",
                ChatColor.GRAY + "Traces left by your actions."));

        inv.setItem(36, createTitle(Material.NAME_TAG, ChatColor.LIGHT_PURPLE + "Chronicle:",
                ChatColor.GRAY + "Fragments the world chose to keep."));

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
            meta.setLore(Arrays.asList(lore, ChatColor.DARK_GRAY + " "));
            item.setItemMeta(meta);
        }
        return item;
    }
}
