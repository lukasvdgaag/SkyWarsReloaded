package com.walrusone.skywarsreloaded.game.cages;

import com.walrusone.skywarsreloaded.menus.gameoptions.objects.CoordLoc;

public class DomeCage extends Cage {
    public DomeCage() {
        cageType = CageType.DOME;
        bottomCoordOffsets.add(new CoordLoc(0, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(4, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(3, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(2, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(3, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(2, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(2, 0, 2));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 2));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 3));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(-2, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(0, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(0, 0, 2));
        bottomCoordOffsets.add(new CoordLoc(0, 0, 3));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, 2));
        bottomCoordOffsets.add(new CoordLoc(-2, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(0, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(0, 0, -2));
        bottomCoordOffsets.add(new CoordLoc(1, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(1, 0, -2));
        bottomCoordOffsets.add(new CoordLoc(2, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(4, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(3, 0, 2));
        bottomCoordOffsets.add(new CoordLoc(2, 0, 3));
        bottomCoordOffsets.add(new CoordLoc(1, 0, 4));
        bottomCoordOffsets.add(new CoordLoc(0, 0, 4));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, 3));
        bottomCoordOffsets.add(new CoordLoc(-2, 0, 2));
        bottomCoordOffsets.add(new CoordLoc(-3, 0, 1));
        bottomCoordOffsets.add(new CoordLoc(-3, 0, 0));
        bottomCoordOffsets.add(new CoordLoc(3, 0, -1));
        bottomCoordOffsets.add(new CoordLoc(2, 0, -2));
        bottomCoordOffsets.add(new CoordLoc(1, 0, -3));
        bottomCoordOffsets.add(new CoordLoc(0, 0, -3));
        bottomCoordOffsets.add(new CoordLoc(-1, 0, -2));
        bottomCoordOffsets.add(new CoordLoc(-2, 0, -1));

        middleCoordOffsets.add(new CoordLoc(4, 1, 0));
        middleCoordOffsets.add(new CoordLoc(4, 1, 1));
        middleCoordOffsets.add(new CoordLoc(3, 1, 2));
        middleCoordOffsets.add(new CoordLoc(2, 1, 3));
        middleCoordOffsets.add(new CoordLoc(1, 1, 4));
        middleCoordOffsets.add(new CoordLoc(0, 1, 4));
        middleCoordOffsets.add(new CoordLoc(-1, 1, 3));
        middleCoordOffsets.add(new CoordLoc(-2, 1, 2));
        middleCoordOffsets.add(new CoordLoc(-3, 1, 1));
        middleCoordOffsets.add(new CoordLoc(-3, 1, 0));
        middleCoordOffsets.add(new CoordLoc(3, 1, -1));
        middleCoordOffsets.add(new CoordLoc(2, 1, -2));
        middleCoordOffsets.add(new CoordLoc(1, 1, -3));
        middleCoordOffsets.add(new CoordLoc(0, 1, -3));
        middleCoordOffsets.add(new CoordLoc(-1, 1, -2));
        middleCoordOffsets.add(new CoordLoc(-2, 1, -1));

        middleCoordOffsets.add(new CoordLoc(3, 2, 0));
        middleCoordOffsets.add(new CoordLoc(3, 2, 1));
        middleCoordOffsets.add(new CoordLoc(2, 2, 2));
        middleCoordOffsets.add(new CoordLoc(1, 2, 3));
        middleCoordOffsets.add(new CoordLoc(0, 2, 3));
        topCoordOffsets.add(new CoordLoc(-1, 2, 2));
        topCoordOffsets.add(new CoordLoc(-2, 2, 1));
        topCoordOffsets.add(new CoordLoc(-2, 2, 0));
        topCoordOffsets.add(new CoordLoc(2, 2, -1));
        topCoordOffsets.add(new CoordLoc(1, 2, -2));
        topCoordOffsets.add(new CoordLoc(0, 2, -2));
        topCoordOffsets.add(new CoordLoc(-1, 2, -1));

        topCoordOffsets.add(new CoordLoc(2, 3, 0));
        topCoordOffsets.add(new CoordLoc(2, 3, 1));
        topCoordOffsets.add(new CoordLoc(1, 3, 2));
        topCoordOffsets.add(new CoordLoc(0, 3, 2));
        topCoordOffsets.add(new CoordLoc(-1, 3, 0));
        topCoordOffsets.add(new CoordLoc(-1, 3, 1));
        topCoordOffsets.add(new CoordLoc(0, 3, -1));
        topCoordOffsets.add(new CoordLoc(1, 3, -1));

        topCoordOffsets.add(new CoordLoc(0, 4, 0));
        topCoordOffsets.add(new CoordLoc(1, 4, 0));
        topCoordOffsets.add(new CoordLoc(1, 4, 1));
        topCoordOffsets.add(new CoordLoc(0, 4, 1));
    }
}
