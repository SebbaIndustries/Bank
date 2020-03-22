package me.sebbaindustries.bank.globals;

import me.sebbaindustries.bank.utils.FileManager;
import me.sebbaindustries.bank.utils.Messages;
import net.milkbowl.vault.economy.Economy;

import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * @author sebbaindustries
 * @version 1.0
 * @see FileManager
 * @see Messages
 */
public class GlobalCore {

    // Default because Johnny will 100% fuck up in configuration :3
    public String LANG = "EN";
    public Boolean debug = false;
    public String DATESTR = null;

    public Economy econ = null;

    public FileManager fileManager = new FileManager();
    public Messages message = new Messages();

    public FileHandler handler;

    public Logger logger;

}
