package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;
import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.List;

public interface Spawn {

    boolean isOccupied();

    Coord getLocation();

    List<SWPlayer> getPlayers();

    void addPlayer(SWPlayer player);

    void removePlayer(SWPlayer player);

    Team getTeam();

    void determineCageDesign();

    void updateCage();

}
