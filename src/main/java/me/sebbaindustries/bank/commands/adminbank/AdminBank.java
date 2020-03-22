package me.sebbaindustries.bank.commands.adminbank;

import me.sebbaindustries.bank.Core;
import me.sebbaindustries.bank.local.BankData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.xml.sax.SAXException;
//import org.jdom2.JDOMException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class AdminBank {

    private Player p;
    private String[] args;
    private String illegalNum = null;

    public AdminBank(Player p, String[] args) {
        this.p = p;
        this.args = args;
    }

    /*
    Admin bank commands:
    - /adminbank <player>
    - /adminbank <player> + <amount>
    - /adminbank <player> - <amount>
     */
    public void execute() {

        // Player does not have permission to use this command
        if (!p.hasPermission("bank.admin")) {
            Core.gCore.message.sendMessage(p,"no permission message");
            return;
        }

        // show help
        if (args.length == 0) {
            help();
            return;
        }

        Player target;
        // shows targets balance
        if (args.length == 1) {

            // Check if player exists
            try {
                target = (Player) Bukkit.getOfflinePlayer(args[0]);
            } catch (ClassCastException ignored) {
                Core.gCore.message.sendMessage(p, "account does not exists message");
                return;
            }

            // Check if player has bank account
            if (!new BankData(target).doesBankExists()) {
                Core.gCore.message.sendMessage(p, "account does not exists message");
                return;
            }
            p.sendMessage(Core.gCore.message.getMessage("amount check message").replace("%amount%", String.valueOf(new BankData(target).getBankAcc())));
            return;
        }

        // show help
        if (args.length == 2) {
            help();
            return;
        }

        // Add or remove money from the bank
        if (args.length == 3) {

            // Check if player exists
            try {
                target = (Player) Bukkit.getOfflinePlayer(args[0]);
            } catch (ClassCastException ignored) {
                Core.gCore.message.sendMessage(p, "account does not exists message");
                return;
            }

            // Check if player has bank account
            if (!new BankData(target).doesBankExists()) {
                Core.gCore.message.sendMessage(p, "account does not exists message");
                return;
            }

            if (!args[1].equalsIgnoreCase("+") && !args[1].equalsIgnoreCase("-")) {
                help();
                return;
            }

            // Get amount of money check for null double ect..
            int amount = parseInt(args[2]);
            if (!(amount > 0)) {
                p.sendMessage(Core.gCore.message.getMessage("invalid amount message").replace("%amount%", illegalNum));
                return;
            }


            // ADD to the bank
            if (args[1].equalsIgnoreCase("+")) {
                // execute transaction
                new BankData(target).modifyBank(amount);
                p.sendMessage(Core.gCore.message.getMessage("amount add message").replace("%amount%", String.valueOf(amount)));
                return;
            }

            // REMOVE from the bank
            if (args[1].equalsIgnoreCase("-")) {
                // Check if does player have enough money in tha bank
                int currentAmount = new BankData(target).getBankAcc();
                if (currentAmount < amount) {
                    Core.gCore.message.sendMessage(p, "target does not enough money in bank message");
                    return;
                }
                // execute transaction
                new BankData(target).modifyBank(-amount);
                p.sendMessage(Core.gCore.message.getMessage("amount remove message").replace("%amount%", String.valueOf(amount)));
                return;
            }
            return;
        }
        help();
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NullPointerException | NumberFormatException e) {
            illegalNum = s;
            return 0;
        }
    }

    private void help() {
        for (String s : Core.gCore.message.getHelpList("#adminbankcommand")) {
            p.sendMessage(s);
        }
    }

}
