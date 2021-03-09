package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerData;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import com.walrusone.skywarsreloaded.matchevents.EnderDragonEvent;
import com.walrusone.skywarsreloaded.matchevents.MatchEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;

public class ArenaDamageListener implements org.bukkit.event.Listener {
    public ArenaDamageListener() {
    }

    @EventHandler
    public void dragonDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof EnderDragon) {
            GameMap map = GameMap.getMap(e.getEntity().getLocation().getWorld().getName());
            if (map == null) return;

            for (MatchEvent event : map.getEvents()) {
                if (event instanceof EnderDragonEvent) {
                    if (((EnderDragonEvent)event).makeDragonInvulnerable) {
                        e.setDamage(0);
                    }
                }
            }

         }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerDamagedByAlly(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if ((event.getEntity() instanceof Player)) {
            Player target = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(target);
            if ((gameMap != null) && (!gameMap.getSpectators().contains(target.getUniqueId()))) {
                if ((gameMap.getMatchState() == MatchState.ENDING || gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY) ||
                        gameMap.isDisableDamage()) {
                    event.setCancelled(true);
                // Friendly fire attack
                } else if (!gameMap.allowFriendlyFire() && damager instanceof Player && gameMap.getMatchState() == MatchState.PLAYING && gameMap.getTeamCard(target).equals(gameMap.getTeamCard((Player)damager))) {
                    event.setCancelled(true);
                // Friendly fire shoot
                } else if (!gameMap.allowFriendlyFire() && event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && gameMap.getMatchState() == MatchState.PLAYING && ((Projectile)damager).getShooter() != null
                        && ((Projectile)damager).getShooter() instanceof Player
                        && gameMap.getTeamCard(target).equals(gameMap.getTeamCard((Player) ((Projectile)damager).getShooter()))) {
                    event.setCancelled(true);
                // Process pvp events
                } else {
                    event.setCancelled(false);
                    if (gameMap.getProjectilesOnly()) {
                        if ((damager instanceof Projectile)) {
                            doProjectile(gameMap, damager, event, target);
                        } else if ((damager instanceof Player)) {
                            event.setCancelled(true);
                        }
                    } else if ((damager instanceof Projectile)) {
                        doProjectile(gameMap, damager, event, target);
                    } else if ((damager instanceof Player)) {
                        doPVP(damager, target, event, gameMap);
                    }
                }
            }
        }
    }


    private void doProjectile(GameMap gMap, Entity damager, EntityDamageByEntityEvent event, Player victim) {
        Projectile proj = (Projectile) damager;
        if ((damager instanceof Snowball)) {
            event.setDamage(com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().getSnowDamage());
        }
        if ((damager instanceof Egg)) {
            event.setDamage(com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().getEggDamage());
        }
        if (gMap.isDoubleDamageEnabled()) {
            event.setDamage(event.getDamage() * 2.0D);
        }
        if ((proj.getShooter() instanceof Player)) {
            Player attacker = (Player) proj.getShooter();
            if ((attacker != null) && (attacker != victim)) {
                PlayerData pd = PlayerData.getPlayerData(victim.getUniqueId());
                if (pd != null) {
                    pd.setTaggedBy(attacker);
                }
            }
        }
    }

    private void doPVP(Entity damager, Player victim, EntityDamageByEntityEvent event, GameMap gMap) {
        Player attacker = (Player) damager;
        PlayerData playerData = PlayerData.getPlayerData(victim.getUniqueId());
        if (gMap.isDoubleDamageEnabled()) {
            event.setDamage(event.getDamage() * 2.0D);
        }
        if (playerData != null && victim != damager) {
            playerData.setTaggedBy(attacker);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void playerDamaged(EntityDamageEvent event) {
        if ((event.getEntity() instanceof Player)) {
            Player player = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(player);
            if (gameMap != null) {
                if (gameMap.getMatchState() == MatchState.ENDING || gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY) {
                    event.setCancelled(true);
                } else if (!gameMap.allowFallDamage() && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    event.setCancelled(true);
                } else if (gameMap.isDisableDamage() && event.getCause() != EntityDamageEvent.DamageCause.VOID) {
                    event.setCancelled(true);
                } else {
                    event.setCancelled(false);
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void satLoss(FoodLevelChangeEvent event) {
        if ((event.getEntity() instanceof Player)) {
            Player player = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(player);
            if (gameMap != null && (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY || gameMap.getMatchState() == MatchState.ENDING)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void regen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if (gameMap != null && !gameMap.allowRegen()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void bowEvent(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        GameMap gameMap = MatchManager.get().getPlayerMap(player);
        if ((gameMap != null) && (
                (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY) || (gameMap.getMatchState() == MatchState.ENDING))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAnvilLand(EntityChangeBlockEvent event) {
        FallingBlock fb;
        if ((event.getEntity() instanceof FallingBlock)) {
            fb = (FallingBlock) event.getEntity();
            if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getNMS().checkMaterial(fb, org.bukkit.Material.ANVIL)) {
                for (GameMap gMap : GameMap.getPlayableArenas(com.walrusone.skywarsreloaded.enums.GameType.ALL)) {
                    if (gMap.getAnvils().contains(event.getEntity().getUniqueId().toString())) {
                        event.setCancelled(true);
                        gMap.getAnvils().remove(event.getEntity().getUniqueId().toString());
                        return;
                    }
                }
            } else if (com.walrusone.skywarsreloaded.SkyWarsReloaded.getNMS().checkMaterial(fb, org.bukkit.Material.SAND)) {
                for (GameMap gMap : GameMap.getPlayableArenas(com.walrusone.skywarsreloaded.enums.GameType.ALL)) {
                    for (com.walrusone.skywarsreloaded.game.Crate crate : gMap.getCrates()) {
                        if (fb.equals(crate.getEntity())) {
                            event.setCancelled(true);
                            fb.setDropItem(false);
                            fb.getWorld().getBlockAt(fb.getLocation()).setType(org.bukkit.Material.ENDER_CHEST);
                            crate.setLocation(fb.getWorld().getBlockAt(fb.getLocation()));
                            fb.remove();
                        }
                    }
                }
            }
        }
    }
}
