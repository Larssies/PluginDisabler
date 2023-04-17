package net.twistpvp.plugindisabler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class GUIs {

    public static Inventory gui = Bukkit.createInventory(null, 27, "Blocked Commands");


    public static void openBlockedList(Player p) {

        int index = 0;
        for (String command : Storage.disabledCommands) {
            ItemStack item = new ItemStack(Material.STONE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l" + command));
            ArrayList<String> lore = new ArrayList<>();
            lore.clear();
            lore.add("");
            lore.add(ChatColor.RED + "Click to remove this command");
            meta.setLore(lore);
            item.setItemMeta(meta);

            gui.setItem(index, item);
            index++;
            if (index % 9 == 0) {
                index += 2;
            }
        }

        p.openInventory(gui);
    }
}
