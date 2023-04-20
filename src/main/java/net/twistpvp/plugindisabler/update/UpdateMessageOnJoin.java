package net.twistpvp.plugindisabler.update;

import net.twistpvp.plugindisabler.PluginDisabler;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateMessageOnJoin implements Listener {

    PluginDisabler plugin;

    public UpdateMessageOnJoin(PluginDisabler plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if(plugin.getConfig().getBoolean("check-for-updates")) {
            if(p.isOp()) {
                new UpdateChecker(plugin, 1234).getVersion(version -> {
                    if(!plugin.getDescription().getVersion().equals(version)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "[PluginDisabler] &cThere is a new update available on Spigot!"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCheck it out here: &7&ohttps://api.spigotmc.org/legacy/update.php?resource="));
                    }
                });
            }
        }
    }
}
