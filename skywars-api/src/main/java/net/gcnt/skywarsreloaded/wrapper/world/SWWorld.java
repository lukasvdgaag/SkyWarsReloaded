package net.gcnt.skywarsreloaded.wrapper.world;

import net.gcnt.skywarsreloaded.utils.Item;
import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.wrapper.player.SWPlayer;

import java.util.List;

public interface SWWorld {

    List<SWPlayer> getAllPlayers();

    void setBlockAt(SWCoord location, Item block);

    String getName();

}
