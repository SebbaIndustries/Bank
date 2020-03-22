package me.sebbaindustries.bank.commands;

import me.sebbaindustries.bank.Core;
import me.sebbaindustries.bank.commands.adminbank.AdminBank;
import me.sebbaindustries.bank.commands.bank.Bank;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor, TabCompleter {

    /*
    Bank commands:
    - /bank
    - /bank + <amount>
    - /bank - <amount>

    Admin bank commands:
    - /adminbank <player>
    - /adminbank <player> + <amount>
    - /adminbank <player> - <amount>
     */

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (label.equalsIgnoreCase("adminbank")) {
            new AdminBank((Player) sender, args).execute();
            return true;
        }

        if (label.equalsIgnoreCase("bank")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Core.gCore.message.getMessage("Console cannot execute message"));
                return true;
            }
            new Bank((Player) sender, args).execute();
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (label.equalsIgnoreCase("adminbank")) {
            if (args.length == 1) {
                return null;
            }
            if (args.length == 2) {
                List<String> tab = new ArrayList<>();
                tab.add("+");
                tab.add("-");
                return tab;
            }
            if (args.length == 3) {
                List<String> tab = new ArrayList<>();
                tab.add("[amount]");
                return tab;
            }
        }
        if (label.equalsIgnoreCase("bank")) {
            if (args.length == 1) {
                List<String> tab = new ArrayList<>();
                tab.add("+");
                tab.add("-");
                return tab;
            }
            if (args.length == 2) {
                List<String> tab = new ArrayList<>();
                tab.add("[amount]");
                return tab;
            }
        }
        return null;
    }

}
