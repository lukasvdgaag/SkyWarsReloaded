package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.PlayerStat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerJoinListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent a1) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (SkyWarsReloaded.getCfg().getSpawn() != null && SkyWarsReloaded.getCfg().teleportOnJoin()) {
                    a1.getPlayer().teleport(SkyWarsReloaded.getCfg().getSpawn());
                }
            }
        }.runTaskLater(SkyWarsReloaded.get(), 1);

        if (SkyWarsReloaded.getCfg().promptForResource()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    a1.getPlayer().setResourcePack(SkyWarsReloaded.getCfg().getResourceLink());
                }
            }.runTaskLater(SkyWarsReloaded.get(), 20);
        }

        if (PlayerStat.getPlayerStats(a1.getPlayer()) != null) {
            PlayerStat.removePlayer(a1.getPlayer().getUniqueId().toString());
        }
        //new BukkitRunnable() {
           // @Override
            //public void run() {
        if (!SkyWarsReloaded.getCfg().bungeeMode()) {
            for (GameMap gMap : GameMap.getMaps()) {
                if (gMap.getCurrentWorld() != null && gMap.getCurrentWorld().equals(a1.getPlayer().getWorld())) {
                    if (SkyWarsReloaded.getCfg().getSpawn() != null) {
                        a1.getPlayer().teleport(SkyWarsReloaded.getCfg().getSpawn());
                    }
                }
            }
        }
        //    }
       // }.runTaskLater(SkyWarsReloaded.get(), 1);

        PlayerStat.getPlayers().add(new PlayerStat(a1.getPlayer()));
    }
}