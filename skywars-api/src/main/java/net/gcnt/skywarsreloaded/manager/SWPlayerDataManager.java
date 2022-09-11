package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.SWPlayerStats;

public interface SWPlayerDataManager {

    SWPlayerData createSWPlayerDataInstance();

    SWPlayerStats createSWPlayerStatsInstance();

    void loadAllPlayerData();
}
