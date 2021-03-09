package com.walrusone.skywarsreloaded.game.cages;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;

public class SphereCage extends Cage {
    public SphereCage() {
        cageType = CageType.SPHERE;
        bottomCoordOffsets.add(new CoordLoc(0, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(0, 0, 1));

        bottomCoordOffsets.add(new CoordLoc(2, 1, 0));
        bottomCoordOffsets.add(new CoordLoc(2, 1, 1));
        bottomCoordOffsets.add(new CoordLoc(1, 1, 2));
        bottomCoordOffsets.add(new CoordLoc(0, 1, 2));
        bottomCoordOffsets.add(new CoordLoc(-1, 1, 0));
        bottomCoordOffsets.add(new CoordLoc(-1, 1, 1));
        bottomCoordOffsets.add(new CoordLoc(0, 1, -1));
        bottomCoordOffsets.add(new CoordLoc(1, 1, -1));

        middleCoordOffsets.add(new CoordLoc(3, 2, 0));
        middleCoordOffsets.add(new CoordLoc(3, 2, 1));
        middleCoordOffsets.add(new CoordLoc(2, 2, 2));
        middleCoordOffsets.add(new CoordLoc(1, 2, 3));
        middleCoordOffsets.add(new CoordLoc(0, 2, 3));
        middleCoordOffsets.add(new CoordLoc(-1, 2, 2));
        middleCoordOffsets.add(new CoordLoc(-2, 2, 1));
        middleCoordOffsets.add(new CoordLoc(-2, 2, 0));
        middleCoordOffsets.add(new CoordLoc(2, 2, -1));
        middleCoordOffsets.add(new CoordLoc(1, 2, -2));
        middleCoordOffsets.add(new CoordLoc(0, 2, -2));
        middleCoordOffsets.add(new CoordLoc(-1, 2, -1));

        middleCoordOffsets.add(new CoordLoc(4, 3, 0));
        middleCoordOffsets.add(new CoordLoc(4, 3, 1));
        middleCoordOffsets.add(new CoordLoc(3, 3, 2));
        middleCoordOffsets.add(new CoordLoc(2, 3, 3));
        middleCoordOffsets.add(new CoordLoc(1, 3, 4));
        middleCoordOffsets.add(new CoordLoc(0, 3, 4));
        middleCoordOffsets.add(new CoordLoc(-1, 3, 3));
        middleCoordOffsets.add(new CoordLoc(-2, 3, 2));
        middleCoordOffsets.add(new CoordLoc(-3, 3, 1));
        middleCoordOffsets.add(new CoordLoc(-3, 3, 0));
        middleCoordOffsets.add(new CoordLoc(3, 3, -1));
        middleCoordOffsets.add(new CoordLoc(2, 3, -2));
        middleCoordOffsets.add(new CoordLoc(1, 3, -3));
        middleCoordOffsets.add(new CoordLoc(0, 3, -3));
        middleCoordOffsets.add(new CoordLoc(-1, 3, -2));
        middleCoordOffsets.add(new CoordLoc(-2, 3, -1));

        topCoordOffsets.add(new CoordLoc(3, 4, 0));
        topCoordOffsets.add(new CoordLoc(3, 4, 1));
        topCoordOffsets.add(new CoordLoc(2, 4, 2));
        topCoordOffsets.add(new CoordLoc(1, 4, 3));
        topCoordOffsets.add(new CoordLoc(0, 4, 3));
        topCoordOffsets.add(new CoordLoc(-1, 4, 2));
        topCoordOffsets.add(new CoordLoc(-2, 4, 1));
        topCoordOffsets.add(new CoordLoc(-2, 4, 0));
        topCoordOffsets.add(new CoordLoc(2, 4, -1));
        topCoordOffsets.add(new CoordLoc(1, 4, -2));
        topCoordOffsets.add(new CoordLoc(0, 4, -2));
        topCoordOffsets.add(new CoordLoc(-1, 4, -1));

        topCoordOffsets.add(new CoordLoc(2, 5, 0));
        topCoordOffsets.add(new CoordLoc(2, 5, 1));
        topCoordOffsets.add(new CoordLoc(1, 5, 2));
        topCoordOffsets.add(new CoordLoc(0, 5, 2));
        topCoordOffsets.add(new CoordLoc(-1, 5, 0));
        topCoordOffsets.add(new CoordLoc(-1, 5, 1));
        topCoordOffsets.add(new CoordLoc(0, 5, -1));
        topCoordOffsets.add(new CoordLoc(1, 5, -1));

        topCoordOffsets.add(new CoordLoc(0, 6, 0));
        topCoordOffsets.add(new CoordLoc(1, 6, 0));
        topCoordOffsets.add(new CoordLoc(1, 6, 1));
        topCoordOffsets.add(new CoordLoc(0, 6, 1));
    }
}
