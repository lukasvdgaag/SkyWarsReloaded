package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.SkyWarsReloaded;
import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;
import org.jetbrains.annotations.Nullable;

public class CoreSWCoord implements SWCoord {

    private String worldName;
    private SWWorld world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public CoreSWCoord(double x, double y, double z) {
        this(null, x, y, z, 0, 0);
    }

    public CoreSWCoord(SWWorld world, double x, double y, double z) {
        this(world, x, y, z, 0, 0);
    }

    public CoreSWCoord(double x, double y, double z, float yaw, float pitch) {
        this(null, x, y, z, yaw, pitch);
    }

    public CoreSWCoord(SWWorld world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        if (world != null) this.worldName = world.getName();
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Get a Coord loc from a formatted input string (format: x:y:z)
     *
     * @param input Formatted coord loc string.
     * @throws IndexOutOfBoundsException Thrown if there are more/less than 3 points found in this formatted input string.
     * @throws NumberFormatException     Thrown if one of the points seems to not be an integer.
     * @throws IllegalArgumentException  Thrown if the input string is null or empty.
     */
    public CoreSWCoord(SkyWarsReloaded plugin, @Nullable String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("String cannot be converted to a Coord location. Input is empty/null.");
        }
        String[] arg0 = input.split(":");
        int lenAdd = 0;
        final int length = arg0.length;
        if (length < 3 || length > 6) {
            throw new IndexOutOfBoundsException("The coord input string \"" + input + "\" has an invalid amount of arguments.");
        }
        if (length == 4 || length == 6) lenAdd = 1;

        if (!plugin.getUtils().isDouble(arg0[lenAdd]) || !plugin.getUtils().isDouble(arg0[1 + lenAdd]) || !plugin.getUtils().isDouble(arg0[2 + lenAdd]) || (lenAdd == 1 && (!plugin.getUtils().isFloat(arg0[3 + lenAdd]) || !plugin.getUtils().isFloat(arg0[4 + lenAdd])))) {
            throw new NumberFormatException("One of the coord points seems to not be a number: " + input);
        }

        // todo add support for coord strings with pitch and yaws (+ world names).

        x = Double.parseDouble(arg0[lenAdd]);
        y = Double.parseDouble(arg0[1 + lenAdd]);
        z = Double.parseDouble(arg0[2 + lenAdd]);
        if (lenAdd == 1) { // aka checks if length == 4 || length == 6 (contains world name)
            this.worldName = arg0[0];
            this.world = plugin.getUtils().getSWWorld(arg0[0]);
            if (this.world == null) {
                plugin.getLogger().warn(String.format("The world \"%s\" is null for SWCoord \"%s\". This may cause problems in the futures when it gets referred to.", arg0[0], input));
            }
        }
        if (length == 5 || length == 6) {
            this.yaw = Float.parseFloat(arg0[length - 2]);
            this.pitch = Float.parseFloat(arg0[length - 1]);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (worldName != null) sb.append(worldName).append(":");
        sb.append(xPrecise()).append(":").append(yPrecise()).append(":").append(zPrecise());
        if (yaw != 0 || pitch != 0) sb.append(yaw).append(":").append(pitch);
        return sb.toString();
    }

    public boolean equals(SWCoord o) {
        if (this.world == null || o.world() == null) {
            return this.x == o.xPrecise() && this.y == o.yPrecise() && this.z == o.zPrecise() && this.pitch == o.pitch() && this.yaw == o.yaw();
        }
        return (o.toString().equals(this.toString()));
    }

    public SWWorld world() {
        return world;
    }

    private int floor(double num) {
        int floor = (int) num;
        return (double) floor == num ? floor : floor - (int) (Double.doubleToRawLongBits(num) >>> 63);
    }

    @Override
    public SWCoord asBlock() {
        return new CoreSWCoord(floor(xPrecise()), floor(yPrecise()), floor(zPrecise()));
    }

    @Override
    public int x() {
        return (int) x;
    }

    @Override
    public double xPrecise() {
        return x;
    }

    @Override
    public int y() {
        return (int) y;
    }

    @Override
    public double yPrecise() {
        return y;
    }

    @Override
    public int z() {
        return (int) z;
    }

    @Override
    public double zPrecise() {
        return z;
    }

    @Override
    public float pitch() {
        return pitch;
    }

    @Override
    public float yaw() {
        return yaw;
    }

    @Override
    public SWCoord add(SWCoord coord) {
        return add(coord.x(), coord.y(), coord.z());
    }

    @Override
    public SWCoord add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public SWCoord clone() {
        return new CoreSWCoord(x, y, z);
    }
}
