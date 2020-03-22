/*
    ${project.name} a Plugin By SebbaIndustries
    Version: ${project.version}

    Copyright 2020 SebbaIndustries

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package me.sebbaindustries.bank;

import me.sebbaindustries.bank.commands.CommandManager;
import me.sebbaindustries.bank.globals.GlobalCore;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * @author SebbaIndustries
 * @version 1.0
 * @see org.bukkit.command.CommandExecutor
 * @see org.bukkit.command.TabCompleter
 * @see org.bukkit.command.TabExecutor
 * @see org.bukkit.plugin.java.JavaPlugin
 * @see org.bukkit.plugin.PluginBase
 * @see GlobalCore
 */
public class Core extends JavaPlugin {

    // Global Core - Handles all public variables for easier use
    public static GlobalCore gCore;


    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        super.onEnable();
        initializeGlobalCore();
        loadFiles();

        setupDefaults();
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
        getCommand("adminbank").setExecutor(new CommandManager());
        Bukkit.getPluginCommand("adminbank").setTabCompleter(new CommandManager());
        getCommand("bank").setExecutor(new CommandManager());
        Bukkit.getPluginCommand("bank").setTabCompleter(new CommandManager());

        try {
            gCore.handler = new FileHandler(getDataFolder() + "/logs/" + gCore.DATESTR, true);
            gCore.logger = Logger.getLogger("me.sebbaindustries.bank.Core");
            gCore.logger.addHandler(gCore.handler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // System.out.println(gCore.message.getMessage("no permission message"));

    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /**
     * Setups all default values - Language, logging, debugger, ...
     */
    private void setupDefaults() {
        gCore.LANG = Objects.requireNonNull(gCore.fileManager.getConfiguration().getString("language")).toUpperCase();
        // gCore.logger = gCore.fileManager.getConfiguration().getBoolean("logger");
        // gCore.debug = gCore.fileManager.getConfiguration().getBoolean("debug");
    }

    /**
     * VAULT - setup economy on load
     * @return Economy setup
     */
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        gCore.econ = rsp.getProvider();
        return gCore.econ != null;
    }

    /**
     * Creates global class gCore for access to everything
     */
    private void initializeGlobalCore() {
        gCore = new GlobalCore();
    }

    /**
     * Loads all files that plugin needs -> Configuration, messages...
     */
    private void loadFiles() {
        gCore.fileManager.initializeConfiguration();
        gCore.fileManager.initializeMessages();
        gCore.fileManager.initializeBankData();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
        Date date = new Date();
        gCore.DATESTR = dateFormat.format(date)+".log";
        gCore.fileManager.initializeLogger("logs/" + dateFormat.format(date)+".log");
        gCore.fileManager.initializeREADME();
    }
}
