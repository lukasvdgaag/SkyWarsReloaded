package net.gcnt.skywarsreloaded.wrapper.player;

import java.util.UUID;

public class AbstractSWOfflinePlayer implements SWOfflinePlayer {

    private final UUID uuid;
    private boolean online;

    public AbstractSWOfflinePlayer(UUID uuidIn, boolean onlineIn) {
        this.uuid = uuidIn;
        this.online = onlineIn;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public void setOnline(boolean onlineIn) {
        this.online = onlineIn;
    }
}
