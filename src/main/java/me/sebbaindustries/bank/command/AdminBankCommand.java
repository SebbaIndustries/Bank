package me.sebbaindustries.bank.command;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.Permission;
import me.mattstudios.mf.base.CommandBase;
import me.sebbaindustries.bank.BankPlugin;
import me.sebbaindustries.bank.util.Color;
import me.sebbaindustries.bank.util.Replace;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@Command("adminbank")
public final class AdminBankCommand extends CommandBase {

    private static final String PERMISSION = "bank.admin";
    private static final String DEPOSIT_COMMAND = "+";
    private static final String WITHDRAW_COMMAND = "-";

    private final BankPlugin plugin;
    private final ConfigurationSection messages;

    public AdminBankCommand(final BankPlugin plugin) {
        this.plugin = plugin;
        this.messages = plugin.getConfig().getConfigurationSection("messages");
    }

    @Default
    @Permission(PERMISSION)
    public void adminCommand(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Color.colorize(messages.getString("bankAdminInvalidPlayer")));
            return;
        }

        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(Color.colorize(messages.getString("bankAdminInvalidPlayer")));
            return;
        }

        if (args.length == 1) {
            sender.sendMessage(Color.colorize(Replace.replaceString(
                    messages.getString("bankAdminBalanceMessage"),
                    "%player%", player.getName(),
                    "%balance%", String.valueOf(plugin.getPlayerBalance(player)))));
            return;
        }

        final String argument = args[1];
        if (argument == null || !argument.equalsIgnoreCase(DEPOSIT_COMMAND) && !argument.equalsIgnoreCase(WITHDRAW_COMMAND)) {
            sender.sendMessage(Color.colorize(messages.getString("bankAdminInvalidArgumentMessage")));
            return;
        }

        long amount;
        try {
            amount = Long.valueOf(args[2]);
        } catch (final NumberFormatException ignored) {
            player.sendMessage(Color.colorize(messages.getString("invalidAmountMessage")));
            return;
        }

        if (amount == 0) {
            sender.sendMessage(Color.colorize(messages.getString("bankAdminInvalidAmountMessage")));
            return;
        }

        switch (argument) {
            case DEPOSIT_COMMAND:
                plugin.depositPlayerBalance(player, amount, true);
                break;
            case WITHDRAW_COMMAND:
                plugin.withdrawPlayerBalance(player, amount, true);
                break;
            default:
                sender.sendMessage(Color.colorize(messages.getString("bankAdminInvalidArgumentMessage")));
                return;
        }

        sender.sendMessage(Color.colorize(Replace.replaceString(
                messages.getString("bankAdminModifiedMessage"),
                "%player%", player.getName(),
                "%balance%", String.valueOf(plugin.getPlayerBalance(player))
        )));
    }
}
