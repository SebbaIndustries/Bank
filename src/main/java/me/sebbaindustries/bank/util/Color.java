package me.sebbaindustries.bank.util;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Color {

    private static final Pattern HEX_PATTERN = Pattern.compile("#<([A-Fa-f0-9]){6}>");

    public static String colorize(final String message) {
        String translation = message;

        Matcher matcher = HEX_PATTERN.matcher(translation);

        while (matcher.find()) {
            String hexString = matcher.group();

            hexString = "#" + hexString.substring(2, hexString.length() - 1);

            final ChatColor hex = ChatColor.of(hexString);
            final String before = translation.substring(0, matcher.start());
            final String after = translation.substring(matcher.end());

            translation = before + hex + after;
            matcher = HEX_PATTERN.matcher(translation);
        }

        return ChatColor.translateAlternateColorCodes('&', translation);
    }

}
