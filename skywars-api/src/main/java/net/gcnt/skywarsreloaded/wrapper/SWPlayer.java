package net.gcnt.skywarsreloaded.wrapper;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;

import java.util.UUID;

public interface SWPlayer {

    UUID getUuid();

    void setOnline(boolean online);

    boolean isOnline();

    SWPlayerData getPlayerData();
}
