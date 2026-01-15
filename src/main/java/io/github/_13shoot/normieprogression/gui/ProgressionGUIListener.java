// File: ProgressionGUIListener.java
// Path: src/main/java/io/github/_13shoot/normieprogression/gui/ProgressionGUIListener.java

package io.github._13shoot.normieprogression.gui;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ProgressionGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equals(ChatColor.DARK_GRAY + "Your Path So Far")) {

            // Lock top inventory (GUI)
            if (event.getRawSlot() < 54) {
                event.setCancelled(true);
            }
        }
    }
}
