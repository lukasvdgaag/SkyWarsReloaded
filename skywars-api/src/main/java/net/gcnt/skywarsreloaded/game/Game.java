package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public interface Game {

    String getName();

    String getCreator();

    String getDisplayName();

    List<Team> getTeams();

    int getTeamSize();

    Coord getWaitingLobbySpawn();

    void setWaitingLobbySpawn(Coord loc);

    Coord getSpectateSpawn();

    void setSpectateSpawn(Coord loc);

    void disable();

    void loadData();

    void saveData();

    void addChest(Coord loc);

    void removeChest(Coord loc);

    List<Coord> getChests();


}
