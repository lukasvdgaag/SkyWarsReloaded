package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;

public enum NormalCageShape {

    DEFAULT,
    CUBE,
    DOME,
    PYRAMID,
    SPHERE;

    private final List<Coord> locations;

    NormalCageShape() {
        locations = new ArrayList<>();
        loadLocations(this);
    }

    public static NormalCageShape fromString(String input) {
        for (NormalCageShape s : values()) {
            if (input.equalsIgnoreCase(s.name())) return s;
        }
        return null;
    }

    private void loadLocations(NormalCageShape shape) {
        switch (shape) {
            case DEFAULT -> {
                locations.add(new Coordinate(0, 0, 0));
                locations.add(new Coordinate(0, 1, 1));
                locations.add(new Coordinate(0, 1, -1));
                locations.add(new Coordinate(1, 1, 0));
                locations.add(new Coordinate(-1, 1, 0));
                locations.add(new Coordinate(0, 2, 1));
                locations.add(new Coordinate(0, 2, -1));
                locations.add(new Coordinate(1, 2, 0));
                locations.add(new Coordinate(-1, 2, 0));
                locations.add(new Coordinate(0, 3, 1));
                locations.add(new Coordinate(0, 3, -1));
                locations.add(new Coordinate(1, 3, 0));
                locations.add(new Coordinate(-1, 3, 0));
                locations.add(new Coordinate(0, 4, 0));
            }
            case CUBE -> {
                locations.add(new Coordinate(0, 0, 0));
                locations.add(new Coordinate(0, 0, 1));
                locations.add(new Coordinate(0, 0, -1));
                locations.add(new Coordinate(1, 0, 0));
                locations.add(new Coordinate(1, 0, 1));
                locations.add(new Coordinate(1, 0, -1));
                locations.add(new Coordinate(-1, 0, 0));
                locations.add(new Coordinate(-1, 0, 1));
                locations.add(new Coordinate(-1, 0, -1));
                for (int i = 1; i < 4; i++) {
                    locations.add(new Coordinate(2, i, -1));
                    locations.add(new Coordinate(2, i, 0));
                    locations.add(new Coordinate(2, i, 1));
                    locations.add(new Coordinate(-2, i, -1));
                    locations.add(new Coordinate(-2, i, 0));
                    locations.add(new Coordinate(-2, i, 1));
                    locations.add(new Coordinate(-1, i, 2));
                    locations.add(new Coordinate(0, i, 2));
                    locations.add(new Coordinate(1, i, 2));
                    locations.add(new Coordinate(-1, i, -2));
                    locations.add(new Coordinate(0, i, -2));
                    locations.add(new Coordinate(1, i, -2));
                }
                locations.add(new Coordinate(0, 4, 0));
                locations.add(new Coordinate(0, 4, 1));
                locations.add(new Coordinate(0, 4, -1));
                locations.add(new Coordinate(1, 4, 0));
                locations.add(new Coordinate(1, 4, 1));
                locations.add(new Coordinate(1, 4, -1));
                locations.add(new Coordinate(-1, 4, 0));
                locations.add(new Coordinate(-1, 4, 1));
                locations.add(new Coordinate(-1, 4, -1));
            }
            case DOME -> {
                locations.add(new Coordinate(0, 0, 0));
                locations.add(new Coordinate(4, 0, 0));
                locations.add(new Coordinate(3, 0, 0));
                locations.add(new Coordinate(2, 0, 0));
                locations.add(new Coordinate(1, 0, 0));
                locations.add(new Coordinate(3, 0, 1));
                locations.add(new Coordinate(2, 0, 1));
                locations.add(new Coordinate(1, 0, 1));
                locations.add(new Coordinate(2, 0, 2));
                locations.add(new Coordinate(1, 0, 2));
                locations.add(new Coordinate(1, 0, 3));
                locations.add(new Coordinate(-1, 0, 0));
                locations.add(new Coordinate(-2, 0, 0));
                locations.add(new Coordinate(0, 0, 1));
                locations.add(new Coordinate(0, 0, 2));
                locations.add(new Coordinate(0, 0, 3));
                locations.add(new Coordinate(-1, 0, 1));
                locations.add(new Coordinate(-1, 0, 2));
                locations.add(new Coordinate(-2, 0, 1));
                locations.add(new Coordinate(0, 0, -1));
                locations.add(new Coordinate(0, 0, -2));
                locations.add(new Coordinate(1, 0, -1));
                locations.add(new Coordinate(1, 0, -2));
                locations.add(new Coordinate(2, 0, -1));
                locations.add(new Coordinate(-1, 0, -1));
                locations.add(new Coordinate(4, 0, 1));
                locations.add(new Coordinate(3, 0, 2));
                locations.add(new Coordinate(2, 0, 3));
                locations.add(new Coordinate(1, 0, 4));
                locations.add(new Coordinate(0, 0, 4));
                locations.add(new Coordinate(-1, 0, 3));
                locations.add(new Coordinate(-2, 0, 2));
                locations.add(new Coordinate(-3, 0, 1));
                locations.add(new Coordinate(-3, 0, 0));
                locations.add(new Coordinate(3, 0, -1));
                locations.add(new Coordinate(2, 0, -2));
                locations.add(new Coordinate(1, 0, -3));
                locations.add(new Coordinate(0, 0, -3));
                locations.add(new Coordinate(-1, 0, -2));
                locations.add(new Coordinate(-2, 0, -1));

                locations.add(new Coordinate(4, 1, 0));
                locations.add(new Coordinate(4, 1, 1));
                locations.add(new Coordinate(3, 1, 2));
                locations.add(new Coordinate(2, 1, 3));
                locations.add(new Coordinate(1, 1, 4));
                locations.add(new Coordinate(0, 1, 4));
                locations.add(new Coordinate(-1, 1, 3));
                locations.add(new Coordinate(-2, 1, 2));
                locations.add(new Coordinate(-3, 1, 1));
                locations.add(new Coordinate(-3, 1, 0));
                locations.add(new Coordinate(3, 1, -1));
                locations.add(new Coordinate(2, 1, -2));
                locations.add(new Coordinate(1, 1, -3));
                locations.add(new Coordinate(0, 1, -3));
                locations.add(new Coordinate(-1, 1, -2));
                locations.add(new Coordinate(-2, 1, -1));

                locations.add(new Coordinate(3, 2, 0));
                locations.add(new Coordinate(3, 2, 1));
                locations.add(new Coordinate(2, 2, 2));
                locations.add(new Coordinate(1, 2, 3));
                locations.add(new Coordinate(0, 2, 3));
                locations.add(new Coordinate(-1, 2, 2));
                locations.add(new Coordinate(-2, 2, 1));
                locations.add(new Coordinate(-2, 2, 0));
                locations.add(new Coordinate(2, 2, -1));
                locations.add(new Coordinate(1, 2, -2));
                locations.add(new Coordinate(0, 2, -2));
                locations.add(new Coordinate(-1, 2, -1));

                locations.add(new Coordinate(2, 3, 0));
                locations.add(new Coordinate(2, 3, 1));
                locations.add(new Coordinate(1, 3, 2));
                locations.add(new Coordinate(0, 3, 2));
                locations.add(new Coordinate(-1, 3, 0));
                locations.add(new Coordinate(-1, 3, 1));
                locations.add(new Coordinate(0, 3, -1));
                locations.add(new Coordinate(1, 3, -1));

                locations.add(new Coordinate(0, 4, 0));
                locations.add(new Coordinate(1, 4, 0));
                locations.add(new Coordinate(1, 4, 1));
                locations.add(new Coordinate(0, 4, 1));
            }
            case PYRAMID -> {
                locations.add(new Coordinate(0, 0, 0));
                locations.add(new Coordinate(1, 0, 0));
                locations.add(new Coordinate(1, 0, 1));
                locations.add(new Coordinate(1, 0, -1));
                locations.add(new Coordinate(1, 0, 2));
                locations.add(new Coordinate(1, 0, -2));
                locations.add(new Coordinate(2, 0, 0));
                locations.add(new Coordinate(2, 0, 1));
                locations.add(new Coordinate(2, 0, -1));
                locations.add(new Coordinate(3, 0, 0));
                locations.add(new Coordinate(-1, 0, 0));
                locations.add(new Coordinate(-1, 0, 1));
                locations.add(new Coordinate(-1, 0, -1));
                locations.add(new Coordinate(-1, 0, 2));
                locations.add(new Coordinate(-1, 0, -2));
                locations.add(new Coordinate(-2, 0, 0));
                locations.add(new Coordinate(-2, 0, 1));
                locations.add(new Coordinate(-2, 0, -1));
                locations.add(new Coordinate(-3, 0, 0));
                locations.add(new Coordinate(0, 0, 1));
                locations.add(new Coordinate(0, 0, 2));
                locations.add(new Coordinate(0, 0, 3));
                locations.add(new Coordinate(0, 0, -1));
                locations.add(new Coordinate(0, 0, -2));
                locations.add(new Coordinate(0, 0, -3));

                locations.add(new Coordinate(1, 1, 2));
                locations.add(new Coordinate(1, 1, -2));
                locations.add(new Coordinate(2, 1, 1));
                locations.add(new Coordinate(2, 1, -1));
                locations.add(new Coordinate(3, 1, 0));
                locations.add(new Coordinate(-1, 1, 2));
                locations.add(new Coordinate(-1, 1, -2));
                locations.add(new Coordinate(-2, 1, 1));
                locations.add(new Coordinate(-2, 1, -1));
                locations.add(new Coordinate(-3, 1, 0));
                locations.add(new Coordinate(0, 1, 3));
                locations.add(new Coordinate(0, 1, -3));

                locations.add(new Coordinate(1, 2, 2));
                locations.add(new Coordinate(1, 2, -2));
                locations.add(new Coordinate(2, 2, 1));
                locations.add(new Coordinate(2, 2, -1));
                locations.add(new Coordinate(3, 2, 0));
                locations.add(new Coordinate(-1, 2, 2));
                locations.add(new Coordinate(-1, 2, -2));
                locations.add(new Coordinate(-2, 2, 1));
                locations.add(new Coordinate(-2, 2, -1));
                locations.add(new Coordinate(-3, 2, 0));
                locations.add(new Coordinate(0, 2, 3));
                locations.add(new Coordinate(0, 2, -3));

                locations.add(new Coordinate(2, 3, 0));
                locations.add(new Coordinate(1, 3, 1));
                locations.add(new Coordinate(0, 3, 2));
                locations.add(new Coordinate(-1, 3, 1));
                locations.add(new Coordinate(-2, 3, 0));
                locations.add(new Coordinate(1, 3, -1));
                locations.add(new Coordinate(0, 3, -2));
                locations.add(new Coordinate(-1, 3, -1));

                locations.add(new Coordinate(1, 4, 0));
                locations.add(new Coordinate(-1, 4, 0));
                locations.add(new Coordinate(0, 4, 1));
                locations.add(new Coordinate(0, 4, -1));
                locations.add(new Coordinate(0, 5, 0));
            }
            case SPHERE -> {
                locations.add(new Coordinate(0, 0, 0));
                locations.add(new Coordinate(1, 0, 0));
                locations.add(new Coordinate(1, 0, 1));
                locations.add(new Coordinate(0, 0, 1));

                locations.add(new Coordinate(2, 1, 0));
                locations.add(new Coordinate(2, 1, 1));
                locations.add(new Coordinate(1, 1, 2));
                locations.add(new Coordinate(0, 1, 2));
                locations.add(new Coordinate(-1, 1, 0));
                locations.add(new Coordinate(-1, 1, 1));
                locations.add(new Coordinate(0, 1, -1));
                locations.add(new Coordinate(1, 1, -1));

                locations.add(new Coordinate(3, 2, 0));
                locations.add(new Coordinate(3, 2, 1));
                locations.add(new Coordinate(2, 2, 2));
                locations.add(new Coordinate(1, 2, 3));
                locations.add(new Coordinate(0, 2, 3));
                locations.add(new Coordinate(-1, 2, 2));
                locations.add(new Coordinate(-2, 2, 1));
                locations.add(new Coordinate(-2, 2, 0));
                locations.add(new Coordinate(2, 2, -1));
                locations.add(new Coordinate(1, 2, -2));
                locations.add(new Coordinate(0, 2, -2));
                locations.add(new Coordinate(-1, 2, -1));

                locations.add(new Coordinate(4, 3, 0));
                locations.add(new Coordinate(4, 3, 1));
                locations.add(new Coordinate(3, 3, 2));
                locations.add(new Coordinate(2, 3, 3));
                locations.add(new Coordinate(1, 3, 4));
                locations.add(new Coordinate(0, 3, 4));
                locations.add(new Coordinate(-1, 3, 3));
                locations.add(new Coordinate(-2, 3, 2));
                locations.add(new Coordinate(-3, 3, 1));
                locations.add(new Coordinate(-3, 3, 0));
                locations.add(new Coordinate(3, 3, -1));
                locations.add(new Coordinate(2, 3, -2));
                locations.add(new Coordinate(1, 3, -3));
                locations.add(new Coordinate(0, 3, -3));
                locations.add(new Coordinate(-1, 3, -2));
                locations.add(new Coordinate(-2, 3, -1));

                locations.add(new Coordinate(3, 4, 0));
                locations.add(new Coordinate(3, 4, 1));
                locations.add(new Coordinate(2, 4, 2));
                locations.add(new Coordinate(1, 4, 3));
                locations.add(new Coordinate(0, 4, 3));
                locations.add(new Coordinate(-1, 4, 2));
                locations.add(new Coordinate(-2, 4, 1));
                locations.add(new Coordinate(-2, 4, 0));
                locations.add(new Coordinate(2, 4, -1));
                locations.add(new Coordinate(1, 4, -2));
                locations.add(new Coordinate(0, 4, -2));
                locations.add(new Coordinate(-1, 4, -1));

                locations.add(new Coordinate(2, 5, 0));
                locations.add(new Coordinate(2, 5, 1));
                locations.add(new Coordinate(1, 5, 2));
                locations.add(new Coordinate(0, 5, 2));
                locations.add(new Coordinate(-1, 5, 0));
                locations.add(new Coordinate(-1, 5, 1));
                locations.add(new Coordinate(0, 5, -1));
                locations.add(new Coordinate(1, 5, -1));

                locations.add(new Coordinate(0, 6, 0));
                locations.add(new Coordinate(1, 6, 0));
                locations.add(new Coordinate(1, 6, 1));
                locations.add(new Coordinate(0, 6, 1));
            }
        }
    }

    public List<Coord> getLocations() {
        return locations;
    }

}
