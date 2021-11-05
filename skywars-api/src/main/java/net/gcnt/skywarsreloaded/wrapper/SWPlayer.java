package net.gcnt.skywarsreloaded.wrapper;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;

import java.util.UUID;

/**
 * General data about a player that is independent of any running state of games or teams
 */
public interface SWPlayer {

    UUID getUuid();

    boolean isOnline();

    void setOnline(boolean online);

    SWPlayerData getPlayerData();

    void setPlayerData(SWPlayerData playerData);
}
