package com.walrusone.skywarsreloaded.menus.gameoptions.objects;

import org.bukkit.Location;

import java.util.Objects;

public class CoordLoc {

    private final int x;
    private final int z;
    private final int y;

    public CoordLoc(Location locIn) {
        this(locIn.getBlockX(), locIn.getBlockY(), locIn.getBlockZ());
    }

    public CoordLoc(int x, int y, int z) {
        this.x = x;
        this.z = z;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public int getY() {
        return y;
    }

    public String getLocationString() {
        return x + ":" + y + ":" + z;
    }

    public boolean equals(Object object) {
        boolean result = false;
        if ((object instanceof CoordLoc)) {
            CoordLoc loc = (CoordLoc) object;
            result = (loc.getX() == x) && (loc.getY() == y) && (loc.getZ() == z);
        }
        return result;
    }

    @Override
    public String toString() {
        return "CoordLoc[" + getLocationString() + "]";
    }

    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
