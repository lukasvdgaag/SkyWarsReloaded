package com.walrusone.skywarsreloaded.listeners;

import com.walrusone.skywarsreloaded.game.GameMap;
import com.walrusone.skywarsreloaded.managers.MatchManager;
import org.bukkit.block.Block;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileSpleefListener implements org.bukkit.event.Listener {
    public ProjectileSpleefListener() {
    }

    @org.bukkit.event.EventHandler
    public void projectileHitEvent(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof org.bukkit.entity.Player)) {
            return;
        }
        GameMap gMap = MatchManager.get().getPlayerMap((org.bukkit.entity.Player) e.getEntity().getShooter());
        if ((gMap != null) && (gMap.isProjectileSpleefEnabled())) {
            Projectile projectile = e.getEntity();
            if ((projectile instanceof org.bukkit.entity.EnderPearl)) {
                return;
            }
            Block block = com.walrusone.skywarsreloaded.SkyWarsReloaded.getNMS().getHitBlock(e);
            if (block == null) {
                return;
            }
            block.breakNaturally();
        }
    }
}
