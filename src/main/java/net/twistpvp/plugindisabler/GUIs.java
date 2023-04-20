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


    private static final String TITLE = "Blocked Commands";
    private static final int PAGE_SIZE = 27;
    private static final ItemStack NEXT_PAGE_ITEM = new ItemStack(Material.ARROW, 1);
    private static final ItemStack PREVIOUS_PAGE_ITEM = new ItemStack(Material.ARROW, 1);

    static {
        ItemMeta nextPageMeta = NEXT_PAGE_ITEM.getItemMeta();
        nextPageMeta.setDisplayName(ChatColor.GREEN + "Next Page");
        NEXT_PAGE_ITEM.setItemMeta(nextPageMeta);

        ItemMeta previousPageMeta = PREVIOUS_PAGE_ITEM.getItemMeta();
        previousPageMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
        PREVIOUS_PAGE_ITEM.setItemMeta(previousPageMeta);
    }

    public static void openBlockedList(Player p) {
        int numCommands = Storage.disabledCommands.size();
        int numPages = (int) Math.ceil(numCommands / 27.0);
        int currentPage = 1;
        int index = 0;

        // Create a new inventory for the first page
        Inventory gui = Bukkit.createInventory(null, 36, TITLE + " - Page " + currentPage);

        for (String command : Storage.disabledCommands) {
            if (index >= PAGE_SIZE) {
                gui.setItem(35, NEXT_PAGE_ITEM);
                p.openInventory(gui);
                currentPage++;
                gui = Bukkit.createInventory(null, 36, TITLE + " - Page " + currentPage);
                index = 0;
                if (currentPage > 2) {
                    gui.setItem(27, PREVIOUS_PAGE_ITEM);
                }
            }

            ItemStack item = new ItemStack(Material.STONE);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l" + command));
            ArrayList<String> lore = new ArrayList<>();
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

        if (numPages > 1) {
            gui.setItem(35, NEXT_PAGE_ITEM);
        }

        p.openInventory(gui);
    }
}
