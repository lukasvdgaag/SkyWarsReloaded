package com.walrusone.skywarsreloaded.utilities.placeholders;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
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

        if (identifier.equalsIgnoreCase("elo")) {
            return "" + stat.getElo();
        } else if (identifier.equalsIgnoreCase("wins")) {
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
        } else {
            double statt;
            if (identifier.equals("kill_death")) {
                statt = (double) stat.getKills() / (double) stat.getDeaths();
                return String.format("%1$,.2f", statt);
            } else if (identifier.equals("win_loss")) {
                statt = (double) stat.getWins() / (double) stat.getLosses();
                return String.format("%1$,.2f", statt);
            } else {
                return null;
            }
        }

    }
}
