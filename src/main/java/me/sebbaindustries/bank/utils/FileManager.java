package me.sebbaindustries.bank.utils;

import me.sebbaindustries.bank.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;
import java.util.logging.Level;

/**
 * @author sebbaindustries
 * @version 1.0
 * @see FileConfiguration
 * @see YamlConfiguration
 * @see File
 * @see IOException
 * @see InputStreamReader
 * @see Reader
 * @see Objects
 * @see Level
 */
public class FileManager {

    // Core instance
    private Core core = Core.getPlugin(Core.class);

    public File README;

    public void initializeREADME() {
        if (README == null) {
            README = new File(core.getDataFolder(), "README.md");
        }
        if (!README.exists()) {
            core.saveResource("README.md", false);
        }
    }

    public void initializeLogger(String id) {
        try {
            new File(core.getDataFolder(), "logs").mkdir();
            new File(core.getDataFolder(), id).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Messages XML file ready for parsing
     */
    public File messagesXML;

    /**
     * Loads and saves message.xml file to server plugin folder, file is then copied to memory
     */
    public void initializeMessages() {
        if (messagesXML == null) {
            messagesXML = new File(core.getDataFolder(), "messages.xml");
        }
        if (!messagesXML.exists()) {
            core.saveResource("messages.xml", false);
        }
    }

    /**
     * Messages XML file ready for parsing
     */
    public File bankDataXML;

    /**
     * Loads and saves message.xml file to server plugin folder, file is then copied to memory
     */
    public void initializeBankData() {
        if (bankDataXML == null) {
            bankDataXML = new File(core.getDataFolder(), "bank data.xml");
        }
        if (!bankDataXML.exists()) {
            core.saveResource("bank data.xml", false);
        }
    }

    /*
    YAML Configuration file
     */
    private static FileConfiguration fileConfiguration = null;
    private static File configurationFile = null;

    /**
     * Reloads configuration file with saved file
     */
    public void reloadConfiguration() {
        if (configurationFile == null) {
            configurationFile = new File(core.getDataFolder(), "Configuration.yml");

        }
        fileConfiguration = YamlConfiguration.loadConfiguration(configurationFile);

        Reader defConfigStream = new InputStreamReader(Objects.requireNonNull(core.getResource("Configuration.yml")));
        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
        fileConfiguration.setDefaults(defConfig);
    }

    /**
     * Gets configuration file for reading/writing
     * @return configuration file instance
     */
    public FileConfiguration getConfiguration() {
        if (fileConfiguration == null) {
            reloadConfiguration();
        }
        return fileConfiguration;
    }

    /**
     * Saves configuration to its folder
     */
    public void saveConfiguration() {
        if (fileConfiguration == null || configurationFile == null) {
            return;
        }
        try {
            getConfiguration().save(configurationFile);
        } catch (IOException ex) {
            core.getLogger().log(Level.SEVERE, "Could not save Configuration to " + configurationFile, ex);
        }
    }


    /**
     * Configuration loader - Only used onEnable
     */
    public void initializeConfiguration() {
        if (configurationFile == null) {
            configurationFile = new File(core.getDataFolder(), "Configuration.yml");
        }
        if (!configurationFile.exists()) {
            core.saveResource("Configuration.yml", false);
        }
    }
}
