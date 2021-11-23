package net.gcnt.skywarsreloaded.utils;

import net.gcnt.skywarsreloaded.wrapper.world.SWWorld;

public interface SWCoord {

    @Override
    String toString();

    SWWorld world();

    int x();

    double xPrecise();

    int y();

    double yPrecise();

    int z();

    double zPrecise();

    float pitch();

    float yaw();

    SWCoord add(SWCoord coord);

    SWCoord add(double x, double y, double z);

    boolean equals(SWCoord o);

    SWCoord asBlock();

    SWCoord clone();

}
