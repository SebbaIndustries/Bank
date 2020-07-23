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

import java.util.*;
import java.util.stream.Collectors;

@Command("banktop")
public final class BankTopCommand extends CommandBase {

    private static final String PERMISSION = "bank.top";

    private final BankPlugin plugin;
    private final ConfigurationSection messages;

    public BankTopCommand(final BankPlugin plugin) {
        this.plugin = plugin;

        this.messages = plugin.getConfig().getConfigurationSection("messages");
    }

    @Default
    @Permission(PERMISSION)
    public void defaultCommand(final CommandSender sender) {
        Map<Player, Long> balances = new HashMap<>();
        final Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

        for (final Player player : players) {
            balances.put(player, plugin.getPlayerBalance(player));
        }

        balances = getReversedMap(balances);

        for (final String line : messages.getStringList("bankTopHeaderMessage")) {
            sender.sendMessage(Color.colorize(Replace.replaceString(line, "%total%", String.valueOf(getServerTotal(balances)))));
        }

        int amount = 0;
        for (final Player player : balances.keySet()) {
            if (amount == messages.getInt("bankTopAmount")) break;
            sender.sendMessage(Color.colorize(Replace.replaceString(
                    messages.getString("bankTopListerMessage"),
                    "%position%", String.valueOf(amount + 1),
                    "%player%", player.getName(),
                    "%balance%", String.valueOf(balances.get(player)))));
            amount++;
        }

        messages.getStringList("bankTopFooterMessage").forEach(line -> sender.sendMessage(Color.colorize(line)));
    }

    private Map<Player, Long> getReversedMap(final Map<Player, Long> input) {
        return input.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private Long getServerTotal(final Map<Player, Long> input) {
        Long total = 0L;

        for (final Player player : input.keySet()) {
            total += input.get(player);
        }

        return total;
    }

}
