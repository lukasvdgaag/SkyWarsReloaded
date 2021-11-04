package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.Coord;

import java.util.List;

public interface GameData {

    String getName();

    String getCreator();

    String getDisplayName();

    int getTeamSize();

    Coord getWaitingLobbySpawn();

    void setWaitingLobbySpawn(Coord loc);

    Coord getSpectateSpawn();

    void setSpectateSpawn(Coord loc);

    void disable();

    void loadData();

    void saveData();

    boolean addChest(Coord loc);

    boolean removeChest(Coord loc);

    List<Coord> getChests();


}
