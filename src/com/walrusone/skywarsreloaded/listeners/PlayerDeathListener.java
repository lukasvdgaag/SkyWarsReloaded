package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.SkyWarsReloaded;
import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerData;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;
import com.walrusone.skywarsreloaded.utilities.Util;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeathListener implements org.bukkit.event.Listener {
    public PlayerDeathListener() {
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDeath2(PlayerDeathEvent v2) {
        GameMap gameMap = MatchManager.get().getPlayerMap(v2.getEntity());

        if (gameMap == null) {
            return;
        }

        Player player = v2.getEntity();
        v2.getEntity().getInventory().clear();
        v2.getEntity().getInventory().setArmorContents(null);

        EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.CUSTOM;
        if (v2.getEntity().getLastDamageCause() != null) {
            damageCause = v2.getEntity().getLastDamageCause().getCause();
        }
        EntityDamageEvent.DamageCause dCause = damageCause;
        v2.setDeathMessage("");

        MatchManager.get().playerLeave(player, dCause, false, true, true);
    }

    @EventHandler
    public void onQuickDeath(PlayerMoveEvent e) {
        GameMap gameMap = MatchManager.get().getPlayerMap(e.getPlayer());

        if (gameMap == null) {
            return;
        }

        if (gameMap.getMatchState() == MatchState.ENDING && gameMap.getAlivePlayers().contains(e.getPlayer()) && e.getTo().getY() <= -10) {
            CoordLoc loc = gameMap.getSpectateSpawn();
            e.getPlayer().teleport(new Location(gameMap.getCurrentWorld(), loc.getX(), loc.getY(), loc.getZ()));
        }

        if (e.getPlayer().getGameMode().equals(GameMode.SURVIVAL)) {
            if (SkyWarsReloaded.getCfg().getEnableQuickDeath()) {
                if (e.getTo().getY() <= SkyWarsReloaded.getCfg().getQuickDeathY()) {
                    if (gameMap.getMatchState() == MatchState.PLAYING) {
                        EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.CUSTOM;
                        if (e.getPlayer().getLastDamageCause() != null) {
                            damageCause = e.getPlayer().getLastDamageCause().getCause();
                        }
                        MatchManager.get().playerLeave(e.getPlayer(), damageCause, false, true, true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent a1) {
        final PlayerData pData = PlayerData.getPlayerData(a1.getPlayer().getUniqueId());
        if (pData != null) {
            if (SkyWarsReloaded.getCfg().spectateEnable()) {
                final GameMap gMap = MatchManager.get().getDeadPlayerMap(a1.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = new Location(world, 0.0D, 95.0D, 0.0D);
                    a1.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            MatchManager.get().addSpectator(gMap, a1.getPlayer());
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 15L);
                }
            } else {
                GameMap gMap = MatchManager.get().getDeadPlayerMap(a1.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = new Location(world, 0.0D, 200.0D, 0.0D);
                    a1.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            pData.restore(false);
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 15L);
                }
            }
        }
        if (Util.get().isSpawnWorld(a1.getPlayer().getWorld())) {
            a1.setRespawnLocation(SkyWarsReloaded.getCfg().getSpawn());
            com.walrusone.skywarsreloaded.managers.PlayerStat.updatePlayer(a1.getPlayer().getUniqueId().toString());
        }
    }
}
