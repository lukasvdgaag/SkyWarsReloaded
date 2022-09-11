package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.game.chest.filler.SWChestFiller;

public interface SWChestFillerManager {

    SWChestFiller getFillerByName(String name);

}
