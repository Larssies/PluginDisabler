package net.twistpvp.plugindisabler.events;

import net.twistpvp.plugindisabler.PluginDisabler;
import net.twistpvp.plugindisabler.Storage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatEvent implements Listener {

    PluginDisabler plugin;

    public ChatEvent(PluginDisabler plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = plugin.getConfig().getString("message");
        Player player = event.getPlayer();
        String command = event.getMessage().split(" ")[0].substring(1);

        if (Storage.disabledCommands.contains(command) && !player.hasPermission("pluginhider.admin")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.hasPermission("pluginhider.admin")) {
                    if(Storage.logMode.contains(p.getUniqueId())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b[S] &7" + player.getName() + " &chas tried to execute &f/" + command));
                    }
                }
            }
            event.setCancelled(true);
        }
    }
}