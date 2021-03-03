package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import com.walrusone.skywarsreloaded.utilities.Party;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerQuitListener implements org.bukkit.event.Listener {
    public PlayerQuitListener() {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent a1) {
        final String id = a1.getPlayer().getUniqueId().toString();
        Party party = Party.getParty(a1.getPlayer());
        if (party != null) {
            party.removeMember(a1.getPlayer());
        }
        GameMap gameMap = MatchManager.get().getPlayerMap(a1.getPlayer());
        if (gameMap == null) {


            new BukkitRunnable() {
                public void run() {
                    PlayerStat.removePlayer(id);
                }
            }.runTaskLater(SkyWarsReloaded.get(), 5L);
            return;
        }

        MatchManager.get().removeAlivePlayer(a1.getPlayer(), EntityDamageEvent.DamageCause.CUSTOM, true, true);

        if (PlayerStat.getPlayerStats(id) != null) {
            new BukkitRunnable() {
                public void run() {
                    PlayerStat.removePlayer(id);
                }
            }.runTaskLater(SkyWarsReloaded.get(), 20L);
        }
    }
}
