package net.gcnt.skywarsreloaded.wrapper;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;

import java.util.UUID;

public interface SWPlayer {

    UUID getUuid();

    boolean isOnline();

    void setOnline(boolean online);

    SWPlayerData getPlayerData();

    void setPlayerData(SWPlayerData playerData);
}
