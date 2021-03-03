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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
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

        MatchManager.get().removeAlivePlayer(player, dCause, false, true);
        gameMap.getGameBoard().updateScoreboard();
    }

    @EventHandler
    public void onQuickDeath(PlayerMoveEvent e) {
        GameMap gameMap = MatchManager.get().getPlayerMap(e.getPlayer());

        if (gameMap == null) {
            return;
        }

        if (gameMap.getMatchState() == MatchState.ENDING &&
                gameMap.getAlivePlayers().contains(e.getPlayer()) &&
                e.getTo().getY() <= SkyWarsReloaded.getCfg().getQuickDeathY()
        ) {
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
                        MatchManager.get().removeAlivePlayer(e.getPlayer(), damageCause, false, true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(final PlayerRespawnEvent a1) {
        final PlayerData pData = PlayerData.getPlayerData(a1.getPlayer().getUniqueId());
        if (pData != null) {
            if (SkyWarsReloaded.getCfg().spectateEnable()) {
                final GameMap gMap = MatchManager.get().getDeadPlayerMap(a1.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    CoordLoc cLoc = gMap.getSpectateSpawn();
                    Location respawn = new Location(world, cLoc.getX(), cLoc.getY(), cLoc.getZ());
                    a1.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            SkyWarsReloaded.get().getPlayerManager().addSpectator(gMap, a1.getPlayer());
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 1L);
                }
            } else {
                GameMap gMap = MatchManager.get().getDeadPlayerMap(a1.getPlayer());
                if (gMap != null) {
                    World world = gMap.getCurrentWorld();
                    Location respawn = SkyWarsReloaded.getCfg().getSpawn();
                    if (respawn == null) respawn = new Location(world, 0.0D, 200.0D, 0.0D);
                    a1.setRespawnLocation(respawn);
                    new BukkitRunnable() {
                        public void run() {
                            pData.restoreToBeforeGameState(false);
                        }
                    }.runTaskLater(SkyWarsReloaded.get(), 1L);
                }
            }
        }
        if (Util.get().isSpawnWorld(a1.getPlayer().getWorld())) {
            a1.setRespawnLocation(SkyWarsReloaded.getCfg().getSpawn());
            com.walrusone.skywarsreloaded.managers.PlayerStat.updatePlayer(a1.getPlayer().getUniqueId().toString());
        }
    }
}
