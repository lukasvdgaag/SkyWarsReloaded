package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public interface TeamSpawn {

    boolean isOccupied();

    Coord getLocation();

    List<GamePlayer> getPlayers();

    void addPlayer(GamePlayer player);

    void removePlayer(GamePlayer player);

    GameTeam getTeam();

    void determineCageDesign();

    void updateCage();

}
