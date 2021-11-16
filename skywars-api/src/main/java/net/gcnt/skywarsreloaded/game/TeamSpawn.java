package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.SWCoord;

import java.util.List;

public interface TeamSpawn {

    boolean isOccupied();

    SWCoord getLocation();

    List<GamePlayer> getPlayers();

    void addPlayer(GamePlayer player);

    void removePlayer(GamePlayer player);

    GameTeam getTeam();

    void determineCageDesign();

    void updateCage();

    void removeCage();

    TeamCage getCage();

    void setCage(TeamCage cage);

}
