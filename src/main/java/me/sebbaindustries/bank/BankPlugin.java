package me.sebbaindustries.bank;

import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.MessageHandler;
import me.sebbaindustries.bank.command.AdminBankCommand;
import me.sebbaindustries.bank.command.BankCommand;
import me.sebbaindustries.bank.command.BankTopCommand;
import me.sebbaindustries.bank.util.Color;
import me.sebbaindustries.bank.util.LoggerManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public final class BankPlugin extends JavaPlugin {
    private static final Logger LOGGER = Logger.getLogger("me.sebbaindustries.bank.BankPlugin");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss");
    private static final Date DATE = new Date();

    private final NamespacedKey NAMESPACED_KEY = new NamespacedKey(this, "bank-balance");
    private final LoggerManager loggerManager = new LoggerManager(this);

    private Economy economy;

    private static String log(final Player player, final Long amount) {
        return "UUID:" + player.getUniqueId() + " Name:" + player.getName() + " Transaction:" + amount;
    }

    public void onEnable() {
        saveDefaultConfig();

        if (!initializeEconomy()) {
            LOGGER.severe(String.format("[%s] - Failed to register dependency: Economy.class!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);

            return;
        }
        final CommandManager manager = new CommandManager(this);
        manager.register(new BankCommand(this), new BankTopCommand(this), new AdminBankCommand(this));

        registerMessages(manager.getMessageHandler());

        try {
            this.loggerManager.initialize("logs/" + DATE_FORMAT.format(DATE) + ".log");

            final FileHandler handler = new FileHandler(getDataFolder() + "/logs/" + DATE_FORMAT.format(DATE) + ".log", true);
            LOGGER.addHandler(handler);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private void registerMessages(MessageHandler handler) {
        final ConfigurationSection messages = getConfig().getConfigurationSection("messages");

        if (messages == null) {
            LOGGER.severe(String.format("[%s] - Failed to register messages: config.yml/messages!", getDescription().getName()));

            return;
        }
        handler.register("cmd.no.console", it -> it.sendMessage(Color.colorize(messages.getString("playerOnlyMessage"))));
        handler.register("cmd.no.permission", it -> it.sendMessage(Color.colorize(messages.getString("noPermissionMessage"))));
        handler.register("cmd.no.exists", it -> it.sendMessage(Color.colorize(messages.getString("invalidCommandMessage"))));
        handler.register("cmd.wrong.usage", it -> it.sendMessage(Color.colorize(messages.getString("wrongUsageMessage"))));
    }

    public void onDisable() {
        reloadConfig();
    }

    private boolean initializeEconomy() {
        final RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider == null) {
            return false;
        }

        this.economy = economyProvider.getProvider();
        return true;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public long getPlayerBalance(final Player player) {
        final Long balance = player.getPersistentDataContainer().get(NAMESPACED_KEY, PersistentDataType.LONG);

        return balance == null ? 0L : balance;
    }

    public void depositPlayerBalance(Player player, Long amount, boolean admin) {
        transactionExecute(player, amount);
        final long balance = getPlayerBalance(player);
        if (!admin) this.economy.withdrawPlayer(player, amount);

        final long updated = balance + amount;
        player.getPersistentDataContainer().set(this.NAMESPACED_KEY, PersistentDataType.LONG, updated);
        transactionSuccessful(player, amount);
    }

    public void withdrawPlayerBalance(Player player, Long amount, boolean admin) {
        transactionExecute(player, amount);
        final long balance = getPlayerBalance(player);
        if (!admin) this.economy.depositPlayer(player, amount);

        final long updated = balance - amount;
        player.getPersistentDataContainer().set(this.NAMESPACED_KEY, PersistentDataType.LONG, updated);
        transactionSuccessful(player, amount);
    }

    private void transactionExecute(final Player player, final Long amount) {
        LOGGER.info(String.format("[%s] - Executing Transaction@ " + log(player, amount), getDescription().getName()));
    }

    private void transactionSuccessful(final Player player, final Long amount) {
        LOGGER.info(String.format("[%s] - Executed Transaction Successfully@ " + log(player, amount), getDescription().getName()));
    }
}