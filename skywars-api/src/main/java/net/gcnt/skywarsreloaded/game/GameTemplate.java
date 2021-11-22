package net.gcnt.skywarsreloaded.game;

import net.gcnt.skywarsreloaded.utils.SWCoord;
import net.gcnt.skywarsreloaded.utils.results.SpawnAddResult;

import java.util.List;

public interface GameTemplate {

    String getName();

    String getCreator();

    void setCreator(String creator);

    String getDisplayName();

    void setDisplayName(String value);

    int getTeamSize();

    void setTeamSize(int size);

    int getMinPlayers();

    void setMinPlayers(int amount);

    SWCoord getWaitingLobbySpawn();

    void setWaitingLobbySpawn(SWCoord loc);

    SWCoord getSpectateSpawn();

    void setSpectateSpawn(SWCoord loc);

    void disable();

    void enable();

    void loadData();

    void saveData();

    boolean addChest(SWCoord loc);

    boolean removeChest(SWCoord loc);

    List<SWCoord> getChests();

    SpawnAddResult addSpawn(int team, SWCoord loc);

    void removeSpawn(SWCoord loc);

    boolean isEnabled();

    List<List<SWCoord>> getTeamSpawnpoints();

    int getBorderRadius();

    void setBorderRadius(int radus);

}
