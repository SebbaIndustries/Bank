package me.sebbaindustries.bank.util;

import me.sebbaindustries.bank.BankPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class LoggerManager {

    private final BankPlugin plugin;

    public LoggerManager(final BankPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize(final String id) {
        try {
            new File(plugin.getDataFolder(), "logs").mkdir();
            new File(plugin.getDataFolder(), id).createNewFile();
        } catch (final IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to generated file " + id, ex);
        }
    }


}
