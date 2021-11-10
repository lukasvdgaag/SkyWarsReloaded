package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import org.jetbrains.annotations.Nullable;

public class Coordinate implements Coord {

    private int x;
    private int y;
    private int z;

    /**
     * Get a new coord from the x, y, and z coordinates.
     *
     * @param x Location x point;
     * @param y Location y point;
     * @param z Location z point;
     */
    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Get a Coord loc from a formatted input string (format: x:y:z)
     *
     * @param input Formatted coord loc string.
     * @throws IndexOutOfBoundsException Thrown if there are more/less than 3 points found in this formatted input string.
     * @throws NumberFormatException     Thrown if one of the points seems to not be an integer.
     * @throws IllegalArgumentException  Thrown if the input string is null or empty.
     */
    public Coordinate(SkyWarsReloaded plugin, @Nullable String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("String cannot be converted to a Coord location. Input is empty/null.");
        }
        String[] arg0 = input.split(":");
        if (arg0.length != 3) {
            throw new IndexOutOfBoundsException("The coord input string \"" + input + "\" has an invalid amount of arguments.");
        } else if (!plugin.getUtils().isInt(arg0[0]) || !plugin.getUtils().isInt(arg0[1]) || !plugin.getUtils().isInt(arg0[2])) {
            throw new NumberFormatException("One of the coord points seems to not be an integer: " + input);
        }

        x = Integer.parseInt(arg0[0]);
        y = Integer.parseInt(arg0[1]);
        z = Integer.parseInt(arg0[2]);
    }

    @Override
    public String toString() {
        return x + ":" + y + ":" + z;
    }

    public boolean equals(Coord o) {
        return (o.toString().equals(this.toString()));
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public int z() {
        return z;
    }

    @Override
    public Coord add(Coord coord) {
        return add(coord.x(), coord.y(), coord.z());
    }

    @Override
    public Coord add(int x, int y, int z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public Coord clone() {
        return new Coordinate(x, y, z);
    }
}
