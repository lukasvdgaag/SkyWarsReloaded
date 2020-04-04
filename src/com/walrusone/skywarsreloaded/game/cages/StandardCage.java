package com.walrusone.skywarsreloaded.game.cages;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;

public class StandardCage extends Cage {
    public StandardCage() {
        cageType = CageType.STANDARD;
        bottomCoordOffsets.add(new CoordLoc(0, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(0, 1, 1));
        bottomCoordOffsets.add(new CoordLoc(0, 1, -1));
        bottomCoordOffsets.add(new CoordLoc(1, 1, 0));
        middleCoordOffsets.add(new CoordLoc(-1, 1, 0));
        middleCoordOffsets.add(new CoordLoc(0, 2, 1));
        middleCoordOffsets.add(new CoordLoc(0, 2, -1));
        middleCoordOffsets.add(new CoordLoc(1, 2, 0));
        middleCoordOffsets.add(new CoordLoc(-1, 2, 0));
        middleCoordOffsets.add(new CoordLoc(0, 3, 1));
        topCoordOffsets.add(new CoordLoc(0, 3, -1));
        topCoordOffsets.add(new CoordLoc(1, 3, 0));
        topCoordOffsets.add(new CoordLoc(-1, 3, 0));
        topCoordOffsets.add(new CoordLoc(0, 4, 0));
    }
}
