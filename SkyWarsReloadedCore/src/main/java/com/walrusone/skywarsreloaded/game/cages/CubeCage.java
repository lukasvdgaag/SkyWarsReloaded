package com.walrusone.skywarsreloaded.game.cages;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;

public class CubeCage extends Cage {
    public CubeCage() {
        cageType = CageType.CUBE;
        bottomCoordOffsets.add(new CoordLoc(0, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(0, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(0, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(1, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, -1));
        for (int i = 1; i < 4; i++) {
            middleCoordOffsets.add(new CoordLoc(2, i, -1));
            middleCoordOffsets.add(new CoordLoc(2, i, 0));
            middleCoordOffsets.add(new CoordLoc(2, i, 1));
            middleCoordOffsets.add(new CoordLoc(-2, i, -1));
            middleCoordOffsets.add(new CoordLoc(-2, i, 0));
            middleCoordOffsets.add(new CoordLoc(-2, i, 1));
            middleCoordOffsets.add(new CoordLoc(-1, i, 2));
            middleCoordOffsets.add(new CoordLoc(0, i, 2));
            middleCoordOffsets.add(new CoordLoc(1, i, 2));
            middleCoordOffsets.add(new CoordLoc(-1, i, -2));
            middleCoordOffsets.add(new CoordLoc(0, i, -2));
            middleCoordOffsets.add(new CoordLoc(1, i, -2));
        }
        topCoordOffsets.add(new CoordLoc(0, 4, 0));
        topCoordOffsets.add(new CoordLoc(0, 4, 1));
        topCoordOffsets.add(new CoordLoc(0, 4, -1));
        topCoordOffsets.add(new CoordLoc(1, 4, 0));
        topCoordOffsets.add(new CoordLoc(1, 4, 1));
        topCoordOffsets.add(new CoordLoc(1, 4, -1));
        topCoordOffsets.add(new CoordLoc(-1, 4, 0));
        topCoordOffsets.add(new CoordLoc(-1, 4, 1));
        topCoordOffsets.add(new CoordLoc(-1, 4, -1));
    }
}
