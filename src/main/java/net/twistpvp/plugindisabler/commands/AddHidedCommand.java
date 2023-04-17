package net.twistpvp.plugindisabler.commands;

import net.twistpvp.plugindisabler.GUIs;
import net.twistpvp.plugindisabler.Storage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddHidedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p;
        if(commandSender instanceof Player) {
            p = (Player) commandSender;

            if(p.hasPermission("pluginhider.admin")) {
                if(args.length != 1) {
                    p.sendMessage(ChatColor.RED + "Usage: /pluginhider <command>\nWithout the slash!");
                } else {
                    String msg = args[0];
                    if(!args[0].equalsIgnoreCase("gui") || !args[0].equalsIgnoreCase("help")) {
                        if(Storage.disabledCommands.contains(msg)) {
                            p.sendMessage(ChatColor.RED + "This command is already on the block list!");
                        } else {
                            Storage.disabledCommands.add(msg);
                            p.sendMessage(ChatColor.GREEN + "Successfully added " + "'" + msg + "' to the block list!");
                        }
                    }
                    if(args[0].equalsIgnoreCase("gui")) {
                        GUIs.openBlockedList(p);
                    }
                    if(args[0].equalsIgnoreCase("help")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lPlugin Hider &7&ov1.0"));
                        p.sendMessage("");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph help &7Gives you this page again"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph gui &7Allows you to manage commands via a GUI"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph add <command> &7Allows you to add commands to the block list"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph reload &7Allows you to reload the config file"));
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permissions to execute this command!");
            }
        }
        return true;
    }
}
