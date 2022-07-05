package net.gcnt.skywarsreloaded.manager;

import net.gcnt.skywarsreloaded.wrapper.entity.SWEntity;
import net.gcnt.skywarsreloaded.wrapper.entity.SWPlayer;

import java.util.List;
import java.util.UUID;

public interface SWPlayerManager {

    SWEntity getEntityFromUUID(UUID uuid);

    SWPlayer getPlayerByUUID(UUID uuid);

    SWPlayer initPlayer(UUID uuid);

    List<SWPlayer> getPlayers();

    void removePlayer(SWPlayer player);

    void initAllPlayers();
}
