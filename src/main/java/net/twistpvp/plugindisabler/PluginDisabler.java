package net.twistpvp.plugindisabler;

import net.twistpvp.plugindisabler.commands.PluginHiderCommand;
import net.twistpvp.plugindisabler.events.ChatEvent;
import net.twistpvp.plugindisabler.events.GUIEvent;
import net.twistpvp.plugindisabler.update.UpdateChecker;
import net.twistpvp.plugindisabler.update.UpdateMessageOnJoin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class PluginDisabler extends JavaPlugin {

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();

        createConfig(config);

        getCommand("pluginhider").setExecutor(new PluginHiderCommand(this));
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        getServer().getPluginManager().registerEvents(new GUIEvent(this), this);
        getServer().getPluginManager().registerEvents(new UpdateMessageOnJoin(this), this);

        if(config.getBoolean("check-for-updates")) {
            getLogger().info("Checking for updates...");

            new UpdateChecker(this, 19254).getVersion(version -> {
                if(getDescription().getVersion().equals(version)) {
                    getLogger().info("Plugin is up to date!");
                } else {
                    getLogger().info("There is a new update available on Spigot!");
                    getLogger().info("Check it out here: https://www.spigotmc.org/resources/command-blocker.109398/");
                }
            });
        }
    }

    @Override
    public void onDisable() {
        saveConfig(getConfig());
    }

    public void createConfig(FileConfiguration config) {
        List<String> disabledCommands = config.getStringList("disabled-commands");

        Storage.disabledCommands.addAll(disabledCommands);
        if(!config.contains("message")) {
            config.set("message", "&cYou are not allowed to execute this command!");
            saveConfig(config);
        }
        if(!config.contains("check-for-updates")) {
            config.set("check-for-updates", true);
            saveConfig(config);
        }
    }

    public void saveConfig(FileConfiguration config) {
        config.set("disabled-commands", Storage.disabledCommands);
        try {
            config.save(getDataFolder() + File.separator + "config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
