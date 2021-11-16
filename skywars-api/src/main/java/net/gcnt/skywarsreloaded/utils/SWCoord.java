package net.gcnt.skywarsreloaded.utils;

public interface SWCoord {

    @Override
    String toString();

    int x();

    int y();

    int z();

    SWCoord add(SWCoord coord);

    SWCoord add(int x, int y, int z);

    boolean equals(SWCoord o);

    SWCoord clone();

}
