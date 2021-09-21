package net.gcnt.skywarsreloaded.data;

import java.util.UUID;

public interface SWPlayer {

    UUID getUuid();

    void setOnline(boolean online);

    boolean isOnline();
}
