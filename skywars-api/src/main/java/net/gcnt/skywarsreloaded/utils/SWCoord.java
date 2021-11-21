package net.gcnt.skywarsreloaded.utils;

public interface SWCoord {

    @Override
    String toString();

    int x();

    double xPrecise();

    int y();

    double yPrecise();

    int z();

    double zPrecise();

    SWCoord add(SWCoord coord);

    SWCoord add(double x, double y, double z);

    boolean equals(SWCoord o);

    SWCoord clone();

}
