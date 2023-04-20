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

import java.util.ArrayList;

public class GUIEvent implements Listener {

    PluginDisabler plugin;

    public GUIEvent(PluginDisabler plugin) {
        this.plugin = plugin;
    }

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

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        Plugin plugin = event.getPlugin();
        if (plugin.equals((Plugin) this)) {
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
        int currentPage = 1;
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null) {
            return;
        }

        Inventory clickedInventory = e.getClickedInventory();
        String inventoryTitle = e.getView().getTitle();

        if (inventoryTitle.startsWith("Blocked Commands")) {
            e.setCancelled(true);

            if (inventoryTitle.endsWith("Page " + currentPage)) {
                ItemStack clickedItem = e.getCurrentItem();

                if (clickedItem != null && clickedItem.getType() == Material.STONE) {
                    String command = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());
                    Storage.disabledCommands.remove(command);

                    int clickedSlot = e.getSlot();
                    clickedInventory.setItem(clickedSlot, new ItemStack(Material.AIR));

                    for (int i = clickedSlot + 1; i < clickedInventory.getSize(); i++) {
                        ItemStack currentItem = clickedInventory.getItem(i);
                        if (currentItem == null || currentItem.getType() == Material.AIR) {
                            break;
                        }

                        clickedInventory.setItem(i - 1, currentItem);
                        clickedInventory.setItem(i, new ItemStack(Material.AIR));
                    }

                    p.sendMessage(ChatColor.GREEN + "Successfully removed " + command + " from the block list!");
                }
            } else {
                ItemStack clickedItem = e.getCurrentItem();

                if (clickedItem != null) {
                    if (clickedItem.isSimilar(NEXT_PAGE_ITEM)) {
                        currentPage++;

                        clickedInventory.clear();
                        int startIndex = (currentPage - 1) * PAGE_SIZE;
                        int endIndex = Math.min(startIndex + PAGE_SIZE, Storage.disabledCommands.size());

                        int index = 0;
                        for (int i = startIndex; i < endIndex; i++) {
                            String command = Storage.disabledCommands.get(i);
                            ItemStack item = new ItemStack(Material.STONE);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l" + command));
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add(ChatColor.RED + "Click to remove this command");
                            meta.setLore(lore);
                            item.setItemMeta(meta);

                            clickedInventory.setItem(index, item);
                            index++;
                            if (index % 9 == 0) {
                                index += 2;
                            }
                        }

                        if (startIndex > 0) {
                            clickedInventory.setItem(18, PREVIOUS_PAGE_ITEM);
                        }

                        if (endIndex < Storage.disabledCommands.size()) {
                            clickedInventory.setItem(26, NEXT_PAGE_ITEM);
                        }
                    } else if (clickedItem.isSimilar(PREVIOUS_PAGE_ITEM)) {
                        currentPage--;

                        clickedInventory.clear();
                        int startIndex = (currentPage - 1) * PAGE_SIZE;
                        int endIndex = Math.min(startIndex + PAGE_SIZE, Storage.disabledCommands.size());

                        int index = 0;
                        for (int i = startIndex; i < endIndex; i++) {
                            String command = Storage.disabledCommands.get(i);
                            ItemStack item = new ItemStack(Material.STONE);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c&l" + command));
                            ArrayList<String> lore = new ArrayList<>();
                            lore.add("");
                            lore.add(ChatColor.RED + "Click to remove this command");
                            meta.setLore(lore);
                            item.setItemMeta(meta);
                            clickedInventory.setItem(index, item);
                            index++;
                            if (index % 9 == 0) {
                                index += 2;
                            }
                        }

                        if (startIndex > 0) {
                            clickedInventory.setItem(18, PREVIOUS_PAGE_ITEM);
                        }

                        if (endIndex < Storage.disabledCommands.size()) {
                            clickedInventory.setItem(26, NEXT_PAGE_ITEM);
                        }
                    }
                }
            }
        }
    }
}