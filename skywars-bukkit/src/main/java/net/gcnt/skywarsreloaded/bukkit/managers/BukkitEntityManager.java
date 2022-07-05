package net.gcnt.skywarsreloaded.bukkit.managers;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWEntity;
import net.gcnt.skywarsreloaded.bukkit.wrapper.player.BukkitSWOwnedEntity;
import net.gcnt.skywarsreloaded.manager.EntityManager;
import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;

import java.util.UUID;

public class BukkitEntityManager implements EntityManager {

    private final SkyWarsReloaded plugin;

    public BukkitEntityManager(SkyWarsReloaded plugin) {
        this.plugin = plugin;
    }

    @Override
    public SWEntity getEntityByUUID(UUID uuid) {
        Entity entity = Bukkit.getEntity(uuid);
        if (entity == null) return null;

        if (entity.getType() == EntityType.PLAYER) {
            return plugin.getPlayerManager().getPlayerByUUID(uuid);
        } else if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;

            SWPlayer owner = null;
            if (projectile.getShooter() instanceof Player) {
                owner = plugin.getPlayerManager().getPlayerByUUID(((Player) projectile.getShooter()).getUniqueId());
            }

            return new BukkitSWOwnedEntity(plugin, entity, owner);
        } else if (entity instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) entity;

            SWPlayer owner = null;
            if (tnt.getSource() instanceof Player) {
                owner = plugin.getPlayerManager().getPlayerByUUID(tnt.getSource().getUniqueId());
            }

            return new BukkitSWOwnedEntity(plugin, entity, owner);
        }

        return new BukkitSWEntity(plugin, entity);
    }
}
