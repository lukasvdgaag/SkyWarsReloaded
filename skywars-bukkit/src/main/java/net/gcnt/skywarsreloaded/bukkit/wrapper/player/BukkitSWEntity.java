package net.gcnt.skywarsreloaded.bukkit.wrapper.player;

import io.papermc.lib.PaperLib;
import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.BukkitSkyWarsReloaded;
import net.gcnt.skywarsreloaded.bukkit.wrapper.world.BukkitSWWorld;
import net.gcnt.skywarsreloaded.utils.CoreSWCCompletableFuture;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;
import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.entity.AbstractSWEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class BukkitSWEntity extends AbstractSWEntity {

    private final SkyWarsReloaded plugin;
    private final Entity entity;

    public BukkitSWEntity(SkyWarsReloaded plugin, Entity entity) {
        super(entity.getUniqueId());
        this.plugin = plugin;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public SWCoord getLocation() throws NullPointerException {
        if (this.entity == null) throw new NullPointerException("Bukkit player is null");
        final Location location = entity.getLocation();
        return new CoreSWCoord(new BukkitSWWorld((BukkitSkyWarsReloaded) plugin, location.getWorld()), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    @Override
    public void teleport(SWCoord coord) {
        teleport(coord.getWorld().getName(), coord.xPrecise(), coord.yPrecise(), coord.zPrecise(), coord.yaw(), coord.pitch());
    }

    @Override
    public void teleport(String world, double x, double y, double z) throws NullPointerException {
        if (this.entity == null) throw new NullPointerException("Bukkit player is null");
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) return;
        entity.teleport(new Location(bukkitWorld, x, y, z));
    }

    @Override
    public void teleport(String world, double x, double y, double z, float yaw, float pitch) throws NullPointerException {
        if (this.entity == null) throw new NullPointerException("Bukkit player is null");
        World bukkitWorld = Bukkit.getWorld(world);
        if (bukkitWorld == null) return;
        entity.teleport(new Location(bukkitWorld, x, y, z, yaw, pitch));
    }

    @Override
    public SWCompletableFuture<Boolean> teleportAsync(SWCoord coord) {
        return this.teleportAsync(coord.getWorld().getName(), coord.xPrecise(), coord.yPrecise(), coord.zPrecise());
    }

    @Override
    public SWCompletableFuture<Boolean> teleportAsync(String world, double x, double y, double z) {
        World bukkitWorld = Bukkit.getWorld(world);
        CoreSWCCompletableFuture<Boolean> successFuture = new CoreSWCCompletableFuture<>(this.plugin);
        if (bukkitWorld == null || entity == null) {
            successFuture.complete(false);
            return successFuture;
        }
        final Location location = new Location(bukkitWorld, x, y, z);
        PaperLib.teleportAsync(entity, location).thenAccept(successFuture::complete);
        return successFuture;
    }

    @Override
    public double getHealth() {
        if (this.entity == null) throw new NullPointerException("Bukkit player is null");
        return (entity instanceof LivingEntity) ? ((LivingEntity) this.entity).getHealth() : 0;
    }

    @Override
    public void setHealth(double health) {
        if (this.entity == null) throw new NullPointerException("Bukkit player is null");
        if (entity instanceof LivingEntity) {
            ((LivingEntity) this.entity).setHealth(health);
        }
    }

    @Override
    public void setFireTicks(int ticks) {
        if (this.entity == null) throw new NullPointerException("Bukkit player is null");
        this.entity.setFireTicks(ticks);
    }

    @Override
    public String getType() {
        return entity == null ? null : this.entity.getType().name();
    }
}
