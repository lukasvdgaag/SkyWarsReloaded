package net.gcnt.skywarsreloaded.wrapper.player;

import net.gcnt.skywarsreloaded.utils.SWCompletableFuture;
import net.gcnt.skywarsreloaded.utils.SWCoord;

import java.util.UUID;

public interface SWEntity {

    UUID getUuid();

    SWCoord getLocation();

    void teleport(SWCoord coord);

    void teleport(String world, double x, double y, double z);

    void teleport(String world, double x, double y, double z, float yaw, float pitch);

    SWCompletableFuture<Boolean> teleportAsync(SWCoord coord);

    SWCompletableFuture<Boolean> teleportAsync(String world, double x, double y, double z);

    double getHealth();

    void setHealth(double health);

    void setFireTicks(int ticks);

    String getType();

}
