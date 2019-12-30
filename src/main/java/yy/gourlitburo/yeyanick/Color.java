package yy.gourlitburo.yeyanick;

import org.bukkit.ChatColor;

class Color {
    static String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
