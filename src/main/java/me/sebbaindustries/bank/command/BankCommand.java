package me.sebbaindustries.bank.command;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import me.sebbaindustries.bank.BankPlugin;
import me.sebbaindustries.bank.util.Color;
import me.sebbaindustries.bank.util.Replace;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@Command("bank")
public final class BankCommand extends CommandBase {

    private static final String PERMISSION = "bank.player";
    private static final String HELP_COMMAND = "help";
    private static final String DEPOSIT_COMMAND = "+";
    private static final String WITHDRAW_COMMAND = "-";

    private final Economy economy;
    private final BankPlugin plugin;
    private final ConfigurationSection messages;

    public BankCommand(final BankPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig().getConfigurationSection("messages");
        this.economy = plugin.getEconomy();
    }

    @Default
    @Permission(PERMISSION)
    public void defaultCommand(final Player player) {
        player.sendMessage(Color.colorize(Replace.replaceString(
                messages.getString("amountCheckMessage"),
                "%amount%",
                String.valueOf(plugin.getPlayerBalance(player)))));
    }

    @SubCommand(HELP_COMMAND)
    @Permission(PERMISSION)
    public void helpCommand(final Player player) {
        messages.getStringList("bankHelpMessage").forEach(message -> player.sendMessage(Color.colorize(message)));
    }

    @SubCommand(DEPOSIT_COMMAND)
    @Permission(PERMISSION)
    public void depositCommand(final Player player, final String inputAmount) {
        final Long amount = handleAmount(player, inputAmount);

        if (amount <= 0) {
            player.sendMessage(Color.colorize(messages.getString("invalidAmountMessage")));
            return;
        }

        if (economy.getBalance(player) < amount) {
            player.sendMessage(Color.colorize(messages.getString("invalidMoneyAmountMessage")));
            return;
        }

        plugin.depositPlayerBalance(player, amount, false);
        player.sendMessage(Color.colorize(Replace.replaceString(
                messages.getString("amountAddMessage"),
                "%amount%",
                String.valueOf(amount)
        )));
    }

    @SubCommand(WITHDRAW_COMMAND)
    @Permission(PERMISSION)
    public void withdrawCommand(final Player player, final String inputAmount) {
        final Long amount = handleAmount(player, inputAmount);

        if (amount <= 0) {
            player.sendMessage(Color.colorize(messages.getString("invalidAmountMessage")));
            return;
        }

        if (plugin.getPlayerBalance(player) < amount) {
            player.sendMessage(Color.colorize(messages.getString("invalidMoneyAmountMessage")));
            return;
        }

        plugin.withdrawPlayerBalance(player, amount, false);
        player.sendMessage(Color.colorize(Replace.replaceString(
                messages.getString("amountRemoveMessage"),
                "%amount%",
                String.valueOf(amount)
        )));
    }

    private Long handleAmount(final Player player, final String inputAmount) {
        long amount;
        try {
            amount = Long.valueOf(inputAmount);
        } catch (final NumberFormatException ignored) {
            player.sendMessage(Color.colorize(messages.getString("invalidAmountMessage")));
            return 0L;
        }

        return amount;
    }
}
