package net.twistpvp.plugindisabler.commands;

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
                    if(Storage.disabledCommands.contains(msg)) {
                        p.sendMessage(ChatColor.RED + "This command is already on the block list!");
                    } else {
                        Storage.disabledCommands.add(msg);
                        p.sendMessage(ChatColor.GREEN + "Successfully added " + "'" + msg + "' to the block list!");
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permissions to execute this command!");
            }
        }
        return true;
    }
}
