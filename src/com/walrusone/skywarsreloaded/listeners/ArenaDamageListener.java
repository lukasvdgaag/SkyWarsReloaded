package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.enums.MatchState;
import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.game.PlayerData;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.*;

public class ArenaDamageListener implements org.bukkit.event.Listener {
    public ArenaDamageListener() {
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST)
    public void playerDamagedByAlly(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if ((event.getEntity() instanceof Player)) {
            Player target = (Player) event.getEntity();
            GameMap gameMap = MatchManager.get().getPlayerMap(target);
            if ((gameMap != null) && (!gameMap.getSpectators().contains(target.getUniqueId()))) {
                if ((gameMap.getMatchState() == MatchState.ENDING) || (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY)) {
                    event.setCancelled(true);
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


    private void doProjectile(GameMap gMap, Entity damager, EntityDamageByEntityEvent event, Player target) {
        Projectile proj = (Projectile) damager;
        if ((damager instanceof org.bukkit.entity.Snowball)) {
            event.setDamage(com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().getSnowDamage());
        }
        if ((damager instanceof org.bukkit.entity.Egg)) {
            event.setDamage(com.walrusone.skywarsreloaded.SkyWarsReloaded.getCfg().getEggDamage());
        }
        if (gMap.isDoubleDamageEnabled()) {
            event.setDamage(event.getDamage() * 2.0D);
        }
        if ((proj.getShooter() instanceof Player)) {
            Player hitter = (Player) proj.getShooter();
            if ((hitter != null) && (hitter != target)) {
                PlayerData pd = PlayerData.getPlayerData(target.getUniqueId());
                if (pd != null) {
                    pd.setTaggedBy(hitter);
                }
            }
        }
    }

    private void doPVP(Entity damager, Player target, EntityDamageByEntityEvent event, GameMap gMap) {
        Player hitter = (Player) damager;
        PlayerData pd = PlayerData.getPlayerData(target.getUniqueId());
        if (gMap.isDoubleDamageEnabled()) {
            event.setDamage(event.getDamage() * 2.0D);
        }
        if (pd != null) {
            pd.setTaggedBy(hitter);
        }
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
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
            if (gameMap != null && (gameMap.getMatchState() == MatchState.WAITINGSTART || gameMap.getMatchState() == MatchState.WAITINGLOBBY)) {
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

    /*@EventHandler
    public void arrowEvent(ProjectileHitEvent event) {
        if ((event.getEntity() instanceof Arrow)) {
            Arrow arrow = (Arrow) event.getEntity();
            if ((arrow.getShooter() instanceof Player)) {
                Player player = (Player) arrow.getShooter();
                GameMap gameMap = MatchManager.get().getPlayerMap(player);
                if (gameMap == null) // changed this from gameMap != null to ==.
                    arrow.remove();
            }
        }
    }*/

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
