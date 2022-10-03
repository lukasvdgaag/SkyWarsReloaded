package net.gcnt.skywarsreloaded.data.player;

public interface SWPlayerStorage extends SWPlayerStorageUnit {

    void initStatsTable();

    void initUnlockablesTable();

    SWUnlockablesStorage getUnlockablesStorage();

}
