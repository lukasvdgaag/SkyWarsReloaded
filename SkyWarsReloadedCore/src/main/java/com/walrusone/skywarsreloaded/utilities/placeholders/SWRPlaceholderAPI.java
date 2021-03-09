package com.walrusone.skywarsreloaded.utilities.placeholders;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Util;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class SWRPlaceholderAPI extends PlaceholderExpansion {

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "swr";
    }

    @Override
    public String getAuthor() {
        return "GaagjesCraft Network Team";
    }

    @Override
    public String getVersion() {
        return SkyWarsReloaded.get().getDescription().getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }

        PlayerStat stat = SkyWarsReloaded.get().getPlayerStat(p);
        if (identifier.equalsIgnoreCase("wins")) {
            return "" + stat.getWins();
        } else if (identifier.equalsIgnoreCase("losses")) {
            return "" + stat.getLosses();
        } else if (identifier.equalsIgnoreCase("kills")) {
            return "" + stat.getKills();
        } else if (identifier.equalsIgnoreCase("deaths")) {
            return "" + stat.getDeaths();
        } else if (identifier.equalsIgnoreCase("xp")) {
            return "" + stat.getXp();
        } else if (identifier.equalsIgnoreCase("games_played") ||
                identifier.equalsIgnoreCase("games")) {
            return "" + (stat.getWins() + stat.getLosses());
        } else if (identifier.equalsIgnoreCase("level")) {
            return "" + Util.get().getPlayerLevel(p, false);
        } else if (identifier.equalsIgnoreCase("kill_death")) {
            double stat1 = (double) stat.getKills() / (double) stat.getDeaths();
            return String.format("%1$,.2f", stat1);
        } else if (identifier.equalsIgnoreCase("win_loss")) {
            double stat1 = (double) stat.getWins() / (double) stat.getLosses();
            return String.format("%1$,.2f", stat1);
        }
        else {
            return null;
        }

    }
}
