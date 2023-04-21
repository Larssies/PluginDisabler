package net.twistpvp.plugindisabler;

import net.twistpvp.plugindisabler.commands.PluginHiderCommand;
import net.twistpvp.plugindisabler.events.ChatEvent;
import net.twistpvp.plugindisabler.events.GUIEvent;
import net.twistpvp.plugindisabler.update.UpdateChecker;
import net.twistpvp.plugindisabler.update.UpdateMessageOnJoin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.List;

public final class PluginDisabler extends JavaPlugin {

    @Override
    public void onEnable() {

        File configFile = new File(getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if(!configFile.exists()) {
            saveResource("config.yml", false);
        }

        createConfig(config);
        saveConfig(config);


        getCommand("pluginhider").setExecutor(new PluginHiderCommand(this));
        getServer().getPluginManager().registerEvents(new ChatEvent(this), this);
        //getServer().getPluginManager().registerEvents(new GUIEvent(this), this);
        getServer().getPluginManager().registerEvents(new UpdateMessageOnJoin(this), this);

        getLogger().info("Setting up database...");
        setupDatabase(config);
        loadDisabledCommandsFromDatabase(config);

        if(config.getBoolean("check-for-updates")) {
            getLogger().info("Checking for updates...");

            new UpdateChecker(this, 109398).getVersion(version -> {
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
        }
        if(!config.contains("check-for-updates")) {
            config.set("check-for-updates", true);
        }
        if(!config.contains("database.enabled")) {
            config.set("database.enabled", false);
        }
        if(!config.contains("database.mysql.url")) {
            config.set("database.mysql.url", "jdbc:mysql://localhost:3306/mydatabase");
        }
        if(!config.contains("database.mysql.username")) {
            config.set("database.mysql.username", "myuser");
        }
        if(!config.contains("database.mysql.password")) {
            config.set("database.mysql.password", "mypassword");
        }
    }

    public void saveConfig(FileConfiguration config) {
        config.set("disabled-commands", Storage.disabledCommands);
        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setupDatabase(FileConfiguration config) {
        if (config.getBoolean("database.enabled")) {
            try {
                String url = config.getString("database.mysql.url");
                String username = config.getString("database.mysql.username");
                String password = config.getString("database.mysql.password");

                try (Connection conn = DriverManager.getConnection(url, username, password)) {
                    String createTable = "CREATE TABLE IF NOT EXISTS disabled_commands ("
                            + "id INT AUTO_INCREMENT PRIMARY KEY,"
                            + "command_name VARCHAR(255) NOT NULL)";
                    try (PreparedStatement createStatement = conn.prepareStatement(createTable)) {
                        createStatement.executeUpdate();
                    }

                    String insertCommand = "INSERT INTO disabled_commands (command_name) VALUES (?)";
                    try (PreparedStatement insertStatement = conn.prepareStatement(insertCommand)) {
                        for (String command : Storage.disabledCommands) {
                            insertStatement.setString(1, command);
                            int rowsInserted = insertStatement.executeUpdate();
                            if (rowsInserted > 0) {
                                getLogger().info("Command '" + command + "' has been added to the list!");
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                getLogger().info("MySQL Error: " + e.getMessage());
            }
        } else {
            getLogger().info("Database is disabled! Enable it in the config!");
        }
    }

    public void loadDisabledCommandsFromDatabase(FileConfiguration config) {
        if (config.getBoolean("database.enabled")) {
            try (Connection conn = DriverManager.getConnection(config.getString("database.mysql.url"),
                    config.getString("database.mysql.username"),
                    config.getString("database.mysql.password"))) {
                String selectCommands = "SELECT command_name FROM disabled_commands";
                try (PreparedStatement selectStatement = conn.prepareStatement(selectCommands);
                     ResultSet result = selectStatement.executeQuery()) {
                    while (result.next()) {
                        String commandName = result.getString("command_name");
                        Storage.disabledCommands.add(commandName);
                    }
                    getLogger().info("Disabled commands loaded from the database: " + Storage.disabledCommands);
                }
            } catch (SQLException e) {
                getLogger().info("MySQL Error: " + e.getMessage());
            }
        }
    }
}
