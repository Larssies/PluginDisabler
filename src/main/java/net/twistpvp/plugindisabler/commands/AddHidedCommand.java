package net.twistpvp.plugindisabler.commands;

import net.twistpvp.plugindisabler.GUIs;
import net.twistpvp.plugindisabler.PluginDisabler;
import net.twistpvp.plugindisabler.Storage;
import net.twistpvp.plugindisabler.github.GitHub;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AddHidedCommand implements CommandExecutor {

    PluginDisabler plugin;

    public AddHidedCommand(PluginDisabler plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Player p;
        if(commandSender instanceof Player) {
            p = (Player) commandSender;

            if(p.hasPermission("pluginhider.admin")) {
                if(args.length < 1) {
                    p.sendMessage(ChatColor.RED + "Usage: /pluginhider help");
                } else {
                    if(args[0].equalsIgnoreCase("add")) {
                        String msg = args[1];
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
                    if(args[0].equalsIgnoreCase("reload")) {
                        plugin.reloadConfig();
                        p.sendMessage(ChatColor.GREEN + "Successfully reloaded the config file!");
                    }
                    if(args[0].equalsIgnoreCase("remove")) {
                        String msg = args[1];
                        if(!Storage.disabledCommands.contains(msg)) {
                            p.sendMessage(ChatColor.RED + "This command is not on the block list!");
                        } else {
                            Storage.disabledCommands.remove(msg);
                            p.sendMessage(ChatColor.GREEN + "Successfully removed " + msg + " from the block list!");
                        }
                    }
                    if(args[0].equalsIgnoreCase("help")) {

                        Path repoPath = Paths.get("https://github.com/Larssies/PluginDisabler.git");
                        String version = GitHub.getCommitVersion(repoPath);

                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&lPlugin Hider &7&ov" + version));
                        p.sendMessage("");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph help &7Gives you this page again"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph gui &7Allows you to manage commands via a GUI"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph add <command> &7Allows you to add commands to the block list"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph remove <command> &7Allows you to remove commands from the block list"));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c/ph reload &7Allows you to reload the config file"));
                    } else {
                        if(!args[0].equalsIgnoreCase("help") && !args[0].equalsIgnoreCase("gui") && !args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("reload")
                        && !args[0].equalsIgnoreCase("remove")) {
                            p.sendMessage(ChatColor.RED + "Usage: /pluginhider help");
                        }
                    }
                }
            } else {
                p.sendMessage(ChatColor.RED + "You don't have permissions to execute this command!");
            }
        } else {
            System.out.println("Only players can execute this command!");
        }
        return true;
    }
}
