package com.walrusone.skywarsreloaded.game;

import org.bukkit.ChatColor;

public class GameMapComparator implements java.util.Comparator<GameMap> {
    public GameMapComparator() {
    }

    public int compare(GameMap f1, GameMap f2) {
        if ((f1 != null) && (f2 != null)) {
            return
                    ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', f1.getDisplayName())).compareTo(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', f2.getDisplayName())));
        }
        return 0;
    }
}
