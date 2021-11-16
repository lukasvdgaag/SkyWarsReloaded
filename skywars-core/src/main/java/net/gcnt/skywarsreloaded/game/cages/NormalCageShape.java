package net.gcnt.skywarsreloaded.game.cages;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.CoreSWCoord;

import java.util.ArrayList;
import java.util.List;

public enum NormalCageShape {

    DEFAULT,
    CUBE,
    DOME,
    PYRAMID,
    SPHERE;

    private final List<SWCoord> locations;

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
                locations.add(new CoreSWCoord(0, 0, 0));
                locations.add(new CoreSWCoord(0, 1, 1));
                locations.add(new CoreSWCoord(0, 1, -1));
                locations.add(new CoreSWCoord(1, 1, 0));
                locations.add(new CoreSWCoord(-1, 1, 0));
                locations.add(new CoreSWCoord(0, 2, 1));
                locations.add(new CoreSWCoord(0, 2, -1));
                locations.add(new CoreSWCoord(1, 2, 0));
                locations.add(new CoreSWCoord(-1, 2, 0));
                locations.add(new CoreSWCoord(0, 3, 1));
                locations.add(new CoreSWCoord(0, 3, -1));
                locations.add(new CoreSWCoord(1, 3, 0));
                locations.add(new CoreSWCoord(-1, 3, 0));
                locations.add(new CoreSWCoord(0, 4, 0));
            }
            case CUBE -> {
                locations.add(new CoreSWCoord(0, 0, 0));
                locations.add(new CoreSWCoord(0, 0, 1));
                locations.add(new CoreSWCoord(0, 0, -1));
                locations.add(new CoreSWCoord(1, 0, 0));
                locations.add(new CoreSWCoord(1, 0, 1));
                locations.add(new CoreSWCoord(1, 0, -1));
                locations.add(new CoreSWCoord(-1, 0, 0));
                locations.add(new CoreSWCoord(-1, 0, 1));
                locations.add(new CoreSWCoord(-1, 0, -1));
                for (int i = 1; i < 4; i++) {
                    locations.add(new CoreSWCoord(2, i, -1));
                    locations.add(new CoreSWCoord(2, i, 0));
                    locations.add(new CoreSWCoord(2, i, 1));
                    locations.add(new CoreSWCoord(-2, i, -1));
                    locations.add(new CoreSWCoord(-2, i, 0));
                    locations.add(new CoreSWCoord(-2, i, 1));
                    locations.add(new CoreSWCoord(-1, i, 2));
                    locations.add(new CoreSWCoord(0, i, 2));
                    locations.add(new CoreSWCoord(1, i, 2));
                    locations.add(new CoreSWCoord(-1, i, -2));
                    locations.add(new CoreSWCoord(0, i, -2));
                    locations.add(new CoreSWCoord(1, i, -2));
                }
                locations.add(new CoreSWCoord(0, 4, 0));
                locations.add(new CoreSWCoord(0, 4, 1));
                locations.add(new CoreSWCoord(0, 4, -1));
                locations.add(new CoreSWCoord(1, 4, 0));
                locations.add(new CoreSWCoord(1, 4, 1));
                locations.add(new CoreSWCoord(1, 4, -1));
                locations.add(new CoreSWCoord(-1, 4, 0));
                locations.add(new CoreSWCoord(-1, 4, 1));
                locations.add(new CoreSWCoord(-1, 4, -1));
            }
            case DOME -> {
                locations.add(new CoreSWCoord(0, 0, 0));
                locations.add(new CoreSWCoord(4, 0, 0));
                locations.add(new CoreSWCoord(3, 0, 0));
                locations.add(new CoreSWCoord(2, 0, 0));
                locations.add(new CoreSWCoord(1, 0, 0));
                locations.add(new CoreSWCoord(3, 0, 1));
                locations.add(new CoreSWCoord(2, 0, 1));
                locations.add(new CoreSWCoord(1, 0, 1));
                locations.add(new CoreSWCoord(2, 0, 2));
                locations.add(new CoreSWCoord(1, 0, 2));
                locations.add(new CoreSWCoord(1, 0, 3));
                locations.add(new CoreSWCoord(-1, 0, 0));
                locations.add(new CoreSWCoord(-2, 0, 0));
                locations.add(new CoreSWCoord(0, 0, 1));
                locations.add(new CoreSWCoord(0, 0, 2));
                locations.add(new CoreSWCoord(0, 0, 3));
                locations.add(new CoreSWCoord(-1, 0, 1));
                locations.add(new CoreSWCoord(-1, 0, 2));
                locations.add(new CoreSWCoord(-2, 0, 1));
                locations.add(new CoreSWCoord(0, 0, -1));
                locations.add(new CoreSWCoord(0, 0, -2));
                locations.add(new CoreSWCoord(1, 0, -1));
                locations.add(new CoreSWCoord(1, 0, -2));
                locations.add(new CoreSWCoord(2, 0, -1));
                locations.add(new CoreSWCoord(-1, 0, -1));
                locations.add(new CoreSWCoord(4, 0, 1));
                locations.add(new CoreSWCoord(3, 0, 2));
                locations.add(new CoreSWCoord(2, 0, 3));
                locations.add(new CoreSWCoord(1, 0, 4));
                locations.add(new CoreSWCoord(0, 0, 4));
                locations.add(new CoreSWCoord(-1, 0, 3));
                locations.add(new CoreSWCoord(-2, 0, 2));
                locations.add(new CoreSWCoord(-3, 0, 1));
                locations.add(new CoreSWCoord(-3, 0, 0));
                locations.add(new CoreSWCoord(3, 0, -1));
                locations.add(new CoreSWCoord(2, 0, -2));
                locations.add(new CoreSWCoord(1, 0, -3));
                locations.add(new CoreSWCoord(0, 0, -3));
                locations.add(new CoreSWCoord(-1, 0, -2));
                locations.add(new CoreSWCoord(-2, 0, -1));

                locations.add(new CoreSWCoord(4, 1, 0));
                locations.add(new CoreSWCoord(4, 1, 1));
                locations.add(new CoreSWCoord(3, 1, 2));
                locations.add(new CoreSWCoord(2, 1, 3));
                locations.add(new CoreSWCoord(1, 1, 4));
                locations.add(new CoreSWCoord(0, 1, 4));
                locations.add(new CoreSWCoord(-1, 1, 3));
                locations.add(new CoreSWCoord(-2, 1, 2));
                locations.add(new CoreSWCoord(-3, 1, 1));
                locations.add(new CoreSWCoord(-3, 1, 0));
                locations.add(new CoreSWCoord(3, 1, -1));
                locations.add(new CoreSWCoord(2, 1, -2));
                locations.add(new CoreSWCoord(1, 1, -3));
                locations.add(new CoreSWCoord(0, 1, -3));
                locations.add(new CoreSWCoord(-1, 1, -2));
                locations.add(new CoreSWCoord(-2, 1, -1));

                locations.add(new CoreSWCoord(3, 2, 0));
                locations.add(new CoreSWCoord(3, 2, 1));
                locations.add(new CoreSWCoord(2, 2, 2));
                locations.add(new CoreSWCoord(1, 2, 3));
                locations.add(new CoreSWCoord(0, 2, 3));
                locations.add(new CoreSWCoord(-1, 2, 2));
                locations.add(new CoreSWCoord(-2, 2, 1));
                locations.add(new CoreSWCoord(-2, 2, 0));
                locations.add(new CoreSWCoord(2, 2, -1));
                locations.add(new CoreSWCoord(1, 2, -2));
                locations.add(new CoreSWCoord(0, 2, -2));
                locations.add(new CoreSWCoord(-1, 2, -1));

                locations.add(new CoreSWCoord(2, 3, 0));
                locations.add(new CoreSWCoord(2, 3, 1));
                locations.add(new CoreSWCoord(1, 3, 2));
                locations.add(new CoreSWCoord(0, 3, 2));
                locations.add(new CoreSWCoord(-1, 3, 0));
                locations.add(new CoreSWCoord(-1, 3, 1));
                locations.add(new CoreSWCoord(0, 3, -1));
                locations.add(new CoreSWCoord(1, 3, -1));

                locations.add(new CoreSWCoord(0, 4, 0));
                locations.add(new CoreSWCoord(1, 4, 0));
                locations.add(new CoreSWCoord(1, 4, 1));
                locations.add(new CoreSWCoord(0, 4, 1));
            }
            case PYRAMID -> {
                locations.add(new CoreSWCoord(0, 0, 0));
                locations.add(new CoreSWCoord(1, 0, 0));
                locations.add(new CoreSWCoord(1, 0, 1));
                locations.add(new CoreSWCoord(1, 0, -1));
                locations.add(new CoreSWCoord(1, 0, 2));
                locations.add(new CoreSWCoord(1, 0, -2));
                locations.add(new CoreSWCoord(2, 0, 0));
                locations.add(new CoreSWCoord(2, 0, 1));
                locations.add(new CoreSWCoord(2, 0, -1));
                locations.add(new CoreSWCoord(3, 0, 0));
                locations.add(new CoreSWCoord(-1, 0, 0));
                locations.add(new CoreSWCoord(-1, 0, 1));
                locations.add(new CoreSWCoord(-1, 0, -1));
                locations.add(new CoreSWCoord(-1, 0, 2));
                locations.add(new CoreSWCoord(-1, 0, -2));
                locations.add(new CoreSWCoord(-2, 0, 0));
                locations.add(new CoreSWCoord(-2, 0, 1));
                locations.add(new CoreSWCoord(-2, 0, -1));
                locations.add(new CoreSWCoord(-3, 0, 0));
                locations.add(new CoreSWCoord(0, 0, 1));
                locations.add(new CoreSWCoord(0, 0, 2));
                locations.add(new CoreSWCoord(0, 0, 3));
                locations.add(new CoreSWCoord(0, 0, -1));
                locations.add(new CoreSWCoord(0, 0, -2));
                locations.add(new CoreSWCoord(0, 0, -3));

                locations.add(new CoreSWCoord(1, 1, 2));
                locations.add(new CoreSWCoord(1, 1, -2));
                locations.add(new CoreSWCoord(2, 1, 1));
                locations.add(new CoreSWCoord(2, 1, -1));
                locations.add(new CoreSWCoord(3, 1, 0));
                locations.add(new CoreSWCoord(-1, 1, 2));
                locations.add(new CoreSWCoord(-1, 1, -2));
                locations.add(new CoreSWCoord(-2, 1, 1));
                locations.add(new CoreSWCoord(-2, 1, -1));
                locations.add(new CoreSWCoord(-3, 1, 0));
                locations.add(new CoreSWCoord(0, 1, 3));
                locations.add(new CoreSWCoord(0, 1, -3));

                locations.add(new CoreSWCoord(1, 2, 2));
                locations.add(new CoreSWCoord(1, 2, -2));
                locations.add(new CoreSWCoord(2, 2, 1));
                locations.add(new CoreSWCoord(2, 2, -1));
                locations.add(new CoreSWCoord(3, 2, 0));
                locations.add(new CoreSWCoord(-1, 2, 2));
                locations.add(new CoreSWCoord(-1, 2, -2));
                locations.add(new CoreSWCoord(-2, 2, 1));
                locations.add(new CoreSWCoord(-2, 2, -1));
                locations.add(new CoreSWCoord(-3, 2, 0));
                locations.add(new CoreSWCoord(0, 2, 3));
                locations.add(new CoreSWCoord(0, 2, -3));

                locations.add(new CoreSWCoord(2, 3, 0));
                locations.add(new CoreSWCoord(1, 3, 1));
                locations.add(new CoreSWCoord(0, 3, 2));
                locations.add(new CoreSWCoord(-1, 3, 1));
                locations.add(new CoreSWCoord(-2, 3, 0));
                locations.add(new CoreSWCoord(1, 3, -1));
                locations.add(new CoreSWCoord(0, 3, -2));
                locations.add(new CoreSWCoord(-1, 3, -1));

                locations.add(new CoreSWCoord(1, 4, 0));
                locations.add(new CoreSWCoord(-1, 4, 0));
                locations.add(new CoreSWCoord(0, 4, 1));
                locations.add(new CoreSWCoord(0, 4, -1));
                locations.add(new CoreSWCoord(0, 5, 0));
            }
            case SPHERE -> {
                locations.add(new CoreSWCoord(0, 0, 0));
                locations.add(new CoreSWCoord(1, 0, 0));
                locations.add(new CoreSWCoord(1, 0, 1));
                locations.add(new CoreSWCoord(0, 0, 1));

                locations.add(new CoreSWCoord(2, 1, 0));
                locations.add(new CoreSWCoord(2, 1, 1));
                locations.add(new CoreSWCoord(1, 1, 2));
                locations.add(new CoreSWCoord(0, 1, 2));
                locations.add(new CoreSWCoord(-1, 1, 0));
                locations.add(new CoreSWCoord(-1, 1, 1));
                locations.add(new CoreSWCoord(0, 1, -1));
                locations.add(new CoreSWCoord(1, 1, -1));

                locations.add(new CoreSWCoord(3, 2, 0));
                locations.add(new CoreSWCoord(3, 2, 1));
                locations.add(new CoreSWCoord(2, 2, 2));
                locations.add(new CoreSWCoord(1, 2, 3));
                locations.add(new CoreSWCoord(0, 2, 3));
                locations.add(new CoreSWCoord(-1, 2, 2));
                locations.add(new CoreSWCoord(-2, 2, 1));
                locations.add(new CoreSWCoord(-2, 2, 0));
                locations.add(new CoreSWCoord(2, 2, -1));
                locations.add(new CoreSWCoord(1, 2, -2));
                locations.add(new CoreSWCoord(0, 2, -2));
                locations.add(new CoreSWCoord(-1, 2, -1));

                locations.add(new CoreSWCoord(4, 3, 0));
                locations.add(new CoreSWCoord(4, 3, 1));
                locations.add(new CoreSWCoord(3, 3, 2));
                locations.add(new CoreSWCoord(2, 3, 3));
                locations.add(new CoreSWCoord(1, 3, 4));
                locations.add(new CoreSWCoord(0, 3, 4));
                locations.add(new CoreSWCoord(-1, 3, 3));
                locations.add(new CoreSWCoord(-2, 3, 2));
                locations.add(new CoreSWCoord(-3, 3, 1));
                locations.add(new CoreSWCoord(-3, 3, 0));
                locations.add(new CoreSWCoord(3, 3, -1));
                locations.add(new CoreSWCoord(2, 3, -2));
                locations.add(new CoreSWCoord(1, 3, -3));
                locations.add(new CoreSWCoord(0, 3, -3));
                locations.add(new CoreSWCoord(-1, 3, -2));
                locations.add(new CoreSWCoord(-2, 3, -1));

                locations.add(new CoreSWCoord(3, 4, 0));
                locations.add(new CoreSWCoord(3, 4, 1));
                locations.add(new CoreSWCoord(2, 4, 2));
                locations.add(new CoreSWCoord(1, 4, 3));
                locations.add(new CoreSWCoord(0, 4, 3));
                locations.add(new CoreSWCoord(-1, 4, 2));
                locations.add(new CoreSWCoord(-2, 4, 1));
                locations.add(new CoreSWCoord(-2, 4, 0));
                locations.add(new CoreSWCoord(2, 4, -1));
                locations.add(new CoreSWCoord(1, 4, -2));
                locations.add(new CoreSWCoord(0, 4, -2));
                locations.add(new CoreSWCoord(-1, 4, -1));

                locations.add(new CoreSWCoord(2, 5, 0));
                locations.add(new CoreSWCoord(2, 5, 1));
                locations.add(new CoreSWCoord(1, 5, 2));
                locations.add(new CoreSWCoord(0, 5, 2));
                locations.add(new CoreSWCoord(-1, 5, 0));
                locations.add(new CoreSWCoord(-1, 5, 1));
                locations.add(new CoreSWCoord(0, 5, -1));
                locations.add(new CoreSWCoord(1, 5, -1));

                locations.add(new CoreSWCoord(0, 6, 0));
                locations.add(new CoreSWCoord(1, 6, 0));
                locations.add(new CoreSWCoord(1, 6, 1));
                locations.add(new CoreSWCoord(0, 6, 1));
            }
        }
    }

    public List<SWCoord> getLocations() {
        return locations;
    }

}
