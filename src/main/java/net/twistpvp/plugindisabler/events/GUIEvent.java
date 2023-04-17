package net.twistpvp.plugindisabler.events;

import net.twistpvp.plugindisabler.GUIs;
import net.twistpvp.plugindisabler.PluginDisabler;
import net.twistpvp.plugindisabler.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class GUIEvent implements Listener {

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        if (plugin.equals(new PluginDisabler())) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                Inventory inventory = player.getInventory();
                ItemStack[] contents = inventory.getContents();
                for (int i = 0; i < contents.length; i++) {
                    ItemStack item = contents[i];
                    if (item != null && item.getType() == Material.REDSTONE_BLOCK) {
                        inventory.setItem(i, new ItemStack(Material.AIR));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getView().getTitle().equals("Blocked Commands")) {
            e.setCancelled(true);
        }

        if (e.getClickedInventory().equals(GUIs.gui)) {
            ItemStack clickedItem = e.getCurrentItem();
            if (clickedItem == null || clickedItem.getType() == Material.AIR) {
                return;
            }

            String command = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
            Storage.disabledCommands.remove(command);

            e.getClickedInventory().remove(clickedItem);

            p.sendMessage(ChatColor.GREEN + "Successfully removed " + command + " from the block list!");
        }
    }
}