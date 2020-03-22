package me.sebbaindustries.bank.utils;

import org.bukkit.ChatColor;

/**
 * @author sebbaindustries
 * @version 1.0
 * @see ChatColor
 */
public class Color {

    /**
     * Chat color translator
     * @param s String that needs translating
     * @return Translated string
     */
    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
