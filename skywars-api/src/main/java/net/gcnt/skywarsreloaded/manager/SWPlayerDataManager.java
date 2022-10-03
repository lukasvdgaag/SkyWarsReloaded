package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerData;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerStats;
import net.gcnt.skywarsreloaded.data.player.stats.SWPlayerUnlockables;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

public interface SWPlayerDataManager {

    SWPlayerData createSWPlayerDataInstance();

    SWPlayerStats createSWPlayerStatsInstance();

    SWPlayerUnlockables createSWPlayerUnlockablesInstance(SWPlayer player);

    void loadAllPlayerData();
}
