package com.walrusone.skywarsreloaded.menus.gameoptions.objects;

import java.util.Objects;

public class CoordLoc {
    private int x;
    private int z;
    private int y;

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

    public String getLocation() {
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

    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
