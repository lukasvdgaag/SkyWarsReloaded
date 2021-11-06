package net.gcnt.skywarsreloaded.utils;

public interface Coord {

    @Override
    String toString();

    int x();

    int y();

    int z();

    boolean equals(Coord o);
}
