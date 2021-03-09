package com.walrusone.skywarsreloaded.utilities.placeholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SWRMVdWPlaceholder {

    public SWRMVdWPlaceholder(Plugin p) {

        PlaceholderAPI.registerPlaceholder(p, "swr_wins", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWarsReloaded) p).getPlayerStat(player).getWins();
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_losses", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWarsReloaded) p).getPlayerStat(player).getLosses();
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_kills", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWarsReloaded) p).getPlayerStat(player).getKills();
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_deaths", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWarsReloaded) p).getPlayerStat(player).getDeaths();
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_xp", e -> {
            Player player = e.getPlayer();
            return "" + ((SkyWarsReloaded) p).getPlayerStat(player).getXp();
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_games_played", e -> {
            Player player = e.getPlayer();
            return "" + (((SkyWarsReloaded) p).getPlayerStat(player).getLosses() + ((SkyWarsReloaded) p).getPlayerStat(player).getWins());
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_kill_death", e -> {
            Player player = e.getPlayer();
            double stat = (double) ((SkyWarsReloaded) p).getPlayerStat(player).getKills() / (double) ((SkyWarsReloaded) p).getPlayerStat(player).getDeaths();
            return String.format("%1$,.2f", stat);
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_win_loss", e -> {
            Player player = e.getPlayer();
            double stat = (double) ((SkyWarsReloaded) p).getPlayerStat(player).getWins() / (double) ((SkyWarsReloaded) p).getPlayerStat(player).getLosses();
            return String.format("%1$,.2f", stat);
        });

        PlaceholderAPI.registerPlaceholder(p, "swr_level", e -> {
            Player player = e.getPlayer();
            return "" + Util.get().getPlayerLevel(player, false);
        });
    }
}