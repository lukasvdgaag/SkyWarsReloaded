package net.gcnt.skywarsreloaded.data.player;

public interface SWPlayerDataManager {

    SWPlayerData createSWPlayerDataInstance();

    SWPlayerStats createSWPlayerStatsInstance();

    void loadAllPlayerData();
}
