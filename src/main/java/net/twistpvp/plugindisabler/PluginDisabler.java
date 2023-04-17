package net.twistpvp.plugindisabler;

import net.twistpvp.plugindisabler.commands.AddHidedCommand;
import net.twistpvp.plugindisabler.events.ChatEvent;
import net.twistpvp.plugindisabler.events.GUIEvent;
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

        getCommand("pluginhider").setExecutor(new AddHidedCommand());
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        getServer().getPluginManager().registerEvents(new GUIEvent(), this);

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
