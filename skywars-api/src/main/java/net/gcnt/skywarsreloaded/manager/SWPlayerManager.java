package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.SWPlayer;

import java.util.List;
import java.util.UUID;

public interface SWPlayerManager {

    SWPlayer getPlayerByUUID(UUID uuid);

    SWPlayer initPlayer(UUID uuid);

    List<SWPlayer> getPlayers();

    void removePlayer(SWPlayer player);
}
