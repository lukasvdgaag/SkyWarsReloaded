package net.gcnt.skywarsreloaded.wrapper.entity;

import java.util.UUID;

/**
 * General data about a player that is independent of any running state of games or teams
 */
public interface SWOfflinePlayer {

    UUID getUuid();

    boolean isOnline();

    void setOnline(boolean online);

}
