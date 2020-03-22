package me.sebbaindustries.bank.commands.bank;

import me.sebbaindustries.bank.Core;
import me.sebbaindustries.bank.local.BankData;
import org.bukkit.entity.Player;

public class Bank {

    private Player p;
    private String[] args;
    private String illegalNum = null;

    public Bank(Player p, String[] args) {
        this.p = p;
        this.args = args;
    }

    /*
    Bank commands:
    - /bank
    - /bank + <amount>
    - /bank - <amount>
     */
    public void execute() {

        // Player does not have permission to use this command
        if (!p.hasPermission("bank.player")) {
            Core.gCore.message.sendMessage(p, "no permission message");
            return;
        }

        // Check if player has bank account
        if (!new BankData(p).doesBankExists()) new BankData(p).createBankAccount();

        // Display bank account
        if (args.length == 0) {
            p.sendMessage(Core.gCore.message.getMessage("amount check message").replace("%amount%", String.valueOf(new BankData(p).getBankAcc())));
            return;
        }

        // show help
        if (args.length == 1) {
            help();
            return;
        }

        // Add or remove money from the bank
        if (args.length == 2) {
            // + or -
            if (!(args[0].equalsIgnoreCase("+")) && !(args[0].equalsIgnoreCase("-"))) {
                help();
                return;
            }

            // Get amount of money check for null double ect..
            int amount = parseInt(args[1]);
            if (!(amount > 0)) {
                p.sendMessage(Core.gCore.message.getMessage("invalid amount message").replace("%amount%", illegalNum));
                return;
            }


            // ADD to the bank
            if (args[0].equalsIgnoreCase("+")) {
                // Check if does player have enough money on the account
                if (Core.gCore.econ.getBalance(p) < amount) {
                    Core.gCore.message.sendMessage(p, "you do not have money message");
                    return;
                }
                Core.gCore.logger.info("TRYING@" + logStr(p, amount));
                // execute transaction
                Core.gCore.econ.withdrawPlayer(p, amount);
                new BankData(p).modifyBank(amount);
                p.sendMessage(Core.gCore.message.getMessage("amount add message").replace("%amount%", String.valueOf(amount)));
                Core.gCore.logger.info("SUCCESS@" + logStr(p, amount));
                return;
            }

            // REMOVE from the bank
            if (args[0].equalsIgnoreCase("-")) {
                // Check if does player have enough money in tha bank
                int currentAmount = new BankData(p).getBankAcc();
                if (currentAmount < amount) {
                    Core.gCore.message.sendMessage(p, "not enough money in bank message");
                    return;
                }
                Core.gCore.logger.info("TRYING@" + logStr(p, -amount));
                // execute transaction
                new BankData(p).modifyBank(-amount);
                Core.gCore.econ.depositPlayer(p, amount);
                p.sendMessage(Core.gCore.message.getMessage("amount remove message").replace("%amount%", String.valueOf(amount)));
                Core.gCore.logger.info("SUCCESS@" + logStr(p, -amount));
                return;
            }

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
        for (String s : Core.gCore.message.getHelpList("#bankcommand")) {
            p.sendMessage(s);
        }
    }

    private String logStr(Player p, int amount) {
        return "UUID:" + p.getUniqueId() + " " + "Name:" + p.getName() + " " + "Transaction:" + amount;
    }

}
