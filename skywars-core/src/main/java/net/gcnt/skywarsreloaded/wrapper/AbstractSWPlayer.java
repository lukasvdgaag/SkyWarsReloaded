package net.gcnt.skywarsreloaded.wrapper;

import net.gcnt.skywarsreloaded.data.player.SWPlayerData;

import java.util.UUID;

public abstract class AbstractSWPlayer implements SWPlayer {

    private final UUID uuid;
    private boolean online;
    private SWPlayerData playerData;

    public AbstractSWPlayer(UUID uuid, boolean online) {
        this.uuid = uuid;
        this.online = online;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public void setOnline(boolean online) {
        this.online = online;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public SWPlayerData getPlayerData() {
        return this.playerData;
    }

    @Override
    public void setPlayerData(SWPlayerData playerData) {
        this.playerData = playerData;
    }
}
